package com.mis.FileHashing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.security.MessageDigest;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private FileRecordRepository fileRecordRepository;

    public String uploadFile(MultipartFile file) throws Exception {
        // Ensure upload directory exists
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Generate hash
        String hash = generateFileHash(file);

        // Check if file with same hash exists
        if (fileRecordRepository.findByFileHash(hash).isPresent()) {
            return " Duplicate file detected! Same content already uploaded.";
        }

        // Save file to disk
        Path filePath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save record to DB
        FileRecord record = FileRecord.builder()
                .fileName(file.getOriginalFilename())
                .filePath(filePath.toString())
                .fileHash(hash)
                .build();

        fileRecordRepository.save(record);

        return "✅ File uploaded successfully!\nFile Hash: " + hash;
    }

    private String generateFileHash(MultipartFile file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        InputStream inputStream = file.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        byte[] hashBytes = digest.digest();

        // Convert bytes to hex
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
