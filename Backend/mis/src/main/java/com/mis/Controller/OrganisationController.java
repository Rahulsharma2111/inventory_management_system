package com.mis.Controller;

import com.mis.ApiResponse.CustomResponse;
import com.mis.DTO.Request.LoginRequest;
import com.mis.DTO.Request.OrganisationRequest;
import com.mis.DTO.Response.OrganisationResponse;
import com.mis.Service.OrganisationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/organisation")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService organisationService;

    @PostMapping("/register")
    private ResponseEntity<?> registerOrganisation(@Valid @RequestBody OrganisationRequest organisationRequest) {
        try {
            OrganisationResponse organisationResponse = organisationService.registerNewOrganisation(organisationRequest);
            return CustomResponse.created("Registration successfully",organisationResponse);
        } catch (Exception e) {
            return CustomResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping("/login")
    private ResponseEntity<?> loginToOrganisation(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            OrganisationResponse organisationResponse = organisationService.loginToOrganisation(loginRequest);
            return CustomResponse.ok("Login Successfully",organisationResponse);
        } catch (Exception e) {
            return CustomResponse.badRequest(e.getMessage());
        }
    }

    @PostMapping("/upload/{organisationId}")
    public ResponseEntity<?> uploadFile(@PathVariable("organisationId") Long organisationId,@RequestParam("file") MultipartFile file) {
        try {
            String path = organisationService.saveFile(organisationId,file);
//            return "File uploaded successfully: " + path;
            System.out.println("image path: "+path);

            return CustomResponse.ok("File uploaded successfully",path);
        } catch (Exception e) {
            return CustomResponse.badRequest("Fail to upload");
        }
    }

    @GetMapping("/{organisationId}")
    public ResponseEntity<Resource> getMediaFile(@PathVariable("organisationId") Long organisationId) throws IOException {

        return organisationService.getFile(organisationId);
    }

    @DeleteMapping("/{organisationId}")
    public ResponseEntity<?> deleteLogoImage(@PathVariable("organisationId") Long organisationId){
        try {

        int isDeleted=organisationService.deleteLogoImage(organisationId);
        if (isDeleted>=1){
            return CustomResponse.ok("Image Deleted Successfully");
        }else {
            return CustomResponse.badRequest("Image not deleted");
        }
        }catch (Exception e) {
            e.printStackTrace();
            return CustomResponse.badRequest("Image not deleted");
        }
    }

}