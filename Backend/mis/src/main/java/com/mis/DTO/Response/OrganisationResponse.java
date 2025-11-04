package com.mis.DTO.Response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationResponse {
    private Long id;
    private String name;
    private String registrationNumber;
    private String type;

    private String email;
    private String phone;
    private String website;
    private String gstNumber;

    private String address;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private String jwtToken;
    private Date createdAt;
    private Date updatedAt;

}
