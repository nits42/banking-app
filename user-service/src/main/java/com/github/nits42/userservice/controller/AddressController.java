package com.github.nits42.userservice.controller;

import com.github.nits42.userservice.dto.AddressDTO;
import com.github.nits42.userservice.request.AddressRequest;
import com.github.nits42.userservice.request.AddressUpdateRequest;
import com.github.nits42.userservice.service.AddressService;
import com.github.nits42.userservice.swagger.apis.AddressApi;
import com.github.nits42.userservice.util.AppConstant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(AppConstant.USER_SERVICE_ADDRESS_BASE_URL)
@RequiredArgsConstructor
// @Tag(name = "Address", description = "Address API related for user Address Operations")
public class AddressController implements AddressApi {

    private final AddressService addressService;

    /*@Operation(summary = "Create Address", description = "Create Address API")
    @ApiResponses(value = {
            // Add your API responses here
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })*/
    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createAddress(@Valid @RequestBody AddressRequest request) {
        log.info("Address creating process is started for: {}", request.getUsername());
        return new ResponseEntity<>(addressService.createAddress(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAddress(@RequestBody AddressUpdateRequest request,
                                                @PathVariable String id) {
        log.info("Address updating process is started for: {}", id);
        return new ResponseEntity<>(addressService.updateAddress(request, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAddress(@PathVariable String id) {
        log.info("Address deletion process is started for: {}", id);
        return new ResponseEntity<>(addressService.deleteAddress(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddress() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("Logged user: {}", username);
        return new ResponseEntity<>(addressService.getAllAddressByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressByID(@PathVariable String id) {
        log.info("Fetching address based on ID: {}", id);
        return new ResponseEntity<>(addressService.getAllAddressById(id), HttpStatus.OK);
    }


}
