package com.bakery.bakery_management.service;


import com.bakery.bakery_management.Utils.MappingUtils;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.dto.request.*;
import com.bakery.bakery_management.domain.dto.response.*;
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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.bakery.bakery_management.domain.enums.TransactionType.IMPORT;

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
        switch (request.getTransactionType()) {
            case IMPORT -> {
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
                            finalExpiry, request.getWarehouseType(), IMPORT, request.getReferenceType());
                }
            }
            case RETURN_TO_SUPPLIER -> {
                if (ReferenceType.INVALID_QUALITY.equals(request.getReferenceType()) ||
                        ReferenceType.INVALID_LIST.equals(request.getReferenceType()) ||
                        ReferenceType.INVALID_QUANTITY.equals(request.getReferenceType()) ||
                        ReferenceType.DAMAGED.equals(request.getReferenceType())
                ) {
                    ExportRequest convert = new ExportRequest();
                    convert.setReferenceId(request.getReferenceId());
                    convert.setWarehouseType(request.getWarehouseType());
                    convert.setTransactionType(request.getTransactionType());
                    convert.setReferenceType(request.getReferenceType());
                    convert.setProcessDate(request.getProcessDate());
                    convert.setNote(request.getNote());
                    List<ExportItemRequest> requests = new ArrayList<>();
                    for (ImportItemRequest item : request.getItems()) {
                        ExportItemRequest expRequest = new ExportItemRequest();
                        expRequest.setReferenceId(item.getReferenceId());
                        expRequest.setProductCode(item.getProductCode());
                        expRequest.setPriceCode(item.getPriceCode());
                        expRequest.setUnitCode(item.getUnitCode());
                        expRequest.setQuantity(item.getQuantity());
                        expRequest.setCostPrice(item.getCostPrice());
                        requests.add(expRequest);
                    }
                    convert.setItems(requests);

                    for (ExportItemRequest item : convert.getItems()) {
                        // Check product exist
                        productRepository.findByCode(item.getProductCode())
                                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "SP không tồn tại"));
                        handleReturnToSupplier(convert, item);
                    }
                }
            }


        }
        return buildImportResponse(request);
    }

    @Transactional
    public ExportResponse processExport(ExportRequest request) {

        for (ExportItemRequest item : request.getItems()) {
            // Check product exist
            productRepository.findByCode(item.getProductCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "SP không tồn tại"));

            switch (request.getTransactionType()) {

                case EXPORT -> handleExport(request, item);

                case RETURN_TO_STORAGE -> handleReturnToStorage(request, item);

                case RETURN_TO_SUPPLIER -> handleReturnToSupplier(request, item);

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

    private void handleReturnToStorage(ExportRequest request, ExportItemRequest item) {

        switch (request.getReferenceType()) {
            // 🔁 KITCHEN → MAIN_STORAGE
            case INVALID_QUALITY, INVALID_QUANTITY, INVALID_LIST, DAMAGED -> transferStock(
                    WarehouseType.KITCHEN,
                    WarehouseType.MAIN_STORAGE,
                    request,
                    item
            );
        }
    }

    private void handleReturnToSupplier(ExportRequest request, ExportItemRequest item) {

        switch (request.getReferenceType()) {
            case DAMAGED, INVALID_QUALITY, INVALID_QUANTITY, INVALID_LIST -> {
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
                            TransactionType.RETURN_TO_SUPPLIER,
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
                    IMPORT,
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

    public List<InventoryResponse> getListInventoryByType(String warehoueType) {
        List<Inventory> entities = inventoryRepository.findProductAvailableInInventory(WarehouseType.valueOf(warehoueType));

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

        List<Inventory> groupedList = grouped.values().stream().toList();

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
            if (priceRes.getIsDefault()) {
                invRes.setPriceCodeDefault(priceRes.getCode());
                invRes.setCurrentCostPrice(priceRes.getCostPrice());
                invRes.setCurrentSalesPrice(priceRes.getSalePrice());
            }

        }

        return responseList;
    }

    public List<ProductResponse> getAvailableProduct() {
        List<InventoryResponse> inventories =
                getListInventoryByType(WarehouseType.MAIN_STORAGE.name());

        if (inventories.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> codes = inventories.stream()
                .map(i -> i.getProduct().getCode())
                .collect(Collectors.toSet());

        Map<String, Product> productMap = productRepository.findByCodeIn(codes)
                .stream()
                .collect(Collectors.toMap(Product::getCode, p -> p));

        return inventories.stream()
                .map(inv -> {
                    Product product = productMap.get(inv.getProduct().getCode());
                    if (product == null) return null;

                    return mapFromInventory(inv, product);
                })
                .filter(Objects::nonNull)
                .filter(res -> res.getAvailableQuantity().compareTo(BigDecimal.ZERO) > 0)
                .toList();
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


    private ProductResponse mapFromInventory(InventoryResponse inv, Product product) {
        ProductResponse res = new ProductResponse();
        res.setCode(inv.getProduct().getCode());
        res.setName(inv.getProduct().getName());
        res.setType(product.getType());
        res.setUnit(inv.getUnit());
        res.setPriceCodeDefault(inv.getPriceCodeDefault());
        res.setCurrentCostPrice(inv.getCurrentCostPrice());
        res.setCurrentSalesPrice(inv.getCurrentSalesPrice());
        res.setExpiryType(product.getExpiryType());
        res.setDefaultExpiryDays(product.getDefaultExpiryDays());
        res.setAvailableQuantity(inv.getQuantity());
        return res;
    }
}