package com.ab.perfume.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String saveLocionImage(MultipartFile file);

    void deleteLocionImage(String fileName);
}
