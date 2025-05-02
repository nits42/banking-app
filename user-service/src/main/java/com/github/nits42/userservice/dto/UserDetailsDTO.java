package com.github.nits42.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailsDTO {

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private LocalDate dateOfBirth;
    private String aboutMe;
    private String profilePicture;
    private Set<AddressDTO> addresses;
    
}