/*
 * Copyright© OneEmpower Pte Ltd. All rights reserved.
 *
 * This work contains trade secrets and confidential material of
 * OneEmpower Pte Ltd, and its unauthorised dissemination, use or
 * disclosure in whole or in part  without explicit written
 * permission of OneEmpower Pte Ltd is strictly prohibited.
 */
package com.bakery.bakery_management.domain.entity;

import com.bakery.bakery_management.domain.enums.StatusCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

/** Created by tuanngo on Sun, 2025-08-24 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class JpaEntity<T extends Serializable> extends BaseEntity<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

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
