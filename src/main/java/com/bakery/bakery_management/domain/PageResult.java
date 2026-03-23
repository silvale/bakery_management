package com.bakery.bakery_management.domain;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PageResult<T>(List<T> items, PageMeta page) {

    @Builder
    public record PageMeta(int number, int size, long totalElements, int totalPages, boolean first, boolean last) {}

    public static <T> PageResult<T> ofPage(Page<T> page) {
        PageMeta pageMeta = new PageMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast());
        return new PageResult<>(page.getContent(), pageMeta);
    }

    // Create PageResult from a full list with pagination applied.
    public static <T> PageResult<T> ofList(List<T> allItems) {
        int size = allItems.size();
        PageMeta pageMeta = new PageMeta(0, size, size, 1, true, true);
        return new PageResult<>(allItems, pageMeta);
    }

    // Create PageResult from already-paginated items.
    // public static <T> PageResult<T> ofPage(List<T> items, int page, int size, long totalElements)

}