package dev.muskrat.delivery.files.service;

import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {

    FileStorageUploadDTO uploadFile(Path uploadPath, String fileName, MultipartFile uploadFile);

    Path getPathForProduct();
}
