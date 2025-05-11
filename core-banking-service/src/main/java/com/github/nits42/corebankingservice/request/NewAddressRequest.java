package com.github.nits42.corebankingservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewAddressRequest {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Flat/House no.  can't be blank")
    private String flatNo;

    private String line1;

    @NotBlank(message = "City can't be blank")
    private String city;

    @NotBlank(message = "State can't be blank")
    private String state;

    @NotBlank(message = "Country can't be blank")
    private String country;

    @NotNull(message = "Postal code is required")
    @Size(min = 6, max = 6, message = "Postal code should contains 6 digits")
    private Integer postalCode;

    private String landmark;

    @NotBlank(message = "Address Type can't be blank")
    private String addressType;

}
