package com.mis.FileHashing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return fileService.uploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ Upload failed: " + e.getMessage();
        }
    }
}
