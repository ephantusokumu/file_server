package com.sftp.sftpcode_eph.repository;

import com.sftp.sftpcode_eph.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
}
