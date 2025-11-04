package com.mis.Mapper;

import com.mis.DTO.Request.ProductRequest;
import com.mis.DTO.Response.ProductResponse;
import com.mis.Entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {


   Product mapProductRequestIntoProduct(ProductRequest productRequest);

    ProductResponse mapProductIntoProductResponse(Product product1);

    List<ProductResponse> mapProductIntoProductResponse(List<Product> productList);

}
