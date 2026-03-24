package com.bakery.bakery_management.base;

import com.bakery.bakery_management.domain.enums.StatusCode;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity<T extends Serializable> implements Serializable {

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusCode status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected Instant createdAt = Instant.now();

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 255)
    protected String createdBy;
}
