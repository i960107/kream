package com.example.demo.src.style.model.dto;

import com.example.demo.src.product.model.dto.GetImageRes;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetStyleListRes {

    private List<GetStyleRes> styleList;
    private int totalPages;
    private Long totalElements;
    private int pageNumber;
    private boolean first;
    private boolean last;

    public GetStyleListRes(List<GetStyleRes> styleList, int totalPages, Long totalElements, int pageNumber, boolean first, boolean last) {
        this.styleList = styleList;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageNumber = pageNumber;
        this.first = first;
        this.last = last;
    }
}
