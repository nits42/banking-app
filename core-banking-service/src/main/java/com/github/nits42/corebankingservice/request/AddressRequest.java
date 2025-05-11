package com.github.nits42.corebankingservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressRequest implements Serializable {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Flat/House no.  can't be blank")
    private String houseNumber;

    private String streetName;

    @NotBlank(message = "City can't be blank")
    private String city;

    @NotBlank(message = "State can't be blank")
    private String state;

    @NotBlank(message = "Country can't be blank")
    private String country;

    @NotNull(message = "Postal code is required")
    private Integer postalCode;

    private String landmark;

    @NotBlank(message = "Address Type can't be blank")
    private String addressType;

}
