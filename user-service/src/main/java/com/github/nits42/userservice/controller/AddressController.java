package com.github.nits42.userservice.controller;

import com.github.nits42.userservice.dto.AddressDTO;
import com.github.nits42.userservice.request.AddressRequest;
import com.github.nits42.userservice.request.AddressUpdateRequest;
import com.github.nits42.userservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<String> createAddress(@Valid @RequestBody AddressRequest request) {
        log.info("Address creating process is started for: {}", request.getUsername());
        return new ResponseEntity<>(addressService.createAddress(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAddress(@RequestBody AddressUpdateRequest request,
                                                @PathVariable String id) {
        log.info("Address updating process is started for: {}", id);
        return new ResponseEntity<>(addressService.updateAddress(request, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable String id) {
        log.info("Address deletion process is started for: {}", id);
        return new ResponseEntity<>(addressService.deleteAddress(id), HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<List<AddressDTO>> getAllAddress() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//        log.info("Logged user: {}", username);
//        return new ResponseEntity<>(addressService.getAllAddressByUsername(username), HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressByID(@PathVariable String id) {
        log.info("Fetching address based on ID: {}", id);
        return new ResponseEntity<>(addressService.getAllAddressById(id), HttpStatus.OK);
    }


}
