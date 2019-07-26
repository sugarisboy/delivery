package dev.muskrat.delivery.files.service;

import dev.muskrat.delivery.components.exception.FileStorageException;
import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FIleStorageServiceImpl implements FileStorageService {

    @Value("application.file.storage.upload-dir.product")
    private String FILE_STORAGE_DIRECTORY_PRODUCT;

    @Getter
    private Path pathForProduct;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        pathForProduct = Paths.get(FILE_STORAGE_DIRECTORY_PRODUCT)
            .toAbsolutePath()
            .normalize();

        try {
            Files.createDirectories(pathForProduct);
        } catch (Exception ex) {
            throw new FileStorageException
                ("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public FileStorageUploadDTO uploadFile(Path uploadPath, String fileName, MultipartFile uploadFile) {
        try {

            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(uploadFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return FileStorageUploadDTO.builder()
                .fileName(fileName)
                .build();

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
