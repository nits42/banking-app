package com.github.nits42.corebankingservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String flatNo;
    private String line1;
    private String city;
    private String state;
    private String country;
    private Integer postalCode;
    private String landmark;
    private String addressType;

}
