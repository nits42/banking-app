package com.github.nits42.userservice.service;

import com.github.nits42.userservice.dto.UserDTO;
import com.github.nits42.userservice.request.UserCreateRequest;
import com.github.nits42.userservice.request.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String createUser(UserCreateRequest request);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(String id);

    UserDTO getUserByUsername(String username);

    UserDTO getUserByEmail(String email);

    String updateUser(String id, UserUpdateRequest request);

    String deleteUser(String id);

}
