package com.sftp.sftpcode_eph.service;

import com.sftp.sftpcode_eph.entity.FileMetadata;
import com.sftp.sftpcode_eph.repository.FileMetadataRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    private final Path rootLocation = Paths.get("filestorage");

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    public FileService() throws IOException {
        Files.createDirectories(rootLocation);
    }

    public FileMetadata storeFile(MultipartFile file) throws IOException {
        // Compress
        String compressedFileName = compressImage(file);
        Path destinationFile = this.rootLocation.resolve(
                        Paths.get(compressedFileName))
                .normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile,
                StandardCopyOption.REPLACE_EXISTING);
        FileMetadata metadata = new FileMetadata();
        metadata.setFileName(compressedFileName);
        metadata.setFileUrl(destinationFile.toUri().toString());
        metadata.setFileSize(Files.size(destinationFile));
        metadata.setUploadDate(LocalDateTime.now());

        return fileMetadataRepository.save(metadata);
    }

    @Cacheable("fileMetadata")
    public List<FileMetadata> getAllFileMetadata() {
        return fileMetadataRepository.findAll();
    }

    @CacheEvict(value = "fileMetadata", allEntries = true)
    public void deleteFile(Long id) throws IOException {
        FileMetadata metadata = fileMetadataRepository.findById(id)
                .orElseThrow(() -> new IOException("File not found"));
        Files.deleteIfExists(Paths.get(metadata.getFileUrl()));
        fileMetadataRepository.deleteById(id);
    }

    private String compressImage(MultipartFile file) throws IOException {
        if (file.getContentType() != null && file.getContentType().startsWith("image")) {
            File tempFile = File.createTempFile("compressed-", ".jpg");
            Thumbnails.of(file.getInputStream())
                    .size(800, 600)
                    .outputFormat("jpg")
                    .outputQuality(0.7)
                    .toFile(tempFile);
            return tempFile.getName();
        }
        return file.getOriginalFilename();
    }

    public long getTotalStorageSize() {
        return fileMetadataRepository.findAll()
                .stream()
                .mapToLong(FileMetadata::getFileSize)
                .sum();
    }

    public long getTotalFiles() {
        return fileMetadataRepository.count();
    }
}
