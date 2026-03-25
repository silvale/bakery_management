package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.base.JpaEntityAuditable;
import com.bakery.bakery_management.domain.enums.ExpiryInputType;
import com.bakery.bakery_management.domain.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DynamicUpdate
@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends JpaEntityAuditable<UUID> {

    private String code; // Business Key
    private String name;
    private String unitCode;

    @Enumerated(EnumType.STRING)
    private ProductType type; // RAW_MATERIAL, SEMI_FINISHED, FINISHED_PRODUCT

    @Enumerated(EnumType.STRING)
    private ExpiryInputType expiryType; // NONE, NUMBER, DATE, TODAY

    private Integer defaultExpiryDays; // Dùng cho loại NUMBER
    private LocalDate fixedExpiryDate; // Dùng cho loại DATE (Hạn cố định)

    @OneToMany(mappedBy = "productCode", fetch = FetchType.LAZY)
    @OrderBy("appliedDate DESC")
    private List<ProductPrice> prices = new ArrayList<>();
}
