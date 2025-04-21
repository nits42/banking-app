package com.github.nits42.userservice.swagger.apis;

import com.github.nits42.userservice.request.AddressRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Address", description = "Address API related for user Address Operations")
public interface AddressApi {

    @Operation(summary = "Create Address", description = "Create Address API")
    @ApiResponses(value = {
            // Add your API responses here
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<String> createAddress(@Valid @RequestBody AddressRequest request);
}
