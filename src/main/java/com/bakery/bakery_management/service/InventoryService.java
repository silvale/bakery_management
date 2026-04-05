package com.bakery.bakery_management.service;


import com.bakery.bakery_management.Utils.MappingUtils;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.PageResult;
import com.bakery.bakery_management.domain.dto.request.*;
import com.bakery.bakery_management.domain.dto.response.ExportResponse;
import com.bakery.bakery_management.domain.dto.response.ImportResponse;
import com.bakery.bakery_management.domain.dto.response.InventoryResponse;
import com.bakery.bakery_management.domain.dto.response.ProductPriceResponse;
import com.bakery.bakery_management.domain.entity.Inventory;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.domain.entity.StockTransaction;
import com.bakery.bakery_management.domain.enums.ExpiryInputType;
import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.exception.ErrorCode;
import com.bakery.bakery_management.mapper.AdminBaseMapper;
import com.bakery.bakery_management.mapper.InventoryMapper;
import com.bakery.bakery_management.repository.InventoryRepository;
import com.bakery.bakery_management.repository.ProductRepository;
import com.bakery.bakery_management.repository.StockTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService extends AdminOperationService<InventoryRequest, InventoryResponse, Inventory> {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final StockTransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final ProductPriceService priceService;
    private final UnitService unitService;
    private final ProductService productService;


    // --- NHẬP KHO ---
    @Transactional
    public ImportResponse processImport(ImportRequest request) {
        for (ImportItemRequest item : request.getItems()) {
            Product product = productRepository.findByCode(item.getProductCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "SP không tồn tại"));

            LocalDateTime finalExpiry = calculateExpiry(product, item);

            // 1. Sync Price
            priceService.syncPrice(item.getPriceCode(), product.getCode(), item.getUnitCode(), item.getCostPrice(), BigDecimal.ZERO, true, product.getType());

            // 2. Update Inventory
            item.setReferenceId(request.getReferenceId());
            Inventory inv = inventoryRepository.findByUniqueStock(product.getCode(), request.getWarehouseType(), finalExpiry)
                    .orElseGet(() -> createNewInventory(request.getWarehouseType(), item.getProductCode(), item.getUnitCode(), item.getReferenceId(), finalExpiry));
            inv.setQuantity(inv.getQuantity().add(item.getQuantity()));
            inventoryRepository.save(inv);

            // 3. Save Transaction
            saveTx(request.getReferenceId(), item.getProductCode(), item.getUnitCode(), item.getQuantity(),
                    finalExpiry, request.getWarehouseType(), TransactionType.IMPORT, request.getReferenceType());
        }

        return buildImportResponse(request);
    }

    @Transactional
    public ExportResponse processExport(ExportRequest request) {

        for (ExportItemRequest item : request.getItems()) {

            Product product = productRepository.findByCode(item.getProductCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "SP không tồn tại"));

            switch (request.getTransactionType()) {

                case EXPORT -> handleExport(request, item);

                case RETURN -> handleReturn(request, item);

                case DISCARD -> handleDiscard(request, item);

                case ADJUSTMENT -> handleAdjustment(request, item);
            }
        }

        return buildExportResponse(request);
    }

    private void handleExport(ExportRequest request, ExportItemRequest item) {

        switch (request.getReferenceType()) {

            case EXPORT_TO_KITCHEN -> transferStock(
                    WarehouseType.MAIN_STORAGE,
                    WarehouseType.KITCHEN,
                    request,
                    item
            );

            case EXPORT_TO_STORE -> transferStock(
                    WarehouseType.KITCHEN,
                    WarehouseType.STORE,
                    request,
                    item
            );
        }
    }

    private void handleReturn(ExportRequest request, ExportItemRequest item) {

        switch (request.getReferenceType()) {

            // 🔁 KITCHEN → MAIN_STORAGE
            case RETURN_TO_STORAGE -> transferStock(
                    WarehouseType.KITCHEN,
                    WarehouseType.MAIN_STORAGE,
                    request,
                    item
            );

            // ❌ Trả NCC / loại khỏi hệ thống
            case RETURN_TO_SUPPLIER, INVALID_QUALITY, INVALID_QUANTITY -> {

                Map<LocalDateTime, BigDecimal> batches = deductStockFEFO(
                        request.getWarehouseType(),
                        item.getProductCode(),
                        item.getQuantity()
                );

                batches.forEach((expiry, qty) -> {
                    // EXPORT ra ngoài (LUÔN âm)
                    saveTx(
                            request.getReferenceId(),
                            item.getProductCode(),
                            item.getUnitCode(),
                            qty.negate(),
                            expiry,
                            request.getWarehouseType(),
                            TransactionType.RETURN,
                            request.getReferenceType()
                    );
                });
            }
        }
    }

    private void handleDiscard(ExportRequest request, ExportItemRequest item) {

        if (request.getReferenceType() == ReferenceType.DAMAGED
                || request.getReferenceType() == ReferenceType.EXPIRED) {

            Map<LocalDateTime, BigDecimal> batches = deductStockFEFO(
                    request.getWarehouseType(),
                    item.getProductCode(),
                    item.getQuantity()
            );

            batches.forEach((expiry, qty) -> {
                // EXPORT ra ngoài (LUÔN âm)
                saveTx(
                        request.getReferenceId(),
                        item.getProductCode(),
                        item.getUnitCode(),
                        qty.negate(),
                        expiry,
                        request.getWarehouseType(),
                        TransactionType.DISCARD,
                        request.getReferenceType()
                );
            });
        }
    }

    private void handleAdjustment(ExportRequest request, ExportItemRequest item) {

        switch (request.getReferenceType()) {

            // ➕ Tăng kho
            case INCREATE -> {

                addStock(
                        request.getWarehouseType(),
                        item.getProductCode(),
                        item.getUnitCode(),
                        request.getReferenceId(),
                        item.getQuantity(),
                        item.getExpiryDate()
                );

                saveTx(
                        request.getReferenceId(),
                        item.getProductCode(),
                        item.getUnitCode(),
                        item.getQuantity(), // dương
                        item.getExpiryDate(),
                        request.getWarehouseType(),
                        TransactionType.ADJUSTMENT,
                        request.getReferenceType()
                );
            }

            // ➖ Giảm kho
            case DECREATE -> {

                Map<LocalDateTime, BigDecimal> batches = deductStockFEFO(
                        request.getWarehouseType(),
                        item.getProductCode(),
                        item.getQuantity()
                );

                batches.forEach((expiry, qty) -> {
                    saveTx(
                            request.getReferenceId(),
                            item.getProductCode(),
                            item.getUnitCode(),
                            qty.negate(), // âm
                            expiry,
                            request.getWarehouseType(),
                            TransactionType.ADJUSTMENT,
                            request.getReferenceType()
                    );
                });
            }
        }
    }


    private Map<LocalDateTime, BigDecimal> deductStockFEFO(
            WarehouseType warehouse,
            String productCode,
            BigDecimal totalDeduct) {

        List<Inventory> inventories = inventoryRepository
                .findByWarehouseTypeAndProductCodeOrderByExpiryDateAsc(warehouse, productCode);

        validateStock(inventories, totalDeduct, warehouse);

        Map<LocalDateTime, BigDecimal> deductedDetails = new LinkedHashMap<>();
        BigDecimal remaining = totalDeduct;

        for (Inventory inv : inventories) {

            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal deductQty = inv.getQuantity().min(remaining);

            if (deductQty.compareTo(BigDecimal.ZERO) <= 0) continue;

            // update inventory
            inv.setQuantity(inv.getQuantity().subtract(deductQty));
            inventoryRepository.save(inv);

            deductedDetails.put(inv.getExpiryDate(), deductQty);

            remaining = remaining.subtract(deductQty);
        }

        return deductedDetails;
    }

    private void transferStock(WarehouseType from,
                               WarehouseType to,
                               ExportRequest request,
                               ExportItemRequest item) {

        Map<LocalDateTime, BigDecimal> batches = deductStockFEFO(
                from,
                item.getProductCode(),
                item.getQuantity()
        );

        batches.forEach((expiry, qty) -> {

            // 1. EXPORT từ kho nguồn (LUÔN âm)
            saveTx(
                    request.getReferenceId(),
                    item.getProductCode(),
                    item.getUnitCode(),
                    qty.negate(),   // 🔥 QUAN TRỌNG
                    expiry,
                    from,
                    TransactionType.EXPORT,
                    request.getReferenceType()
            );

            // 2. IMPORT vào kho đích (LUÔN dương)
            addStock(
                    to,
                    item.getProductCode(),
                    item.getUnitCode(),
                    request.getReferenceId(),
                    qty,
                    expiry
            );

            saveTx(
                    request.getReferenceId(),
                    item.getProductCode(),
                    item.getUnitCode(),
                    qty,
                    expiry,
                    to,
                    TransactionType.IMPORT,
                    request.getReferenceType()
            );
        });
    }

    private void saveTx(String refId,
                        String pCode,
                        String unitCode,
                        BigDecimal qty,
                        LocalDateTime exp,
                        WarehouseType wType,
                        TransactionType tType,
                        ReferenceType rType) {

        StockTransaction tx = new StockTransaction();
        tx.setReferenceId(refId); // 🔥 LUÔN là PXK, KHÔNG dùng PNK
        tx.setProductCode(pCode);
        tx.setUnitCode(unitCode);
        tx.setQuantity(qty); // 🔥 âm/dương đã chuẩn từ caller
        tx.setExpiryDate(exp);
        tx.setWarehouseType(wType);
        tx.setTransactionType(tType);
        tx.setReferenceType(rType);

        transactionRepository.save(tx);
    }


    private void validateStock(List<Inventory> inventories,
                               BigDecimal required,
                               WarehouseType warehouse) {
        BigDecimal available = inventories.stream()
                .map(Inventory::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (available.compareTo(required) < 0) {
            throw new BusinessException(
                    ErrorCode.INSUFFICIENT_STOCK,
                    "Kho " + warehouse + " không đủ hàng"
            );
        }
    }

    //
//    private BigDecimal calculateDeductQty(BigDecimal currentQty, BigDecimal remaining) {
//        if (currentQty.compareTo(remaining) >= 0) {
//            return remaining;
//        }
//        return currentQty;
//    }
//
    private void addStock(WarehouseType warehouse,
                          String productCode,
                          String unitCode,
                          String referenceId,
                          BigDecimal amount,
                          LocalDateTime expiryDate) {
        Inventory inventory = inventoryRepository
                .findByWarehouseTypeAndProductCodeAndExpiryDate(warehouse, productCode, expiryDate)
                .orElseGet(() -> createNewInventory(warehouse, productCode, unitCode, referenceId, expiryDate));

        inventory.setQuantity(inventory.getQuantity().add(amount));

        inventoryRepository.save(inventory);
    }

    private Inventory createNewInventory(WarehouseType warehouse,
                                         String productCode,
                                         String unitCode,
                                         String referenceId,
                                         LocalDateTime expiryDate) {
        Inventory inv = new Inventory();
        inv.setWarehouseType(warehouse);
        inv.setProductCode(productCode);
        inv.setUnitCode(unitCode);
        inv.setReferenceId(referenceId);
        inv.setExpiryDate(expiryDate);
        inv.setQuantity(BigDecimal.ZERO);
        return inv;
    }

    private ImportResponse buildImportResponse(ImportRequest request) {
        return ImportResponse.builder()
                .referenceId(request.getReferenceId())
                .importedAt(LocalDateTime.now()) // Thời điểm xử lý xong
                .warehouseType(request.getWarehouseType())
                .transactionType(request.getTransactionType())
                .referenceType(request.getReferenceType())
                .totalItems(request.getItems() != null ? request.getItems().size() : 0)
                .message(String.format("Nhập kho thành công phiếu %s vào kho %s",
                        request.getReferenceId(),
                        request.getWarehouseType()))
                .build();
    }

    private ExportResponse buildExportResponse(ExportRequest request) {
        return ExportResponse.builder()
                .referenceId(request.getReferenceId())
                .importedAt(LocalDateTime.now()) // Thời điểm xử lý xong
                .warehouseType(request.getWarehouseType())
                .transactionType(request.getTransactionType())
                .referenceType(request.getReferenceType())
                .totalItems(request.getItems() != null ? request.getItems().size() : 0)
                .message(String.format("Nhập kho thành công phiếu %s vào kho %s",
                        request.getReferenceId(),
                        request.getWarehouseType()))
                .build();
    }

    public PageResult<InventoryResponse> getListInventoryByType(Pageable pageable, String warehoueType) {
        List<Inventory> entities = inventoryRepository.findByWarehouseType(WarehouseType.valueOf(warehoueType));

        Map<String, Inventory> grouped = entities.stream()
                .collect(Collectors.toMap(
                        Inventory::getProductCode,
                        inv -> {
                            Inventory i = new Inventory();
                            i.setProductCode(inv.getProductCode());
                            i.setUnitCode(inv.getUnitCode());
                            i.setQuantity(inv.getQuantity());
                            i.setWarehouseType(inv.getWarehouseType());
                            i.setReferenceId(null); // theo yêu cầu
                            return i;
                        },
                        (i1, i2) -> {
                            i1.setQuantity(i1.getQuantity().add(i2.getQuantity()));
                            return i1;
                        }
                ));

        List<Inventory> groupedList = grouped.values().stream()
                .filter(inv -> inv.getQuantity() != null && inv.getQuantity().compareTo(BigDecimal.ZERO) > 0)
                .toList();

        List<InventoryResponse> responseList = groupedList.stream()
                .map(getMapper()::toResponse)
                .collect(Collectors.toList());

        MappingUtils.mapReference(
                groupedList,
                responseList,
                Inventory::getUnitCode,
                unitService::getMapByCodes,
                InventoryResponse::setUnit
        );

        MappingUtils.mapReference(
                groupedList,
                responseList,
                Inventory::getProductCode,
                productService::getMapByCodes,
                InventoryResponse::setProduct
        );

        List<String> codes = groupedList.stream()
                .map(Inventory::getProductCode)
                .filter(Objects::nonNull)
                .toList();

        Map<String, ProductPriceResponse> priceMap = priceService.getDefaultPriceResponsesByCodes(codes);
        for (InventoryResponse invRes : responseList) {
            String code = invRes.getProduct().getCode();
            ProductPriceResponse priceRes = priceMap.get(code);
            invRes.setCurrentCostPrice(priceRes != null ? priceRes.getCostPrice() : BigDecimal.ZERO);
            invRes.setCurrentSalesPrice(priceRes != null ? priceRes.getSalePrice() : BigDecimal.ZERO);
        }

        return PageResult.ofPage(new PageImpl<>(responseList, pageable, entities.size()));
    }

    @Override
    protected JpaRepository<Inventory, UUID> getRepository() {
        return inventoryRepository;
    }

    @Override
    protected AdminBaseMapper<InventoryRequest, InventoryResponse, Inventory> getMapper() {
        return inventoryMapper;
    }

    @Override
    protected void afterGetList(List<Inventory> entities, List<InventoryResponse> responses) {
        MappingUtils.mapReference(
                entities,
                responses,
                Inventory::getUnitCode,
                unitService::getMapByCodes,
                InventoryResponse::setUnit
        );
    }

    @Transactional
    public void stocktake(ExportRequest request) {

        LocalDateTime today = request.getProcessDate();

        for (ExportItemRequest item : request.getItems()) {

            String product = item.getProductCode();
            LocalDateTime expiry = item.getExpiryDate();
            BigDecimal remaining = item.getQuantity();

            // 1. Lấy inventory hiện tại
            Inventory inv = inventoryRepository
                    .findByProductCodeAndWarehouseTypeAndExpiryDate(product, WarehouseType.STORE, expiry)
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            BigDecimal opening = inv.getQuantity();

            // ❗ validate sớm
            if (remaining.compareTo(opening) > 0) {
                throw new RuntimeException("Remaining > opening for product " + product);
            }

            BigDecimal sold;
            BigDecimal discard;

            // 2. Logic chính (FIX compare date)
            if (expiry.toLocalDate().equals(today.toLocalDate())) {
                discard = remaining;
                sold = opening.subtract(discard);
                remaining = BigDecimal.ZERO;
            } else {
                discard = BigDecimal.ZERO;
                sold = opening.subtract(remaining);
            }

            // 3. Validate
            if (sold.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Sold < 0 for product " + product);
            }

            // 4. SALE transaction
            if (sold.compareTo(BigDecimal.ZERO) > 0) {

                saveTx(request.getReferenceId(), product, item.getUnitCode(), sold,
                        today, request.getWarehouseType(), TransactionType.SALE, request.getReferenceType());

                // ❗ FIX: update inventory đúng expiry + trừ đi sold
                Inventory invStore = inventoryRepository
                        .findByProductCodeAndWarehouseTypeAndExpiryDate(product, request.getWarehouseType(), expiry)
                        .orElseThrow(() -> new RuntimeException("Inventory not found"));

                invStore.setQuantity(invStore.getQuantity().subtract(sold));

                if (invStore.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                    throw new RuntimeException("Inventory < 0 after SALE");
                }

                inventoryRepository.save(invStore);
            }

            // 5. DISCARD transaction
            if (discard.compareTo(BigDecimal.ZERO) > 0) {

                saveTx(request.getReferenceId(), product, item.getUnitCode(), discard,
                        today, request.getWarehouseType(), TransactionType.DISCARD, request.getReferenceType());

                // ❗ FIX: update inventory đúng expiry + trừ đi discard
                Inventory invStore = inventoryRepository
                        .findByProductCodeAndWarehouseTypeAndExpiryDate(product, request.getWarehouseType(), expiry)
                        .orElseThrow(() -> new RuntimeException("Inventory not found"));

                invStore.setQuantity(invStore.getQuantity().subtract(discard));

                if (invStore.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                    throw new RuntimeException("Inventory < 0 after DISCARD");
                }

                inventoryRepository.save(invStore);
            }
        }
    }

    private LocalDateTime calculateExpiry(Product p, ImportItemRequest item) {
        ExpiryInputType type = item.getExpiryType() != null ? item.getExpiryType() : p.getExpiryType();
        if (type == ExpiryInputType.NONE || type == null) return null;
        return switch (type) {
            case TODAY -> LocalDateTime.now().with(LocalTime.MAX);
            case DATE ->
                    item.getManualExpiryDate() != null ? item.getManualExpiryDate() : p.getFixedExpiryDate().atTime(LocalTime.MAX);
            case NUMBER ->
                    LocalDateTime.now().with(LocalTime.MAX).plusDays(item.getManualExpiryDays() != null ? item.getManualExpiryDays() : p.getDefaultExpiryDays());
            default -> null;
        };
    }
}