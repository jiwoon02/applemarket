package com.apple.common.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomeFileUtil {

    @Value("${com.apple.upload.path}")
    private String uploadPath;
    
    public String getUploadPath() {
        return uploadPath;
    }
    
    // 상품별 폴더 생성
    public String createProductFolder(Long productId) {
        String productFolderPath = Paths.get(uploadPath, "product_" + productId).toString();
        File productFolder = new File(productFolderPath);

        if (!productFolder.exists()) {
            productFolder.mkdirs(); // 폴더가 존재하지 않으면 생성
        }

        return productFolderPath;
    }


    public List<String> saveFiles(List<MultipartFile> files, Long productId) throws IOException {
        List<String> savedFileNames = new ArrayList<>();

        // 상품ID로 폴더 생성
        String productFolderPath = createProductFolder(productId);
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String savePath = Paths.get(productFolderPath, originalFilename).toString();
                File dest = new File(savePath);

                try (InputStream in = file.getInputStream()) {
                    Files.copy(in, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
                }

                savedFileNames.add(originalFilename);
            }
        }
        return savedFileNames;
    }

    // 파일 불러오기 메서드
    public ResponseEntity<Resource> getFile(Long productId, String fileName) {
        String productFolderPath = createProductFolder(productId);
        Path filePath = Paths.get(productFolderPath, fileName);
        Resource resource = new FileSystemResource(filePath);
        
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    // 파일 삭제 메서드
    public void deleteFile(String fileName, Long productId) {
        String productFolderPath = createProductFolder(productId);
        Path filePath = Paths.get(productFolderPath, fileName);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                log.warn("파일이 존재하지 않습니다: " + fileName);
            }
        } catch (IOException e) {
            log.error("파일 삭제 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("파일 삭제 실패: " + e.getMessage());
        }
    }

    // 상품 삭제 시 폴더 전체 삭제
    public void deleteProductFolder(Long productId) {
        String productFolderPath = Paths.get(uploadPath, "product_" + productId).toString();
        File productFolder = new File(productFolderPath);

        if (productFolder.exists()) {
            try {
                Files.walk(productFolder.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            } catch (IOException e) {
                log.error("상품 폴더 삭제 중 오류 발생: " + e.getMessage());
                throw new RuntimeException("상품 폴더 삭제 실패: " + e.getMessage());
            }
        }
    }
}
