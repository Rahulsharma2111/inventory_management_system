package com.mis.DTO.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {


    @NotBlank(message = "Customer name is required")
    @Size(min = 1, max = 100, message = "Customer name must be required")
    private String name;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

//    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Login again, Something went wrong")
    private Long productId;

    @NotNull(message = "Login again, Something went wrong")
    private Long organisationId;

    @NotNull(message = "Total amount is required")
    private Double totalAmount;

//    private String name;
//    private String email;
//    private String phone;
//    private Double price;
//    private Integer quantity;
//    private Long productId;
//    private Long organisationId;

}
