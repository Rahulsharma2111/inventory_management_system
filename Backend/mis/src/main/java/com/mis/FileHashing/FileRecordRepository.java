package com.mis.FileHashing;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
    Optional<FileRecord> findByFileHash(String fileHash);
}
