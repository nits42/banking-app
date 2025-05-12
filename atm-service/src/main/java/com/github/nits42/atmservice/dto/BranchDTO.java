package com.github.nits42.atmservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class BranchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private String branchCode;
    private String IFSCCode;
    private String branchName;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private Integer postalCode;
    private String status;

}
