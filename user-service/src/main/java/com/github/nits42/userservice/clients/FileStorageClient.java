package com.github.nits42.userservice.clients;

import com.github.nits42.userservice.util.AppConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file-storage-service", path = AppConstant.FILE_STORAGE_SERVICE_BASE_URL)
public interface FileStorageClient {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> uploadImageToFileSystem(@RequestPart("image") MultipartFile file);

    @GetMapping("/download/{id}")
    ResponseEntity<byte[]> getProfilePhoto(@PathVariable String id);

    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteImageFromFileSystem(@PathVariable String id);

}
