package me.imatveev.filestorage.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public interface FileService {
    UUID save(UUID id, MultipartFile file);

    File findById(UUID id);
}
