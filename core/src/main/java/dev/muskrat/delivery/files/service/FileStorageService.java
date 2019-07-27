package dev.muskrat.delivery.files.service;

import dev.muskrat.delivery.files.components.FileFormat;
import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileStorageUploadDTO uploadFile(FileFormat type, String fileName, MultipartFile uploadFile);

    Resource loadFile(FileFormat type, String fileName);
}
