package com.github.nits42.userservice.service;

import com.github.nits42.userservice.dto.AddressDTO;
import com.github.nits42.userservice.request.AddressRequest;
import com.github.nits42.userservice.request.AddressUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {

    String createAddress(@Valid AddressRequest request);
    String updateAddress(@Valid AddressUpdateRequest request, String id);
    List<AddressDTO> getAllAddressByUsername(String username);
    AddressDTO getAllAddressById(String id);
    String deleteAddress(String id);


}