package com.mis.Service;

import com.mis.DTO.Request.LoginRequest;
import com.mis.DTO.Request.OrganisationRequest;
import com.mis.DTO.Response.OrganisationResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OrganisationService {
    OrganisationResponse registerNewOrganisation(OrganisationRequest organisationRequest);

    OrganisationResponse loginToOrganisation(LoginRequest loginRequest);

    String saveFile(Long organisationId,MultipartFile file) throws IOException;

    ResponseEntity<Resource> getFile(Long organisationId)  throws IOException;

    int deleteLogoImage(Long organisationId) throws IOException;

}
