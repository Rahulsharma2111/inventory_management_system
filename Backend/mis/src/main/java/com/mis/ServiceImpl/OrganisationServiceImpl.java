package com.mis.ServiceImpl;

import com.mis.Configuration.JwtUtil;
import com.mis.DTO.Request.LoginRequest;
import com.mis.DTO.Request.OrganisationRequest;
import com.mis.DTO.Response.OrganisationResponse;
import com.mis.Entity.Media;
import com.mis.Entity.Organisation;
import com.mis.Exception.InvalidCredentialsException;
import com.mis.Exception.ResourceAlreadyExist;
import com.mis.Exception.ResourceNotFoundException;
import com.mis.Mapper.OrganisationMapper;
import com.mis.Repository.MediaRepository;
import com.mis.Repository.OrganisationRepository;
import com.mis.Service.OrganisationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {

    private final OrganisationMapper organisationMapper;
    private final OrganisationRepository organisationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MediaRepository mediaRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public OrganisationResponse registerNewOrganisation(OrganisationRequest organisationRequest) {

        validateUniqueFields(organisationRequest);
        try{
        String password= passwordEncoder.encode(organisationRequest.getPassword());
        organisationRequest.setPassword(password);
        Organisation organisation = organisationMapper.mapOrganisationRequestIntoOrganisation(organisationRequest);

        Organisation savedOrganisation = organisationRepository.save(organisation);

        return organisationMapper.mapOrganisationIntoOrganisationResponse(savedOrganisation);
        } catch (RuntimeException e) {
            throw new RuntimeException("Register fail. Please try again");
        }
    }

    @Override
    @Transactional
    public OrganisationResponse loginToOrganisation(LoginRequest loginRequest) {

        Organisation findByEmailOrganisation=organisationRepository.findByEmail(loginRequest.getEmail());

        if (findByEmailOrganisation==null){
//            throw new RuntimeException("Please enter your register mail");
            throw new ResourceNotFoundException("Please enter your registered email");
        }else {

//            String userPassword= passwordEncoder.encode(loginRequest.getPassword());
            if (!passwordEncoder.matches(loginRequest.getPassword(), findByEmailOrganisation.getPassword())){
                throw new RuntimeException("Wrong Credentials");
            } else {
                OrganisationResponse organisationResponse=organisationMapper.mapOrganisationIntoOrganisationResponse(findByEmailOrganisation);
                Authentication authentication= authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        )
                );
                try {
                Organisation organisation= (Organisation) authentication.getPrincipal();
                String token= jwtUtil.generateToken(organisation);
                organisationRepository.updatePasswordAndToken(organisation.getId(), token);
                 organisationResponse.setJwtToken(token);
                return organisationResponse;
                } catch (BadCredentialsException e) {
                    throw new InvalidCredentialsException("Wrong Credentials");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    throw new InvalidCredentialsException("Wrong Credentials");

                }
            }
        }
    }

    private void validateUniqueFields(OrganisationRequest request) {

        if (organisationRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new ResourceAlreadyExist("This registration number already exists");
        }

        if (organisationRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExist("This email already exists");
        }

        if (organisationRepository.existsByPhone(request.getPhone())) {
            throw new ResourceAlreadyExist("This phone number already exists");
        }

        if (request.getGstNumber() != null && !request.getGstNumber().isBlank()
                && organisationRepository.existsByGstNumber(request.getGstNumber())) {
            throw new ResourceAlreadyExist("This GST number already exists");
        }
    }

    @Override
    @Transactional
    public String saveFile(Long organisationId, MultipartFile file) throws IOException {
        String mediaType = "logo_image"; // specify the type
        Media existingMedia = mediaRepository.findByOrganisationIdAndMediaType(organisationId, mediaType);

        Media saveMedia=null;
        if (existingMedia != null) {
            existingMedia.setOrganisationId(organisationId);
            existingMedia.setFileName(file.getOriginalFilename());
            saveMedia=mediaRepository.save(existingMedia);
        }else {
            Media media = new Media();
            media.setOrganisationId(organisationId);
            media.setFileName(file.getOriginalFilename());
            media.setDescription("Logo Image");
            media.setMediaType("logo_image");
            saveMedia = mediaRepository.save(media);
        }

        Path uploadPath = Paths.get(uploadDir, String.valueOf(organisationId), "media",String.valueOf(saveMedia.getId()));

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (DirectoryStream<Path> files = Files.newDirectoryStream(uploadPath)) {
            for (Path existingFile : files) {
                Files.deleteIfExists(existingFile);
            }
        }

        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    @Override
    public ResponseEntity<Resource> getFile(Long organisationId) throws IOException {

        Media media = mediaRepository.findByOrganisationIdAndMediaType(organisationId, "logo_image");
        if (media==null){
            throw new ResourceNotFoundException("Media not found");
        }

        Path filePath = Paths.get(uploadDir, String.valueOf(organisationId), "media", String.valueOf(media.getId()));

        Path imagePath = filePath.resolve(media.getFileName());
        System.out.println("Path :  "+imagePath);
        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("File not found: " + media.getFileName());
        }

        Resource resource = new UrlResource(imagePath.toUri());
        System.out.println("Resource: "+ resource);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }

    @Override
    public int deleteLogoImage(Long organisationId) throws IOException {
        Media media = mediaRepository.findByOrganisationIdAndMediaType(organisationId, "logo_image");
        Path uploadPath = Paths.get(uploadDir, String.valueOf(organisationId), "media", String.valueOf(media.getId()));
        Path filePath = uploadPath.resolve(media.getFileName());
        boolean deleted = Files.deleteIfExists(filePath);

        int isSoftDeletedLogoImage=mediaRepository.isSoftDeletedLogoImage(media.getId());
        return isSoftDeletedLogoImage;
    }

}
