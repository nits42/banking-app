package com.github.nits42.userservice.service.impl;

import com.github.nits42.userservice.dto.UserDTO;
import com.github.nits42.userservice.entities.User;
import com.github.nits42.userservice.entities.UserDetails;
import com.github.nits42.userservice.enums.Role;
import com.github.nits42.userservice.enums.Status;
import com.github.nits42.userservice.exceptions.BankingAppUserServiceException;
import com.github.nits42.userservice.repository.UserRepository;
import com.github.nits42.userservice.request.UserCreateRequest;
import com.github.nits42.userservice.request.UserUpdateRequest;
import com.github.nits42.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public String createUser(UserCreateRequest request) {
        log.info("User creation is started");

        if(request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
            log.error("User creation failed: Missing required fields");
            throw BankingAppUserServiceException.builder()
                    .message("Missing required fields")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if(userRepository.existsByUsername(request.getUsername())){
            log.error("User creation failed: Username already exists");
            throw BankingAppUserServiceException.builder()
                    .message("Username already exists")
                    .httpStatus(HttpStatus.FOUND)
                    .build();
        }
        if(userRepository.existsByEmail(request.getEmail())){
            log.error("User creation failed: Email already exists");
            throw BankingAppUserServiceException.builder()
                    .message("Email already exists")
                    .httpStatus(HttpStatus.FOUND)
                    .build();
        }
        User userEntity = convertToEntity(request);
        userEntity.setStatus(Status.ACTIVE);
        userEntity.setRole(Role.USER);
        userRepository.save(userEntity);

        log.info("User creation is completed");
        return "User created successfully";
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findByStatus(Status.ACTIVE)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public UserDTO getUserById(String id) {
        log.info("User's details retrieval process is started with Id: {}", id);
        User user = findById(id);
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        log.info("User's details retrieval process is started with username: {}", username);
        User user = findByUsername(username);
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.info("User's details retrieval process is started with email: {}", email);
        User user = findByEmail(email);
        return convertToDTO(user);
    }

    @Override
    public String updateUser(String id, UserUpdateRequest request) {
        log.info("User's details update process is started");
        User userToUpdate = findById(id);

        request.setUserDetailsRequest(updateUserDetails(userToUpdate.getUserDetails(), request.getUserDetailsRequest()));
        modelMapper.map(request, userToUpdate);
        userRepository.save(userToUpdate);
        log.info("User's details update process is completed");
        return "User updated successfully";
    }

    @Override
    public String deleteUser(String id) {
        log.info("User deletion process is started");
        User user = findById(id);
        user.setStatus(Status.DELETED);
        userRepository.save(user);
        log.info("User deletion process is completed");
        return "User deleted successfully";
    }

    private User findByUsername(String username) {
        return userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> BankingAppUserServiceException.builder()
                        .message("User not found with username: " + username)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    private User findByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> BankingAppUserServiceException.builder()
                        .message("User not found with email: " + email)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    private User findById(String id) {
        return userRepository.findByIdAndStatus(UUID.fromString(id), Status.ACTIVE)
                .orElseThrow(() -> BankingAppUserServiceException.builder()
                        .message("User not found with Id: " + id)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private User convertToEntity(UserCreateRequest userCreateRequest) {
        return modelMapper.map(userCreateRequest, User.class);
    }

    private UserDetails updateUserDetails(UserDetails toUpdate, UserDetails userDetailsRequest) {
        toUpdate = toUpdate == null ? new UserDetails() : toUpdate;
        modelMapper.map(userDetailsRequest, toUpdate);
        return toUpdate;
    }

}
