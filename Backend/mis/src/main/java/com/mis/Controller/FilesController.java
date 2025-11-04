package com.mis.Controller;

import com.mis.DTO.Request.FileRequest;
import com.mis.DTO.Request.FileRequest1;
import com.mis.Entity.Media;
import com.mis.ServiceImpl.FilesService;
import com.mis.ServiceImpl.NewFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FilesController {

    private final FilesService fileService;
    private final NewFileService newFileService;

    @PostMapping("/upload/v1")
    public ResponseEntity<String> uploadFile(@ModelAttribute FileRequest fileRequest) {
        try {
            fileService.saveFileData(fileRequest);
            return ResponseEntity.ok("Files uploaded successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/upload/v2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFiles(
            @ModelAttribute FileRequest1 fileRequest1) {
        try {
            newFileService.saveFileData1(fileRequest1);
            return ResponseEntity.ok("Files uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/files/{orgId}")
    public ResponseEntity<?> getFiles(@PathVariable Long orgId) {
        List<Media> files = newFileService.getAllFiles(orgId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files")
    public ResponseEntity<Resource> getMediaFile(@RequestParam Long fileId,
                                                 @RequestParam Long organisationId) throws IOException {
        return newFileService.getFile(fileId,organisationId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        try {
            newFileService.deleteFile(id);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFileDescription(@PathVariable Long id,
                                                        @RequestParam MultipartFile file,
                                                        @RequestParam String description) {
        try {
            newFileService.updateDescription(id,file, description);
            return ResponseEntity.ok("File updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}
