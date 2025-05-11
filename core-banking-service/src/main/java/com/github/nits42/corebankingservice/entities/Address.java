package com.github.nits42.corebankingservice.entities;

import com.github.nits42.corebankingservice.enums.AddressType;
import com.github.nits42.corebankingservice.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity {

    private String houseNumber;
    private String streetName;
    private String city;
    private String state;
    private String country;
    private Integer postalCode;
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
