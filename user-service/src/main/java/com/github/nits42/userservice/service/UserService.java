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

public interface UserService {

    String createUser(UserSignupRequest request);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(String id);

    UserDTO getUserByUsername(String username);

    UserDTO getUserByEmail(String email);

    String updateUser(String id, UserUpdateRequest request);

    String deleteUser(String id);

    String uploadUserProfilePhotoById(@Valid MultipartFile file, String id);

    byte[] downloadImageFromFileSystem(String username);

    String updatePassword(@Valid UserPasswordUpdateRequest request);

    List<CardDTO> getAllCardsByUsername(String username);

    String saveToken(TokenRequest request);

}
