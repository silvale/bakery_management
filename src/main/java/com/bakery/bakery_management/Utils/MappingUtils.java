package com.bakery.bakery_management.Utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MappingUtils {
    public static <E, R, K, V> void mapReference(
            List<E> entities,
            List<R> responses,
            Function<E, K> keyExtractor,
            Function<List<K>, Map<K, V>> mapFetcher,
            BiConsumer<R, V> setter
    ) {
        if (entities == null || responses == null || entities.size() != responses.size()) {
            return;
        }

        List<K> keys = entities.stream()
                .map(keyExtractor)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<K, V> map = mapFetcher.apply(keys);
        if (map == null || map.isEmpty()) return;

        for (int i = 0; i < entities.size(); i++) {
            K key = keyExtractor.apply(entities.get(i));
            if (key != null) {
                V value = map.get(key);
                if (value != null) {
                    setter.accept(responses.get(i), value);
                }
            }
        }
    }
}
