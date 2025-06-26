package com.ecommerce.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private List<ProductDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private boolean lastPage;
    private Long totalElements;
}
