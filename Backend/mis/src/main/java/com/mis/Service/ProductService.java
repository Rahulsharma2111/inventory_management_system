package com.mis.Service;

import com.mis.DTO.Request.ProductRequest;
import com.mis.DTO.Response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addNewProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProduct(Long organisationId);

    int deleteProductById(Long productId);

    ProductResponse updateProduct(Long productId, ProductRequest productRequest);

}
