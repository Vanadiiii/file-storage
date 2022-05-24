package me.imatveev.filestorage.application.repository;

import me.imatveev.filestorage.domain.FileInfoService;
import me.imatveev.filestorage.domain.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface FileInfoRepository extends FileInfoService, JpaRepository<FileInfo, UUID> {
}
