package com.bakery.bakery_management.service;


import com.bakery.bakery_management.Utils.MappingUtils;
import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.PageResult;
import com.bakery.bakery_management.domain.dto.request.*;
import com.bakery.bakery_management.domain.dto.response.ExportResponse;
import com.bakery.bakery_management.domain.dto.response.ImportResponse;
import com.bakery.bakery_management.domain.dto.response.InventoryResponse;
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
import org.springframework.data.domain.Page;
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

import static com.bakery.bakery_management.domain.enums.ReferenceType.EXPORT_TO_KITCHEN;
import static com.bakery.bakery_management.domain.enums.ReferenceType.RETURN_TO_STORAGE;

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
                    .orElseGet(() -> createNewInventory(item, request.getWarehouseType(), finalExpiry));
            inv.setQuantity(inv.getQuantity().add(item.getQuantity()));
            inventoryRepository.save(inv);

            // 3. Save Transaction
            saveTx(request.getReferenceId(), item.getProductCode(), item.getUnitCode(), item.getQuantity(),
                    finalExpiry, request.getWarehouseType(), TransactionType.IMPORT, request.getReferenceType());
        }

        return buildImportResponse(request);
    }

    // --- XUẤT KHO (FEFO) ---
    @Transactional
    public ExportResponse processExport(ExportRequest request) {
        List<StockTransaction> savedTransactions = new ArrayList<>();

        for (ExportItemRequest item : request.getItems()) {
            Product product = productRepository.findByCode(item.getProductCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "SP không tồn tại"));


            // Quản lý xuất hàng.
            if (TransactionType.EXPORT.equals(request.getTransactionType())) {
                switch (request.getReferenceType()) {
                    case EXPORT_TO_KITCHEN -> {
                        // Trừ kho Main
                        Map<LocalDateTime, BigDecimal> deductedBatches = deductStockFEFO(
                                WarehouseType.MAIN_STORAGE,
                                item.getProductCode(),
                                item.getQuantity(),
                                EXPORT_TO_KITCHEN
                        );

                        // Cộng vào kho Bếp
                        deductedBatches.forEach((expiryDate, qty) -> {
                            addStock(WarehouseType.KITCHEN,
                                    item.getProductCode(),
                                    item.getUnitCode(),
                                    request.getReferenceId(),
                                    qty,
                                    expiryDate);

                            saveTx(request.getReferenceId(), item.getProductCode(), item.getUnitCode(), item.getQuantity(),
                                    expiryDate, WarehouseType.MAIN_STORAGE, TransactionType.EXPORT, request.getReferenceType());
                        });

                    }
                    case EXPORT_TO_STORE -> {
                        Map<LocalDateTime, BigDecimal> deductedBatches = deductStockFEFO(
                                WarehouseType.KITCHEN,
                                item.getProductCode(),
                                item.getQuantity(),
                                RETURN_TO_STORAGE
                        );

                        deductedBatches.forEach((expiryDate, qty) -> {
                            addStock(WarehouseType.STORE,
                                    item.getProductCode(),
                                    item.getUnitCode(),
                                    request.getReferenceId(),
                                    qty,
                                    expiryDate);
                            saveTx(request.getReferenceId(), item.getProductCode(), item.getUnitCode(), item.getQuantity(),
                                    expiryDate, WarehouseType.STORE, TransactionType.EXPORT, request.getReferenceType());
                        });
                    }
                }
            }
            // Quản lý trả hàng.
            if (TransactionType.RETURN.equals(request.getTransactionType())) {
                switch (request.getReferenceType()) {
                    case RETURN_TO_STORAGE -> {
                        Map<LocalDateTime, BigDecimal> deductedBatches = deductStockFEFO(
                                WarehouseType.KITCHEN,
                                item.getProductCode(),
                                item.getQuantity(),
                                RETURN_TO_STORAGE
                        );

                        deductedBatches.forEach((expiryDate, qty) -> {
                            addStock(WarehouseType.MAIN_STORAGE,
                                    item.getProductCode(),
                                    item.getUnitCode(),
                                    request.getReferenceId(),
                                    qty,
                                    expiryDate);

                            saveTx(request.getReferenceId(), item.getProductCode(), item.getUnitCode(), item.getQuantity(),
                                    expiryDate, WarehouseType.KITCHEN, TransactionType.EXPORT, request.getReferenceType());
                        });
                    }
                    case RETURN_TO_SUPPLIER -> {
                        Map<LocalDateTime, BigDecimal> deductedBatches = deductStockFEFO(
                                request.getWarehouseType(),
                                item.getProductCode(),
                                item.getQuantity(),
                                request.getReferenceType()
                        );
                        deductedBatches.forEach((expiryDate, qty) -> {
                            saveTx(request.getReferenceId(), item.getProductCode(), item.getUnitCode(), item.getQuantity(),
                                    expiryDate, request.getWarehouseType(), TransactionType.RETURN, request.getReferenceType());
                        });
                    }
                }
            }

            // Quản lý huỷ sản phẩm
            if (TransactionType.DISCARD.equals(request.getTransactionType())) {
                switch (request.getReferenceType()) {
                    case DAMAGED -> {
                        Map<LocalDateTime, BigDecimal> deductedBatches = deductStockFEFO(
                                request.getWarehouseType(),
                                item.getProductCode(),
                                item.getQuantity(),
                                request.getReferenceType()
                        );
                        deductedBatches.forEach((expiryDate, qty) -> {
                            saveTx(request.getReferenceId(), item.getProductCode(), item.getUnitCode(), item.getQuantity(),
                                    expiryDate, request.getWarehouseType(), TransactionType.DISCARD, request.getReferenceType());
                        });
                    }
                }
            }
            // Sync giá bán nếu có
            if (item.getSalePrice() != null) {
                priceService.syncPrice(item.getPriceCode(), product.getCode(), item.getUnitCode(), BigDecimal.ZERO, item.getSalePrice(), true, product.getType());
            }

        }
        return buildExportResponse(request);
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


    private Map<LocalDateTime, BigDecimal> deductStockFEFO(WarehouseType type, String code, BigDecimal totalDeduct, ReferenceType referenceType) {
        // Lấy tất cả lô, ưu tiên hạn gần
        List<Inventory> inventories = inventoryRepository
                .findByWarehouseTypeAndProductCodeOrderByExpiryDateAsc(type, code);

        // Kiểm tra tổng tồn kho trước khi xử lý
        BigDecimal available = inventories.stream()
                .map(Inventory::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (available.compareTo(totalDeduct) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK, "Kho " + type + " không đủ hàng");
        }

        Map<LocalDateTime, BigDecimal> deductedDetails = new HashMap<>();
        BigDecimal remainingNeed = totalDeduct;

        for (Inventory inv : inventories) {
            if (remainingNeed.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal currentQty = inv.getQuantity();
            BigDecimal deductAmount;

            if (currentQty.compareTo(remainingNeed) >= 0) {
                // Lô này dư hoặc vừa đủ
                deductAmount = remainingNeed;
                inv.setQuantity(currentQty.subtract(remainingNeed));
                remainingNeed = BigDecimal.ZERO;
            } else {
                // Lô này thiếu, lấy hết sạch lô này
                deductAmount = currentQty;
                remainingNeed = remainingNeed.subtract(currentQty);
                inv.setQuantity(BigDecimal.ZERO);
            }

            if (deductAmount.compareTo(BigDecimal.ZERO) > 0) {
                inventoryRepository.save(inv);
                deductedDetails.put(inv.getExpiryDate(), deductAmount);
            }
            saveTx(inv.getReferenceId(), inv.getProductCode(), inv.getUnitCode(), totalDeduct, inv.getExpiryDate(), WarehouseType.KITCHEN, TransactionType.IMPORT, referenceType);
        }
        return deductedDetails;
    }

    private void addStock(WarehouseType type, String code, String unitCode, String referenceId, BigDecimal amount, LocalDateTime expiryDate) {
        Inventory inventory = inventoryRepository
                .findByWarehouseTypeAndProductCodeAndExpiryDate(type, code, expiryDate)
                .orElseGet(() -> {
                    Inventory newInv = new Inventory();
                    newInv.setWarehouseType(type);
                    newInv.setProductCode(code);
                    newInv.setUnitCode(unitCode);
                    newInv.setReferenceId(referenceId);
                    newInv.setExpiryDate(expiryDate);
                    newInv.setQuantity(BigDecimal.ZERO);
                    return newInv;
                });

        inventory.setQuantity(inventory.getQuantity().add(amount));
        inventoryRepository.save(inventory);
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

    private void saveTx(String refId, String pCode, String unitCode, BigDecimal qty, LocalDateTime exp, WarehouseType wType, TransactionType tType, ReferenceType rType) {
        StockTransaction tx = new StockTransaction();
        tx.setReferenceId(refId);
        tx.setProductCode(pCode);
        tx.setUnitCode(unitCode);
        tx.setQuantity(qty);
        tx.setExpiryDate(exp);
        tx.setWarehouseType(wType);
        tx.setTransactionType(tType);
        tx.setReferenceType(rType);
        transactionRepository.save(tx);
    }

    private Inventory createNewInventory(ImportItemRequest item, WarehouseType warehouseType, LocalDateTime expiryDate) {
        Inventory inv = new Inventory();
        inv.setProductCode(item.getProductCode());
        inv.setWarehouseType(warehouseType);
        inv.setUnitCode(item.getUnitCode());
        inv.setExpiryDate(expiryDate);
        inv.setReferenceId(item.getReferenceId());
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
        Page<Inventory> entities = inventoryRepository.findByWarehouseType(WarehouseType.valueOf(warehoueType), pageable);

        List<InventoryResponse> responseList = entities.getContent().stream()
                .map(getMapper()::toResponse)
                .collect(Collectors.toList());

        MappingUtils.mapReference(
                entities.getContent(),
                responseList,
                Inventory::getUnitCode,
                unitService::getMapByCodes,
                InventoryResponse::setUnit
        );

        MappingUtils.mapReference(
                entities.getContent(),
                responseList,
                Inventory::getProductCode,
                productService::getMapByCodes,
                InventoryResponse::setProduct
        );
        return PageResult.ofPage(new PageImpl<>(responseList, pageable, entities.getTotalElements()));
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
}