package com.mis.Controller;

import com.mis.Entity.Media;
import com.mis.ServiceImpl.NewFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Mock
    private NewFileService newFileService;

    @InjectMocks
    private FilesController fileController;

    private Media media1;
    private Media media2;
    List<Media> expectedFiles;

    @BeforeEach
    void setMediaData(){

        media1 = Media.builder()
                .id(1L)
                .fileName("document.pdf")
                .organisationId(1L)
                .mediaType("application/pdf")
                .description("Important document")
                .build();

        media2 = Media.builder()
                .id(2L)
                .fileName("image.jpg")
                .organisationId(1L)
                .mediaType("image/jpeg")
                .description("Company logo")
                .build();

        expectedFiles = Arrays.asList(media1, media2);
    }

    @Test
    @DisplayName("get files in list")
    void getFiles_WhenFilesExist_ShouldReturnListOfFiles() {
        Long orgId = 1L;

        when(newFileService.getAllFiles(orgId)).thenReturn(expectedFiles);
        ResponseEntity<?> response = fileController.getFiles(orgId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        List<Media> actualFiles = (List<Media>) response.getBody();

        assertNotNull(actualFiles);
        assertEquals(2, actualFiles.size());
        assertEquals("document.pdf", actualFiles.get(0).getFileName());
        verify(newFileService, times(1)).getAllFiles(orgId);
    }

//    @Test
//    void getFiles_WhenNoFiles_ShouldReturnEmptyList() {
//
//        Long orgId = 2L;
//
//        when(newFileService.getAllFiles(orgId)).thenReturn(Collections.emptyList());
//        ResponseEntity<?> response = fileController.getFiles(orgId);
//        assertNotNull(response);
//        assertEquals(200, response.getStatusCode().value());
//
//        List<Media> actualFiles = (List<Media>) response.getBody();
//        assertNotNull(actualFiles);
//        assertTrue(actualFiles.isEmpty());
//        verify(newFileService, times(1)).getAllFiles(orgId);
//    }
}