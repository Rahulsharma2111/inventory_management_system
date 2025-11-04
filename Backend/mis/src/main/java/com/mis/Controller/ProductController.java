package com.mis.Controller;

import com.mis.ApiResponse.CustomResponse;
import com.mis.DTO.Request.ProductRequest;
import com.mis.DTO.Response.ProductResponse;
import com.mis.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:5173/")
public class ProductController {

private final ProductService productService;

    @PostMapping("/add")
    private ResponseEntity<?> addNewProduct(@Valid @RequestBody ProductRequest productRequest) {
        try {
            ProductResponse productResponse = productService.addNewProduct(productRequest);
            return CustomResponse.created("Product save successfully",productResponse);
        } catch (Exception e) {
            return CustomResponse.badRequest(e.getMessage());
        }
    }

    @GetMapping("/{organisationId}")
    private ResponseEntity<?> getAllProduct(@PathVariable("organisationId") Long organisationId){
        try {

            List<ProductResponse> productResponse = productService.getAllProduct(organisationId);
            return CustomResponse.ok("Data fetch successfully",productResponse);
        } catch (Exception e) {
            return CustomResponse.badRequest("Fail to fetch data");
        }
    }


    @DeleteMapping("/{productId}")
    private ResponseEntity<?> deleteProduct(@PathVariable("productId") Long productId){
        try {
            int isDeleted=productService.deleteProductById(productId);
                if(isDeleted>0){
                    return CustomResponse.ok("product deleted successfully");
                }
            return CustomResponse.badRequest("Product Deleted Fail");
        } catch (Exception e) {
            return CustomResponse.badRequest(e.getMessage());
        }
    }

    @PutMapping("/{productId}")
    private ResponseEntity<?> updateProduct(@PathVariable("productId") Long productId,@Valid @RequestBody ProductRequest productRequest) {
        try {
            ProductResponse productResponse = productService.updateProduct(productId,productRequest);
            return CustomResponse.created("Product save successfully",productResponse);
        } catch (Exception e) {
            return CustomResponse.badRequest(e.getMessage());
        }
    }
}
