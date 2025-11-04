package com.mis.DTO.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationRequest {

    @NotBlank(message = "Organisation name is required")
    @Size(min = 2, max = 100, message = "Organisation name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Registration number is required")
//    @Pattern(regexp = "^[0-9A-Z]$", message = "Registration number must be alphanumeric characters")
    private String registrationNumber;

    @NotBlank(message = "Type is required (e.g., Pvt Ltd, NGO, LLP)")
    private String type;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @Size(max = 100, message = "Website URL cannot exceed 100 characters")
    private String website;

//    @Pattern(regexp = "^[0-9A-Z]$", message = "GST number must be alphanumeric characters")
    @NotBlank(message = "GST number is required")
    private String gstNumber;

    @NotBlank(message = "Address is required")
    @Pattern(regexp = "^[A-Za-z0-9 ,\\-]+$", message = "Special letter not allow")
    private String address;

    @NotBlank(message = "City is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Special letter not allow")
    private String city;

    @NotBlank(message = "State is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Special letter not allow")
    private String state;

    @NotBlank(message = "Country is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Special letter not allow")
    private String country;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[0-9]{8}$", message = "Pincode must be exactly 6 digits")
    private String pincode;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;


//    private String name;
//    private String registrationNumber;
//    private String type;
//    private String email;
//    private String phone;
//    private String website;
//    private String gstNumber;
//    private String address;
//    private String city;
//    private String state;
//    private String country;
//    private String pincode;
//    private String password;
}
