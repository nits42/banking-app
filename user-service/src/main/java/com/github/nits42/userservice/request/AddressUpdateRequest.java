package com.github.nits42.userservice.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressUpdateRequest implements Serializable {

    private String houseNumber;
    private String streetName;
    private String city;
    private String state;
    private String country;
    private Integer postalCode;
    private String landmark;
    private String addressType;

}