package com.github.nits42.corebankingservice.repository;

import com.github.nits42.corebankingservice.entities.Address;
import com.github.nits42.corebankingservice.entities.User;
import com.github.nits42.corebankingservice.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findAllByUserAndStatus(User user, Status status);
}
