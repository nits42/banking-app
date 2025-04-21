package com.github.nits42.userservice.service.impl;

import com.github.nits42.userservice.dto.AddressDTO;
import com.github.nits42.userservice.entities.Address;
import com.github.nits42.userservice.entities.User;
import com.github.nits42.userservice.enums.AddressType;
import com.github.nits42.userservice.enums.Status;
import com.github.nits42.userservice.exceptions.BankingAppUserServiceException;
import com.github.nits42.userservice.repository.AddressRepository;
import com.github.nits42.userservice.repository.UserRepository;
import com.github.nits42.userservice.request.AddressRequest;
import com.github.nits42.userservice.request.AddressUpdateRequest;
import com.github.nits42.userservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public String createAddress(AddressRequest request) {
        log.info("User's Address saving process is started");
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new BankingAppUserServiceException("User not found with given username: " + request.getUsername(),
                                HttpStatus.NOT_FOUND));

        Address address = Address.builder()
                .user(user)
                .houseNumber(request.getHouseNumber())
                .streetName(request.getStreetName())
                .landmark(request.getLandmark())
                .state(request.getState())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .status(Status.ACTIVE)
                .addressType(AddressType.valueOf(request.getAddressType().toUpperCase()))
                .build();
        address = addressRepository.save(address);
        if (address.getId() == null)
            throw BankingAppUserServiceException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("User's address is not created")
                    .build();
        log.info("User's Address saving process is completed");
        return "User's address is created successfully!";
    }

    @Override
    public String updateAddress(AddressUpdateRequest request, String id) {
        log.info("User's address update process is started");
        Address toUpdate = addressRepository.findById(UUID.fromString(id))
                .orElseThrow(() ->
                        new BankingAppUserServiceException("Address not found with given id: " + id,
                                HttpStatus.NOT_FOUND));
        this.modelMapper.map(request, toUpdate);

        if (!(request.getAddressType() == null))
            toUpdate.setAddressType(AddressType.valueOf(request.getAddressType().toUpperCase()));

        this.addressRepository.save(toUpdate);
        log.info("User's address update process is completed");
        return "Address updated successfully";
    }

    @Override
    public List<AddressDTO> getAllAddressByUsername(String username) {
        log.info("Fetching all addresses of user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new BankingAppUserServiceException("User not found with given username: " + username,
                                HttpStatus.NOT_FOUND));

        List<Address> addressList = addressRepository.findAllByUserAndStatus(user, Status.ACTIVE);
        if (addressList == null || addressList.isEmpty())
            throw BankingAppUserServiceException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("User's address not found with given username: " + username)
                    .build();

        log.info("Fetching all addresses of user {} is completed", username);
        return addressList.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAllAddressById(String id) {
        log.info("Fetching address: {}", id);
        Address address = addressRepository.findById(UUID.fromString(id))
                .orElseThrow(() ->
                        new BankingAppUserServiceException("Address not found with given id: " + id,
                                HttpStatus.NOT_FOUND));

        log.info("Fetching address is completed");
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public String deleteAddress(String id) {
        log.info("Deleting address: {}", id);

        Address address = addressRepository.findById(UUID.fromString(id))
                .orElseThrow(() ->
                        new BankingAppUserServiceException("Address not found with given id: " + id,
                                HttpStatus.NOT_FOUND));

        address.setStatus(Status.DELETED);
        addressRepository.save(address);

        log.info("Address deletion is completed");
        return "Address is deleted successfully";
    }

}
