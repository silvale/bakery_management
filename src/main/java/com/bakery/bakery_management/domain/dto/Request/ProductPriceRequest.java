package com.bakery.bakery_management.domain.dto.Request;

import com.bakery.bakery_management.domain.enums.StatusCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceRequest {

    @NotBlank(message = "Mã giá không được để trống")
    private String code; // Ví dụ: P-BOTMI-2026-01

    @NotBlank(message = "Mã sản phẩm không được để trống")
    private String productCode;

    @NotBlank(message = "Đơn vị tính không được để trống")
    private String unitCode;

    @NotNull(message = "Giá vốn không được để trống")
    @PositiveOrZero
    private BigDecimal costPrice; // Giá nhập

    @NotNull(message = "Giá bán không được để trống")
    @PositiveOrZero
    private BigDecimal salePrice; // Giá bán lẻ

    private boolean isDefault; // Đánh dấu là giá mặc định

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appliedDate; // Ngày áp dụng giá này

    private StatusCode status = StatusCode.ACTIVE;
}
