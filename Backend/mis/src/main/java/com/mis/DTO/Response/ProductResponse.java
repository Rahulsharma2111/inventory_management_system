package com.mis.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String category;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private Long organisationId;
    private Date createdAt;
}
