//package com.mis.ServiceImpl;
//
//import com.mis.Entity.Media;
//import com.mis.Exception.ResourceNotFoundException;
//import com.mis.Repository.MediaRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.core.io.Resource;
//import org.springframework.http.ResponseEntity;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class NewFileServiceTest {
//
//    @Mock
//    private MediaRepository mediaRepository;
//
//    @InjectMocks
//    private NewFileService newFileService;
//
//    private Media testMedia;
//    private final String uploadDir = "uploads";
//
//    @BeforeEach
//    void setUp() {
//        testMedia = Media.builder()
//                .id(1L)
//                .fileName("test.jpg")
//                .organisationId(1L)
//                .mediaType("image/jpeg")
//                .description("Test image")
//                .build();
//    }
//
//    @Test
//    @DisplayName("get file")
//    void getFile_WhenFileExists_ShouldReturnFile() throws Exception {
//
//        Long organisationId = 1L;
//        Long fileId = 1L;
//
//  when(mediaRepository.findByOrganisationIdAndId(organisationId, fileId))
//                .thenReturn(testMedia);
//
//        Path testFilePath = Paths.get(uploadDir, String.valueOf(organisationId), "media", "test.jpg");
//
//        Files.deleteIfExists(testFilePath);
//        Files.createDirectories(testFilePath.getParent());
//        Files.createFile(testFilePath);
//
//        try {
//            ResponseEntity<Resource> response = newFileService.getFile(organisationId, fileId);
//
//            assertNotNull(response);
//            assertEquals(200, response.getStatusCodeValue());
//            assertNotNull(response.getBody());
//
//            verify(mediaRepository, times(1)).findByOrganisationIdAndId(organisationId, fileId);
//        } finally {
//            // Clean up
//            Files.deleteIfExists(testFilePath);
//        }
//    }
//
//    @Test
//    @DisplayName("Should throw exception when file not found in database")
//    void getFile_WhenFileNotFoundInDB_ShouldThrowException() {
//
//        Long organisationId = 1L;
//        Long fileId = 999L;
//
//        when(mediaRepository.findByOrganisationIdAndId(organisationId, fileId))
//                .thenReturn(null);
//
//        assertThrows(ResourceNotFoundException.class, () -> {
//            newFileService.getFile(organisationId, fileId);
//        });
//
//        verify(mediaRepository, times(1)).findByOrganisationIdAndId(organisationId, fileId);
//    }
//}


package com.mis.ServiceImpl;

import com.mis.Entity.Media;
import com.mis.Repository.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewFileServiceTest {

    @Mock
    private MediaRepository mediaRepository;

    @InjectMocks
    private NewFileService newFileService;

    private Media media1, media2, logoMedia;

    @BeforeEach
    void setUp() {

        media1 = Media.builder()
                .id(1L)
                .fileName("document.pdf")
                .organisationId(1L)
                .mediaType("application/pdf")
                .description("Test document")
                .build();

        media2 = Media.builder()
                .id(2L)
                .fileName("image.jpg")
                .organisationId(1L)
                .mediaType("image/jpeg")
                .description("Test image")
                .build();

        logoMedia = Media.builder()
                .id(3L)
                .fileName("logo.png")
                .organisationId(1L)
                .mediaType("logo_image")
                .description("Company logo")
                .build();
    }

    @Test
    @DisplayName("Should return all files except logos when files exist")
    void getAllFiles_WhenFilesExist_ShouldReturnFilesExcludingLogos() {
        Long orgId = 1L;

        List<Media> expectedFiles = Arrays.asList(media1, media2);
        when(mediaRepository.findByOrganisationIdAndMediaTypeNot(orgId, "logo_image"))
                .thenReturn(expectedFiles);

        List<Media> result = newFileService.getAllFiles(orgId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertFalse(result.contains(logoMedia));

        verify(mediaRepository, times(1))
                .findByOrganisationIdAndMediaTypeNot(orgId, "logo_image");
    }

    @Test
    @DisplayName("Should return empty list when no files exist")
    void getAllFiles_WhenNoFiles_ShouldReturnEmptyList() {

        Long orgId = 2L;

        when(mediaRepository.findByOrganisationIdAndMediaTypeNot(orgId, "logo_image"))
                .thenReturn(Collections.emptyList());

        List<Media> result = newFileService.getAllFiles(orgId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(mediaRepository, times(1))
                .findByOrganisationIdAndMediaTypeNot(orgId, "logo_image");
    }

    @Test
    @DisplayName("Should return only non-logo files when mixed files exist")
    void getAllFiles_WhenMixedFilesExist_ShouldReturnOnlyNonLogoFiles() {

        Long orgId = 1L;

        List<Media> mixedFiles = Arrays.asList(media1, media2, logoMedia);
        when(mediaRepository.findByOrganisationIdAndMediaTypeNot(orgId, "logo_image"))
                .thenReturn(Arrays.asList(media1, media2));

        List<Media> result = newFileService.getAllFiles(orgId);

        assertNotNull(result);
        assertEquals(2, result.size());

        boolean hasLogo = result.stream()
                .anyMatch(media -> "logo_image".equals(media.getMediaType()));
        assertFalse(hasLogo);

        verify(mediaRepository, times(1))
                .findByOrganisationIdAndMediaTypeNot(orgId, "logo_image");
    }

    @Test
    @DisplayName("Should handle different organization IDs correctly")
    void getAllFiles_WithDifferentOrgIds_ShouldReturnCorrectFiles() {

        Long orgId1 = 1L;
        Long orgId2 = 2L;

        List<Media> org1Files = Arrays.asList(media1);
        List<Media> org2Files = Arrays.asList(media2);

        when(mediaRepository.findByOrganisationIdAndMediaTypeNot(orgId1, "logo_image"))
                .thenReturn(org1Files);
        when(mediaRepository.findByOrganisationIdAndMediaTypeNot(orgId2, "logo_image"))
                .thenReturn(org2Files);

        List<Media> result1 = newFileService.getAllFiles(orgId1);
        List<Media> result2 = newFileService.getAllFiles(orgId2);

        assertEquals(1, result1.size());
        assertEquals(1, result2.size());
        assertEquals("document.pdf", result1.get(0).getFileName());
        assertEquals("image.jpg", result2.get(0).getFileName());

        verify(mediaRepository, times(1))
                .findByOrganisationIdAndMediaTypeNot(orgId1, "logo_image");
        verify(mediaRepository, times(1))
                .findByOrganisationIdAndMediaTypeNot(orgId2, "logo_image");
    }
}