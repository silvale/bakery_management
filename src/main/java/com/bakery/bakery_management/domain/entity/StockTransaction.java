package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.ReferenceType;
import com.bakery.bakery_management.domain.enums.TransactionType;
import com.bakery.bakery_management.domain.enums.WarehouseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_transactions")
@Getter @Setter
public class StockTransaction extends JpaEntityAuditable<UUID> {

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "warehouse_type")
    private WarehouseType warehouseType; // MAIN_STORAGE hoặc KITCHEN

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType; // IMPORT, EXPORT, ADJUST

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType; // PURCHASE_IMPORT, PRODUCTION_EXPORT...

    @Column(precision = 19, scale = 3)
    private BigDecimal quantity; // Số lượng biến động (Luôn lưu số dương, logic cộng/trừ dựa vào TransactionType)

    @Column(name = "unit_code")
    private String unitCode;

    @Column(name = "lot_number")
    private String lotNumber; // ID của phiếu nhập gốc

    @Column(name = "reference_id")
    private String referenceId; // ID của phiếu cụ thể (Phiếu nhập, Phiếu xuất...)

    private String note; // Ghi chú chi tiết nếu cần
}
