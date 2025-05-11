package com.github.nits42.corebankingservice.entities;

import com.github.nits42.corebankingservice.enums.Status;
import com.github.nits42.corebankingservice.util.BranchUtils;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Branch extends BaseEntity {

    @Column(unique = true, updatable = false, nullable = false)
    private String branchCode;

    @Column(unique = true, updatable = false, nullable = false)
    private String IFSCCode;

    @Column(unique = true, nullable = false)
    private String branchName;
    private String landmark;
    private String city;
    private String state;
    private String country;
    private int postalCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @PrePersist
    private void prePersist() {
        // Generate a random branch code
        this.branchCode = BranchUtils.generateBranchCode().toUpperCase();

        // Generate a random IFSC code
        this.IFSCCode = BranchUtils.generateIFSCCode(branchCode);
    }

}
