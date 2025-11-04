package com.mis.ServiceImpl;

import com.mis.DTO.Request.FileRequest1;
import com.mis.DTO.Request.SingleFileRequest;
import com.mis.Entity.Media;
import com.mis.Exception.ResourceNotFoundException;
import com.mis.Repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NewFileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final MediaRepository mediaRepository;

    private static final Logger logger = LoggerFactory.getLogger(NewFileService.class);

    @Async()
    public void saveFileData1(FileRequest1 newFileRequest) {
        String requestId = UUID.randomUUID().toString();
        Long organisationId = newFileRequest.getOrganizationId();
        logger.info("[{}] Upload started in thread: {}", requestId, Thread.currentThread().getName());

        for (SingleFileRequest fileRequest : newFileRequest.getFileDetails()) {
            MultipartFile file = fileRequest.getFile();
            String description = fileRequest.getDescription();

            if (file == null || file.isEmpty()) {
                logger.warn("[{}] Skipped empty file", requestId);
                continue;
            }

            try {
                String originalName = file.getOriginalFilename();
                if (originalName == null) {
                    logger.warn("[{}] Skipped file with null name", requestId);
                    continue;
                }

                String extension = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
                String fileType = determineFileType(extension);

                saveToStorageAndDB(organisationId, fileType, file, description, requestId);

            } catch (Exception e) {
                logger.error("[{}] Error processing file {} -> {}", requestId, file.getOriginalFilename(), e.getMessage());
            }
        }

        logger.info("[{}] Upload completed in thread: {}", requestId, Thread.currentThread().getName());
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    private String determineFileType(String ext) {
        if (ext.matches("jpg|jpeg|png|gif|bmp|webp")) return "image";
        if (ext.matches("txt|md|log")) return "text";
        if (ext.matches("csv")) return "csv";
        if (ext.matches("pdf")) return "pdf";
        if (ext.matches("mp3|wav|aac|flac")) return "audio";
        if (ext.matches("mp4|mov|avi|mkv")) return "video";
        return "other";
    }

    public void saveToStorageAndDB(Long orgId, String fileType, MultipartFile file, String description, String requestId) throws IOException {
        Path folderPath = Paths.get(uploadDir, String.valueOf(orgId), "media", fileType);

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
            logger.info("[{}] Created folder: {}", requestId, folderPath);
        }

        String newFileName = getUniqueFileName(folderPath, file.getOriginalFilename());
        Path targetPath = folderPath.resolve(newFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Save file data in DB
        Media media = new Media();
        media.setFileName(newFileName);
        media.setMediaType(fileType);
        media.setOrganisationId(orgId);
        media.setDescription(description);
        mediaRepository.save(media);

        logger.info("[{}]  Saved File: {}, Type: {}, Path: {}", requestId, file.getOriginalFilename(), fileType, targetPath);
    }

    private String getUniqueFileName(Path folderPath, String originalName) {
        String name = originalName.substring(0, originalName.lastIndexOf('.'));
        String extension = originalName.substring(originalName.lastIndexOf('.') + 1);
        Path filePath = folderPath.resolve(originalName);

        int count = 1;
        while (Files.exists(filePath)) {
            String newName = String.format("%s(%d).%s", name, count, extension);
            filePath = folderPath.resolve(newName);
            count++;
        }
        return filePath.getFileName().toString();
    }


    public List<Media> getAllFiles(Long orgId) {
       return mediaRepository.findByOrganisationIdAndMediaTypeNot(orgId,"logo_image");
    }

    public ResponseEntity<Resource> getFile(Long fileId,Long organisationId) throws IOException {

        Media media = mediaRepository.findByOrganisationIdAndId(organisationId, fileId);
        if (media==null){
            throw new ResourceNotFoundException("Media not found");
        }

        Path filePath = Paths.get(uploadDir, String.valueOf(organisationId), "media", media.getMediaType());

        Path imagePath = filePath.resolve(media.getFileName());
        System.out.println("Path :  "+imagePath);
        if (!Files.exists(imagePath)) {
            throw new FileNotFoundException("File not found: " + media.getFileName());
        }

        Resource resource = new UrlResource(imagePath.toUri());
        System.out.println("Resource: "+ resource);

        String mimeType = Files.probeContentType(imagePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + media.getFileName() + "\"")
                .body(resource);
    }


    public void deleteFile(Long id) throws IOException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Path path = Paths.get(uploadDir, String.valueOf(media.getOrganisationId()), "media", media.getMediaType(), media.getFileName());
        Files.deleteIfExists(path);

        mediaRepository.deleteById(id);
    }


    public void updateDescription(Long id,MultipartFile file, String description) throws IOException {
        String requestId = UUID.randomUUID().toString();
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        logger.info("[{}] update started in thread: {}", requestId, Thread.currentThread().getName());

        deleteFileInLocal(media);

        updateFileInDatabaseAndSystem(media,file,description,requestId);


    }

    private void deleteFileInLocal(Media media) throws IOException {
        Path path = Paths.get(uploadDir, String.valueOf(media.getOrganisationId()), "media", media.getMediaType(), media.getFileName());
        Files.deleteIfExists(path);
    }

    private void updateFileInDatabaseAndSystem(Media media, MultipartFile file, String description, String requestId) {
        try {
            String originalName = file.getOriginalFilename();
            if (originalName == null) {
                logger.warn("[{}] file with null name", requestId);
                return;
            }

            String extension = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
            String fileType = determineFileType(extension);

            Path folderPath = Paths.get(uploadDir, String.valueOf(media.getOrganisationId()), "media", fileType);

            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
                logger.info("[{}] Created folder: {}", requestId, folderPath);
            }
            String newFileName = getUniqueFileName(folderPath, file.getOriginalFilename());
            Path targetPath = folderPath.resolve(newFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            media.setDescription(description);
            media.setFileName(newFileName);
            mediaRepository.save(media);

        } catch (Exception e) {
            logger.error("[{}] Error update processing file {} -> {}", requestId, file.getOriginalFilename(), e.getMessage());
        }
    }
}
