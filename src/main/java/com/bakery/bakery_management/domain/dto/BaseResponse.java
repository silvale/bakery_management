package com.bakery.bakery_management.domain.dto;


import com.bakery.bakery_management.domain.enums.StatusCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Generated;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public abstract class BaseResponse implements Serializable {

    @JsonFormat(
            shape = JsonFormat.Shape.STRING
    )
    protected UUID id;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )

    protected String createdBy;
    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    protected Instant createdAt;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    protected String updatedBy;

    @JsonProperty(
            access = JsonProperty.Access.READ_ONLY
    )
    protected Instant updatedAt;

    protected StatusCode status;

    @Generated
    public UUID getId() {
        return this.id;
    }

    @Generated
    public String getCreatedBy() {
        return this.createdBy;
    }

    @Generated
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    @Generated
    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public void setId(final UUID id) {
        this.id = id;
    }

    @Generated
    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    @Generated
    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Generated
    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public StatusCode getStatus() {
        return status;
    }

    @Generated
    public void setStatus(StatusCode status) {
        this.status = status;
    }
}
