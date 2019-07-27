package dev.muskrat.delivery.files.components;

import dev.muskrat.delivery.components.exception.FileStorageException;
import lombok.Getter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileFormat {

    @Getter
    private Path path;

    public FileFormat(String mainDir, String path) {
        path = mainDir + path;

        this.path = Paths.get(path)
            .toAbsolutePath()
            .normalize();

        try {
            Files.createDirectories(this.path);
        } catch (Exception ex) {
            throw new FileStorageException
                ("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
}
