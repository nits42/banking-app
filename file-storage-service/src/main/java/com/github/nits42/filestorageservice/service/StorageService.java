package com.github.nits42.filestorageservice.service;


import com.github.nits42.filestorageservice.model.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Nitin Kumar
 * Date:10/05/25
 * Time:21:06
 */

public interface StorageService {

    String uploadImageToFileSystem(MultipartFile file);

    byte[] downloadImageFromFileSystem(String id);

    void deleteImageFromFileSystem(String id);

    File findFileById(String id);

}