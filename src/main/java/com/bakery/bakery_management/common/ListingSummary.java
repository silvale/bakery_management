package common;

import java.util.HashMap;
import java.util.Map;

public final class ListingSummary {

    private final Map<String, Object> values = new HashMap<>();

    public ListingSummary put(String key, Object value) {
        values.put(key, value);
        return this;
    }

    public Object get(String key) {
        return values.get(key);
    }

    public Long getLong(String key) {
        Object value = values.get(key);
        return value instanceof Number ? ((Number) value).longValue() : null;
    }

    public static ListingSummary empty() {
        return new ListingSummary();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
