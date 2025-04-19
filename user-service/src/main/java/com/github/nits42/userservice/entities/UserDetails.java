package com.github.nits42.userservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private Integer phoneNumber;
    private LocalDate dateOfBirth;
    private String aboutMe;
    private String profilePicture;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Address> addresses; //One user can have multiple addresses like current, permanent, office etc.

}
