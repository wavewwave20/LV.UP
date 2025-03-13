package com.alltogether.lvupbackend.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponseDto<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int size;
    private boolean first;
    private boolean last;

    public PageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.pageNumber = page.getNumber();
        this.size = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}