package com.github.nits42.userservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * UserDetailsRequest is a data transfer object (DTO) that represents the details of a user.
 * It is used to transfer user details data between different layers of the application.
 */
public class UserDetailsRequest implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private Integer phoneNumber;
    private LocalDate dateOfBirth;
    private String aboutMe;
    private String profilePicture;

}
