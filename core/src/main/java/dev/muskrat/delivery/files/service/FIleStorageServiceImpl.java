package dev.muskrat.delivery.files.service;

import dev.muskrat.delivery.components.exception.FileStorageException;
import dev.muskrat.delivery.files.components.FileFormat;
import dev.muskrat.delivery.files.dto.FileStorageUploadDTO;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FIleStorageServiceImpl implements FileStorageService {

    @Override
    public FileStorageUploadDTO uploadFile(FileFormat type, String fileName, MultipartFile uploadFile) {
        try {
            Path uploadPath = type.getPath();
            Path targetLocation = uploadPath.resolve(fileName);

            Files.copy(
                uploadFile.getInputStream(),
                targetLocation,
                StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(uploadPath);

            return FileStorageUploadDTO.builder()
                .fileName(fileName)
                .build();

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file. Please try again!");
        }
    }

    @Override
    public Resource loadFile(FileFormat type, String fileName) {
        Path filePath = type.getPath().resolve(fileName).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("File not found " + filePath + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found " + filePath + fileName, ex);
        }
    }
}
