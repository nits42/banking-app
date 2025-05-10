package com.github.nits42.filestorageservice.service.imp;

import com.github.nits42.filestorageservice.exceptions.BankingAppFileStorageServiceApiException;
import com.github.nits42.filestorageservice.model.File;
import com.github.nits42.filestorageservice.repository.FileRepository;
import com.github.nits42.filestorageservice.service.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

/**
 * Author: Nitin Kumar
 * Date:10/05/25
 * Time:21:06
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final FileRepository fileRepository;

    private String FOLDER_PATH;

    /**
     * This method is called after the bean is constructed and is used to initialize the folder path for file storage.
     * It creates the directory if it does not exist.
     */
    @PostConstruct
    public void init() {
        String currentWorkingDirectory = System.getProperty("user.dir");

        FOLDER_PATH = currentWorkingDirectory + "/file-storage-service/src/main/resources/attachments";

        java.io.File targetFolder = new java.io.File(FOLDER_PATH);

        if (!targetFolder.exists()) {
            boolean directoriesCreated = targetFolder.mkdirs();
            if (!directoriesCreated) {
                throw BankingAppFileStorageServiceApiException.builder()
                        .message("Unable to create directories")
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }
        }
    }

    @Override
    public String uploadImageToFileSystem(MultipartFile file) {
        log.info("file uploading is started.");
        String uuid = UUID.randomUUID().toString();
        String filePath = FOLDER_PATH + "/" + uuid;

        try {
            file.transferTo(new java.io.File(filePath));
        } catch (IOException e) {
            throw BankingAppFileStorageServiceApiException.builder()
                    .message("Unable to save file to storage")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        fileRepository.save(File.builder()
                .id(uuid)
                .type(file.getContentType())
                .filePath(filePath)
                .creationTimestamp(java.time.LocalDateTime.now())
                .updateTimestamp(java.time.LocalDateTime.now())
                .build());

        log.info("file uploading is completed.");
        return uuid;
    }

    @Override
    public byte[] downloadImageFromFileSystem(String id) {
        log.info("file downloading is started.");
        try {
            File file = findFileById(id);
            java.io.File f = new java.io.File(file
                    .getFilePath());
            return Files.readAllBytes(f.toPath());
        } catch (IOException e) {
            throw BankingAppFileStorageServiceApiException.builder()
                    .message("Unable to read file from storage")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public void deleteImageFromFileSystem(String id) {
        java.io.File file = new java.io.File(findFileById(id).getFilePath());

        boolean deletionResult = file.delete();

        if (deletionResult) fileRepository.deleteById(id);

        else throw BankingAppFileStorageServiceApiException.builder()
                .message("Unable to delete file from storage")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }


    @Override
    public File findFileById(String id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> BankingAppFileStorageServiceApiException.builder()
                        .message("File not found")
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

}
