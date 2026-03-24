package com.bakery.bakery_management.service;


import com.bakery.bakery_management.domain.dto.Request.ImportItemRequest;
import com.bakery.bakery_management.domain.dto.Request.ImportRequest;
import com.bakery.bakery_management.domain.dto.Response.ImportResponse;
import com.bakery.bakery_management.domain.entity.Inventory;
import com.bakery.bakery_management.domain.entity.Product;
import com.bakery.bakery_management.domain.entity.ProductPrice;
import com.bakery.bakery_management.domain.entity.StockTransaction;
import com.bakery.bakery_management.exception.BusinessException;
import com.bakery.bakery_management.exception.ErrorCode;
import com.bakery.bakery_management.repository.InventoryRepository;
import com.bakery.bakery_management.repository.ProductPriceRepository;
import com.bakery.bakery_management.repository.ProductRepository;
import com.bakery.bakery_management.repository.StockTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository priceRepository;

    @Transactional
    public ImportResponse processImport(ImportRequest request) {
        // 1. Validate Sản phẩm
        Set<String> productCodes = request.getItems().stream()
                .map(ImportItemRequest::getProductCode)
                .collect(Collectors.toSet());

        List<Product> products = productRepository.findByCodeIn(productCodes);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getCode, p -> p));

        if (products.size() != productCodes.size()) {
            productCodes.removeAll(productMap.keySet());
            // Sử dụng BusinessException thay vì RuntimeException
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "Các mã sản phẩm không tồn tại: " + productCodes);
        }

        // 2. Xử lý từng Item
        for (ImportItemRequest item : request.getItems()) {
            Product product = productMap.get(item.getProductCode());

            // 2.1. Validate Unit
            if (!product.getUnitCode().equals(item.getUnitCode())) {
                throw new BusinessException(ErrorCode.INVALID_UNIT,
                        String.format("Sai đơn vị cho mã %s. Yêu cầu: %s", item.getProductCode(), product.getUnitCode()));
            }

            // 2.2. Lấy thông tin giá (Lấy mã giá cụ thể hoặc Default)
            ProductPrice price = findApplicablePrice(item);

            // 3. Ghi log Transaction
            saveStockTransaction(request, item, price);

            // 4. Cập nhật Tồn kho
            updateInventoryStock(request, item);
        }

        return ImportResponse.builder()
                .referenceId(request.getReferenceId())
                .importedAt(LocalDateTime.now())
                .totalItems(request.getItems().size())
                .status("SUCCESS")
                .message("Nhập kho thành công!")
                .build();
    }

    private ProductPrice findApplicablePrice(ImportItemRequest item) {
        if (StringUtils.hasText(item.getCode())) {
            return priceRepository.findByCode(item.getCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRICE_NOT_FOUND, "Mã giá " + item.getCode() + " không tồn tại"));
        }
        return priceRepository.findByProductCodeAndUnitCodeAndIsDefaultTrue(item.getProductCode(), item.getUnitCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.DEFAULT_PRICE_NOT_SET, "Sản phẩm " + item.getProductCode() + " chưa có giá mặc định"));
    }
    

    private void saveStockTransaction(ImportRequest req, ImportItemRequest item, ProductPrice price) {
        StockTransaction tx = new StockTransaction();
        tx.setProductCode(item.getProductCode());
        tx.setWarehouseType(req.getWarehouseType());
        tx.setTransactionType(req.getTransactionType());
        tx.setReferenceType(req.getReferenceType());
        tx.setQuantity(item.getQuantity());
        tx.setUnitCode(item.getUnitCode());
        tx.setLotNumber(item.getLotNumber());
        tx.setReferenceId(req.getReferenceId());
        // Note lại thông tin giá tại thời điểm nhập
        tx.setNote(String.format("PriceCode: %s | Cost: %,.0f", price.getCode(), price.getCostPrice()));
        stockTransactionRepository.save(tx);
    }

    private void updateInventoryStock(ImportRequest req, ImportItemRequest item) {
        Inventory inv = inventoryRepository.findByUniqueStock(
                item.getProductCode(),
                req.getWarehouseType(), // Sử dụng WarehouseType chính xác
                item.getLotNumber(),
                item.getExpiryDate()
        ).orElseGet(() -> {
            Inventory newInv = new Inventory();
            newInv.setProductCode(item.getProductCode());
            newInv.setWarehouseType(req.getWarehouseType());
            newInv.setLotNumber(item.getLotNumber());
            newInv.setUnitCode(item.getUnitCode());
            newInv.setExpiryDate(item.getExpiryDate());
            newInv.setQuantity(BigDecimal.ZERO);
            return newInv;
        });

        inv.setQuantity(inv.getQuantity().add(item.getQuantity()));
        inventoryRepository.save(inv);
    }
}