package com.mis.ServiceImpl;

import com.mis.DTO.Request.ProductRequest;
import com.mis.DTO.Response.ProductResponse;
import com.mis.Entity.Product;
import com.mis.Exception.NegativeValueHandleException;
import com.mis.Exception.ResourceAlreadyExist;
import com.mis.Exception.ResourceNotFoundException;
import com.mis.Mapper.ProductMapper;
import com.mis.Repository.ProductRepository;
import com.mis.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    @Override
    public ProductResponse addNewProduct(ProductRequest productRequest) {

    if (productRequest.getPrice() < 0) {
        throw new NegativeValueHandleException("Price cannot be negative.");
    }

    if (productRequest.getStockQuantity() < 0) {
        throw new NegativeValueHandleException("Stock quantity cannot be negative.");
    }

    if (productRequest.getReorderLevel() < 0) {
        throw new NegativeValueHandleException("Reorder level cannot be negative.");
    }

    Product existingProduct = productRepository.findByNameAndOrganisationId(productRequest.getName(),productRequest.getOrganisationId());
    if (existingProduct != null) {
        throw new ResourceAlreadyExist("Product already exists");
    }
        try {
    Product product = productMapper.mapProductRequestIntoProduct(productRequest);
    Product product1 = productRepository.save(product);

    return productMapper.mapProductIntoProductResponse(product1);
} catch (Exception e) {
    throw new RuntimeException("Fail to save Product");
}
    }

    @Override
    public List<ProductResponse> getAllProduct(Long organisationId) {

        List<Product> productList=productRepository.findAllByOrganisationIdAndDeletedAtIsNull(organisationId);
        return productMapper.mapProductIntoProductResponse(productList);

    }

    @Override
    public int deleteProductById(Long productId) {
        Date currentDate=new Date();
        int isDeleted=productRepository.deleteProductByProductId(productId,currentDate);
        return isDeleted;
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product existingProduct=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found"));

        Product checkProductIsExisting = productRepository.findByNameAndOrganisationId(request.getName(),request.getOrganisationId());
        if (checkProductIsExisting != null && !checkProductIsExisting.getId().equals(existingProduct.getId())) {
            throw new ResourceAlreadyExist("Product already exists");
        }
        existingProduct.setName(request.getName());
        existingProduct.setCategory(request.getCategory());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStockQuantity(request.getStockQuantity());
        existingProduct.setReorderLevel(request.getReorderLevel());
        existingProduct.setOrganisationId(request.getOrganisationId());
try {
    Product updatedProduct = productRepository.save(existingProduct);

        return ProductResponse.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .category(updatedProduct.getCategory())
                .description(updatedProduct.getDescription())
                .price(updatedProduct.getPrice())
                .stockQuantity(updatedProduct.getStockQuantity())
                .reorderLevel(updatedProduct.getReorderLevel())
                .organisationId(updatedProduct.getOrganisationId())
                .build();
} catch (Exception e) {
    throw new RuntimeException("Update fail");
}
    }

}
