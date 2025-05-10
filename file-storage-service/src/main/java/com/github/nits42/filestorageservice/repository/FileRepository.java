package com.github.nits42.filestorageservice.repository;


import com.github.nits42.filestorageservice.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Author: Nitin Kumar
 * Date:10/05/25
 * Time:21:05
 */

public interface FileRepository extends MongoRepository<File, String> {
    // Custom query methods can be defined here if needed
}
