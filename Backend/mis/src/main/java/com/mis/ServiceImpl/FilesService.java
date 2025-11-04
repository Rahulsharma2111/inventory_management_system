package com.mis.ServiceImpl;

import com.mis.DTO.Request.FileRequest;
import com.mis.Entity.Media;
import com.mis.Repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class FilesService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final MediaRepository mediaRepository;

    private static final Logger logger = LoggerFactory.getLogger(FilesService.class);


    @Async("first_thread")
    public void saveFileData(FileRequest fileRequest) throws IOException {
        Long organisationId = fileRequest.getOrganizationId();
        logger.info("Upload started in thread: {}", Thread.currentThread().getName());

        for (MultipartFile file : fileRequest.getFiles()) {
            if (file.isEmpty()) continue;

            try {
                // Determine file type
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null) continue;

                String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
                String fileType = getFileType(extension);

                // Save file immediately
                saveFileFunction(organisationId, fileType, file);

            } catch (Exception e) {
                logger.error("Error saving file {}: {}", file.getOriginalFilename(), e.getMessage());
            }
        }

        logger.info("Upload finished in thread: {}", Thread.currentThread().getName());
    }


    private String getFileType(String ext) {
        if (ext.matches("jpg|jpeg|png|gif|bmp|webp")) return "image";
        if (ext.matches("txt|md|log")) return "text";
        if (ext.matches("csv")) return "csv";
        if (ext.matches("pdf")) return "pdf";
        if (ext.matches("mp3|wav|aac|flac")) return "audio";
        if (ext.matches("mp4|mov|avi|mkv")) return "video";
        return "other";
    }

    public void saveFileFunction(Long organisationId, String fileType, MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir, String.valueOf(organisationId), "media", fileType);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(file.getOriginalFilename());

        // save in DB
        Media media=new Media();
        media.setFileName(file.getOriginalFilename());
        media.setMediaType(fileType);
        media.setOrganisationId(organisationId);
        mediaRepository.save(media);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Saved file to: " + filePath);
    }
}
