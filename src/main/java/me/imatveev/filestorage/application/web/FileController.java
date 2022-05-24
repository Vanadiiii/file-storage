package me.imatveev.filestorage.application.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.imatveev.filestorage.domain.FileInfoService;
import me.imatveev.filestorage.domain.FileService;
import me.imatveev.filestorage.domain.entity.FileInfo;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final FileInfoService fileInfoService;

    @PostMapping(consumes = MediaType.ALL_VALUE)
    public Map<String, UUID> save(@RequestParam Map<String, MultipartFile> files) {
        return files.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            final FileInfo fileInfo = fileInfoService.save(
                                    FileInfo.of(entry.getValue().getOriginalFilename())
                            );
                            return fileService.save(
                                    fileInfo.getId(),
                                    entry.getValue()
                            );
                        }
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> findById(@PathVariable UUID id) throws IOException {
        final String fileName = fileInfoService.findById(id)
                .orElseThrow()
                .getName();

        final File file = fileService.findById(id);
        final InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileName))
                .header(HttpHeaders.CACHE_CONTROL, "no-cache", "no-store", "must-revalidate")
                .header(HttpHeaders.PRAGMA, "no-cache")
                .header(HttpHeaders.EXPIRES, "0")
                .body(resource);
    }
}
