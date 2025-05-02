package com.github.nits42.userservice.service;

import com.github.nits42.userservice.dto.CardDTO;
import com.github.nits42.userservice.dto.UserDTO;
import com.github.nits42.userservice.request.TokenRequest;
import com.github.nits42.userservice.request.UserPasswordUpdateRequest;
import com.github.nits42.userservice.request.UserSignupRequest;
import com.github.nits42.userservice.request.UserUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String createUser(UserSignupRequest request);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(UUID id);

    UserDTO getUserByUsername(String username);

    UserDTO getUserByEmail(String email);

    String updateUser(UUID id, UserUpdateRequest request);

    String deleteUser(UUID id);

    String uploadUserProfilePhotoById(@Valid MultipartFile file, UUID id);

    byte[] downloadImageFromFileSystem(String username);

    String updatePassword(@Valid UserPasswordUpdateRequest request);

    List<CardDTO> getAllCardsByUsername(String username);

    String saveToken(TokenRequest request);

}
