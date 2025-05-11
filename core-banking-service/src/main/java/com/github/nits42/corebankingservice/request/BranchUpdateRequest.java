package com.github.nits42.corebankingservice.request;

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
public class BranchUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    //@NotEmpty(message = "Branch Id is required")
    private UUID id;

    private String branchName;

    private String landmark;

    private String city;

    private String state;

    private String country;

    private Integer postalCode;

}
