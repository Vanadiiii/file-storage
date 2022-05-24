package me.imatveev.filestorage.domain;

import me.imatveev.filestorage.domain.entity.FileInfo;

import java.util.Optional;
import java.util.UUID;

public interface FileInfoService {
    FileInfo save(FileInfo fileInfo);

    Optional<FileInfo> findById(UUID id);
}
