package com.bakery.bakery_management.service;


import com.bakery.bakery_management.base.AdminOperationService;
import com.bakery.bakery_management.domain.PageResult;
import com.bakery.bakery_management.domain.dto.ReferenceResponse;
import com.bakery.bakery_management.domain.dto.Request.*;
import com.bakery.bakery_management.domain.dto.Response.ExportResponse;
import com.bakery.bakery_management.domain.dto.Response.ImportResponse;
import com.bakery.bakery_management.domain.dto.Response.InventoryResponse;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
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


    // --- NHẬP KHO ---
    @Transactional
    public ImportResponse processImport(ImportRequest request) {
        for (ImportItemRequest item : request.getItems()) {
            Product product = productRepository.findByCode(item.getProductCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "SP không tồn tại"));

            LocalDateTime finalExpiry = calculateExpiry(product, item);

            // 1. Sync Price
            priceService.syncPrice(product.getCode(), item.getUnitCode(), item.getCostPrice(), BigDecimal.ZERO, product.getType());

            // 2. Update Inventory
            Inventory inv = inventoryRepository.findByUniqueStock(product.getCode(), request.getWarehouseType(), finalExpiry)
                    .orElseGet(() -> createNewInventory(item, request.getWarehouseType(), finalExpiry));
            inv.setQuantity(inv.getQuantity().add(item.getQuantity()));
            inventoryRepository.save(inv);

            // 3. Save Transaction
            saveTx(request.getReferenceId(), item.getProductCode(), item.getQuantity(),
                    item.getLotNumber(), finalExpiry, request.getWarehouseType(), TransactionType.IMPORT, request.getReferenceType());
        }

        return buildImportResponse(request);
    }

    // --- XUẤT KHO (FEFO) ---
    @Transactional
    public ExportResponse processExport(ExportRequest request) {
        for (ExportItemRequest item : request.getItems()) {
            Product product = productRepository.findByCode(item.getProductCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "SP không tồn tại"));

            // Sync giá bán nếu có
            if (item.getSalePrice() != null) {
                priceService.syncPrice(product.getCode(), item.getUnitCode(), BigDecimal.ZERO, item.getSalePrice(), product.getType());
            }

            // Logic FEFO trừ kho
            BigDecimal remaining = item.getQuantity();
            List<Inventory> stocks = inventoryRepository.findAllForExport(product.getCode(), request.getWarehouseType());

            for (Inventory stock : stocks) {
                if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
                BigDecimal take = stock.getQuantity().min(remaining);

                stock.setQuantity(stock.getQuantity().subtract(take));
                remaining = remaining.subtract(take);

                saveTx(request.getReferenceId(), item.getProductCode(), take.negate(),
                        stock.getLotNumber(), stock.getExpiryDate(), request.getWarehouseType(), TransactionType.EXPORT, request.getReferenceType());
                inventoryRepository.save(stock);
            }

            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK, "Thiếu hàng cho mã: " + product.getCode());
            }
        }
        return buildExportResponse(request);
    }

    private LocalDateTime calculateExpiry(Product p, ImportItemRequest item) {
        ExpiryInputType type = item.getExpiryType() != null ? item.getExpiryType() : p.getExpiryType();
        if (type == ExpiryInputType.NONE || type == null) return null;
        return switch (type) {
            case TODAY -> LocalDateTime.now().with(LocalTime.MAX);
            case DATE ->
                    item.getManualExpiryDate() != null ? item.getManualExpiryDate() : p.getFixedExpiryDate().atTime(LocalTime.MAX);
            case NUMBER ->
                    LocalDateTime.now().plusDays(item.getManualExpiryDays() != null ? item.getManualExpiryDays() : p.getDefaultExpiryDays());
            default -> null;
        };
    }

    private void saveTx(String refId, String pCode, BigDecimal qty, String lot, LocalDateTime exp, WarehouseType wType, TransactionType tType, ReferenceType rType) {
        StockTransaction tx = new StockTransaction();
        tx.setReferenceId(refId);
        tx.setProductCode(pCode);
        tx.setQuantity(qty);
        tx.setLotNumber(lot);
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
        inv.setLotNumber(item.getLotNumber()); // Lưu số lô của lần nhập đầu tiên để tham chiếu
        inv.setQuantity(BigDecimal.ZERO); // Sẽ được cộng dồn ở hàm gọi
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
        Page<Inventory> entities = inventoryRepository.findAll(pageable);

        List<InventoryResponse> responseList = entities.getContent().stream()
                .filter(i -> i.getWarehouseType() == WarehouseType.valueOf(warehoueType))
                .map(getMapper()::toResponse)
                .collect(Collectors.toList());

        List<String> unitCodes = entities.stream()
                .map(Inventory::getUnitCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<String, ReferenceResponse> unitMap = unitService.getMapByCodes(unitCodes);
        for (int i = 0; i < entities.getContent().size(); i++) {
            Inventory entity = entities.getContent().get(i);
            InventoryResponse res = responseList.get(i);

            if (entity.getUnitCode() != null && unitMap != null) {
                res.setUnit(unitMap.get(entity.getUnitCode()));
            }
        }

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
        List<String> unitCodes = entities.stream()
                .map(Inventory::getUnitCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<String, ReferenceResponse> unitMap = unitService.getMapByCodes(unitCodes);
        for (int i = 0; i < entities.size(); i++) {
            Inventory entity = entities.get(i);
            InventoryResponse res = responses.get(i);

            if (entity.getUnitCode() != null && unitMap != null) {
                res.setUnit(unitMap.get(entity.getUnitCode()));
            }
        }
    }
}