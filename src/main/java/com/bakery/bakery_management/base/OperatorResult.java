package com.bakery.bakery_management.base;

import java.util.UUID;

public record OperatorResult(
        boolean success,
        String message,
        UUID id
) {

    public static OperatorResult ok(UUID id) {
        return new OperatorResult(true, "Operation successful", id);
    }

    public static OperatorResult ok(String message, UUID id) {
        return new OperatorResult(true, message, id);
    }

    public static OperatorResult fail(String message) {
        return new OperatorResult(false, message, null);
    }
}
