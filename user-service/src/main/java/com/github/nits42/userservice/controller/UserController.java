package com.github.nits42.userservice.controller;

import com.github.nits42.userservice.dto.CardDTO;
import com.github.nits42.userservice.dto.UserDTO;
import com.github.nits42.userservice.request.TokenRequest;
import com.github.nits42.userservice.request.UserPasswordUpdateRequest;
import com.github.nits42.userservice.request.UserSignupRequest;
import com.github.nits42.userservice.request.UserUpdateRequest;
import com.github.nits42.userservice.service.UserService;
import com.github.nits42.userservice.util.AppConstant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(AppConstant.USER_SERVICE_BASE_URL)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final ApplicationContext applicationContext;

    private SecurityContextHolder securityContextHolder;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserSignupRequest request) {
        // Logic to create a user
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        // Logic to get a user
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("@userService.getUserById(#id).username == principal")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        // Logic to get a user by ID
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        // Logic to get a user by username
        log.info("Getting user by username: {}", username);
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("@userService.getUserByEmail(#email).username == principal")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        // Logic to get a user by email
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        // Logic to update a user
        return new ResponseEntity<>(userService.updateUser(id, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        // Logic to delete a user
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Logged user: {}", authentication.getPrincipal());
        return ResponseEntity.ok(modelMapper.map(userService.getUserByUsername(authentication.getPrincipal().toString()), UserDTO.class));
    }

    @PutMapping(value = "/updatePassword")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UserPasswordUpdateRequest request) {
        log.info("Update user's password process is started");
        return new ResponseEntity<>(userService.updatePassword(request), HttpStatus.OK);
    }

    @PutMapping(value = "/uploadProfilePhoto/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadUserProfilePhotoById(@Valid @RequestParam(value = "file") MultipartFile file,
                                                             @PathVariable UUID id) {
        log.info("Uploading Profile photo of user: {} ", id);
        return new ResponseEntity<>(userService.uploadUserProfilePhotoById(file, id), HttpStatus.OK);
    }

    @GetMapping("/getProfilePhoto")
    public ResponseEntity<?> getProfilePhoto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Logged user: {}", authentication.getPrincipal());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(userService.downloadImageFromFileSystem(authentication.getPrincipal().toString()));
    }

    @PostMapping("/token")
    ResponseEntity<String> save(@RequestBody TokenRequest request) {
        return new ResponseEntity<>(userService.saveToken(request), HttpStatus.CREATED);
    }

    @GetMapping("/getAllCards/{username}")
    public ResponseEntity<List<CardDTO>> getAllCardsByUsername(@PathVariable String username) {
        log.info("Fetching user's cards details");
        return ResponseEntity.ok(userService.getAllCardsByUsername(username));
    }

}
