package com.github.nits42.filestorageservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "files")
public class File {

    @Id
    private String id;

    private String type;

    private String filePath;

    private LocalDateTime creationTimestamp;

    private LocalDateTime updateTimestamp;

}



