package com.github.nits42.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDTO implements Serializable {

    private UUID id;
    private String houseNumber;
    private String streetName;
    private String city;
    private String state;
    private String country;
    private Integer postalCode;
    private String landmark;
    private String addressType;

}
