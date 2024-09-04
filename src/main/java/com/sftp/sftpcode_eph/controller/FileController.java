package com.sftp.sftpcode_eph.controller;

import com.sftp.sftpcode_eph.entity.FileMetadata;
import com.sftp.sftpcode_eph.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileService.storeFile(file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) throws IOException {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metadata")
    public ResponseEntity<List<FileMetadata>> getAllFileMetadata() {
        return ResponseEntity.ok(fileService.getAllFileMetadata());
    }

    @GetMapping("/summary")
    public ResponseEntity<String> getFileSummary() {
        long totalFiles = fileService.getTotalFiles();
        long totalStorageSize = fileService.getTotalStorageSize();
        return ResponseEntity.ok("Total Files: " + totalFiles + ", Total Storage: " + totalStorageSize + " bytes");
    }
}

