package com.github.nits42.userservice.repository;

import com.github.nits42.userservice.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {


}
