package com.github.nits42.userservice.entities;

import com.github.nits42.userservice.enums.AddressType;
import com.github.nits42.userservice.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity {

    private String HouseNumber;
    private String streetName;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String landmark;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;


}
