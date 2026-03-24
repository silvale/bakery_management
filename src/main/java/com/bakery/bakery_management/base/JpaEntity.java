/*
 * Copyright© OneEmpower Pte Ltd. All rights reserved.
 *
 * This work contains trade secrets and confidential material of
 * OneEmpower Pte Ltd, and its unauthorised dissemination, use or
 * disclosure in whole or in part  without explicit written
 * permission of OneEmpower Pte Ltd is strictly prohibited.
 */
package com.bakery.bakery_management.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
public abstract class JpaEntity<T extends Serializable> extends BaseEntity<T> {

    @Id
    @GenerateBakeryId
    @Column(nullable = false, updatable = false)
    private T id;

    public boolean isNew() {
        return id == null;
    }
}