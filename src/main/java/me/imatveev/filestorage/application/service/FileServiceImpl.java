package me.imatveev.filestorage.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.imatveev.filestorage.domain.FileInfoService;
import me.imatveev.filestorage.domain.FileService;
import me.imatveev.filestorage.domain.entity.FileInfo;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private static final String DIR_PATH_STR = "./storage";
    private static final Path DIR_PATH = Path.of("./storage");

    @Override
    @Transactional
    public UUID save(UUID id, MultipartFile file) {
        try {
            if (Files.notExists(DIR_PATH)) {
                Files.createDirectory(DIR_PATH);
            }

            final String originalFilename = file.getOriginalFilename();

            final String extension = FilenameUtils.getExtension(originalFilename);

            final Path filePath = Path.of(DIR_PATH_STR + "/" + id + "." + extension);
            Files.createFile(filePath);
            Files.write(filePath, file.getBytes());

            return id;
        } catch (IOException e) {
            log.error("Can't save file, cause: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public File findById(UUID id) {
        try {
            return Files.find(
                            DIR_PATH,
                            1,
                            (path, attributes) -> path.toFile().isFile()
                    )
                    .map(Path::toFile)
                    .filter(file -> FilenameUtils.getName(file.getName()).startsWith(id.toString()))
                    .findFirst()
                    .orElseThrow();
        } catch (IOException e) {
            log.error("Can't find file by id, cause: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
