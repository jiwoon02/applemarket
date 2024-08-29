package com.apple.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomeFileUtil {

    @Value("${com.apple.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);
        
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }
        
        uploadPath = tempFolder.getAbsolutePath();
        log.info("--------------------------------");
        log.info(uploadPath);
    }

    // 다중 파일 처리 메서드
    public List<String> saveFiles(List<MultipartFile> files) {
        List<String> savedFileNames = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String saveName = saveFile(file); // 모든 파일을 그대로 저장
                if (saveName != null) {
                    savedFileNames.add(saveName);
                }
            }
        }

        return savedFileNames;
    }

    // 단일 파일 처리 메서드
    private String saveFile(MultipartFile file) throws RuntimeException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String saveName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path savePath = Paths.get(uploadPath, saveName);

        try {
            Files.copy(file.getInputStream(), savePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }

        return saveName;
    }

    // 파일 가져오기 메서드
    public ResponseEntity<Resource> getFile(String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        if (!resource.exists()) {
            resource = new FileSystemResource(uploadPath + File.separator + "default.jpg");
        }

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    // 파일 삭제 메서드
    public void deleteFile(String fileName) {
        if (fileName == null) {
            return;
        }

        Path filePath = Paths.get(uploadPath, fileName);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
