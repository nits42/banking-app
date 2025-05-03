package com.github.nits42.userservice.service.impl;

import com.github.nits42.userservice.clients.FileStorageClient;
import com.github.nits42.userservice.dto.CardDTO;
import com.github.nits42.userservice.dto.UserDTO;
import com.github.nits42.userservice.entities.Token;
import com.github.nits42.userservice.entities.User;
import com.github.nits42.userservice.entities.UserDetails;
import com.github.nits42.userservice.enums.Status;
import com.github.nits42.userservice.enums.TokenType;
import com.github.nits42.userservice.exceptions.BankingAppUserServiceException;
import com.github.nits42.userservice.exceptions.ResourceNotFoundException;
import com.github.nits42.userservice.repository.TokenRepository;
import com.github.nits42.userservice.repository.UserRepository;
import com.github.nits42.userservice.request.TokenRequest;
import com.github.nits42.userservice.request.UserPasswordUpdateRequest;
import com.github.nits42.userservice.request.UserSignupRequest;
import com.github.nits42.userservice.request.UserUpdateRequest;
import com.github.nits42.userservice.service.UserService;
import com.github.nits42.userservice.util.AppConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final FileStorageClient fileStorageClient;

    private final TokenRepository tokenRepository;

    @Override
    public String createUser(UserSignupRequest request) {
        log.info("User creation is started");

        if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
            log.error("User creation failed: Missing required fields");
            throw BankingAppUserServiceException.builder()
                    .message(AppConstant.MISSING_REQUIRED_FIELD)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            log.error("User creation failed: Username already exists");
            throw BankingAppUserServiceException.builder()
                    .message(AppConstant.USERNAME_TAKEN)
                    .httpStatus(HttpStatus.FOUND)
                    .build();
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("User creation failed: Email already exists");
            throw BankingAppUserServiceException.builder()
                    .message(AppConstant.EMAIL_TAKEN)
                    .httpStatus(HttpStatus.FOUND)
                    .build();
        }
        User userEntity = convertToEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setStatus(Status.ACTIVE);

        userRepository.save(userEntity);

        log.info("User creation is completed");
        return AppConstant.USER_CREATED;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findByStatus(Status.ACTIVE)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public UserDTO getUserById(UUID id) {
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
    public String updateUser(UUID id, UserUpdateRequest request) {
        log.info("User's details update process is started");
        User userToUpdate = findById(id);

        request.setUserDetailsRequest(updateUserDetails(userToUpdate.getUserDetails(), request.getUserDetailsRequest()));
        modelMapper.map(request, userToUpdate);
        userRepository.save(userToUpdate);
        log.info("User's details update process is completed");
        return AppConstant.USER_UPDATED;
    }

    @Override
    public String deleteUser(UUID id) {
        log.info("User deletion process is started");
        User user = findById(id);
        user.setStatus(Status.DELETED);
        userRepository.save(user);
        log.info("User deletion process is completed");
        return AppConstant.USER_DELETED;
    }

    private User findByUsername(String username) {
        return userRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> BankingAppUserServiceException.builder()
                        .message(AppConstant.USER_NOT_FOUND_BY_USERNAME + ": " + username)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    private User findByEmail(String email) {
        return userRepository.findByEmailAndStatus(email, Status.ACTIVE)
                .orElseThrow(() -> BankingAppUserServiceException.builder()
                        .message(AppConstant.USER_NOT_FOUND_BY_EMAIL + ": " + email)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    private User findById(UUID id) {
        return userRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> BankingAppUserServiceException.builder()
                        .message(AppConstant.USER_NOT_FOUND_BY_ID + ": " + id)
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build());
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private User convertToEntity(UserSignupRequest userCreateRequest) {
        return modelMapper.map(userCreateRequest, User.class);
    }

    private UserDetails updateUserDetails(UserDetails toUpdate, UserDetails userDetailsRequest) {
        toUpdate = toUpdate == null ? new UserDetails() : toUpdate;
        modelMapper.map(userDetailsRequest, toUpdate);
        return toUpdate;
    }

    @Override
    public String uploadUserProfilePhotoById(MultipartFile file, UUID id) {
        log.info("User's Profile photo upload process is started.");
        User toUpdate = findById(id);
        toUpdate = uploadUserProfilePhoto(toUpdate, file);
        userRepository.save(toUpdate);
        log.info("User's profile photo upload process is completed.");
        return "User's profile photo is uploaded successfully.";
    }

    @Override
    public byte[] downloadImageFromFileSystem(String username) {
        String profilePicture = this.findByUsername(username).getUserDetails().getProfilePicture();

        if (profilePicture == null)
            throw BankingAppUserServiceException.builder()
                    .message("Profile Photo is not available")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();

        log.info("Calling file-storage-service for retrieving user's profile photo");
        byte[] profilePhoto = fileStorageClient.getProfilePhoto(profilePicture).getBody();
        log.info("Received response from file-storage-service");

        return profilePhoto;
    }

    @Override
    public String updatePassword(UserPasswordUpdateRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw BankingAppUserServiceException.builder()
                    .message("Current password is incorrect")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password is updated successfully");
        return "Password updated successfully";
    }

// FIXME - Wrong Card-Type value

    //    @Override
    public List<CardDTO> getAllCardsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

//        Set<Account> accounts = user.getUserDetails().getAccounts();
//
//        List<Card> cardList = accounts
//                .stream()
//                .map(Account::getCards)
//                .flatMap(Collection::stream)
//                .filter(card -> card.getStatus().equals(Status.ACTIVE))
//                .toList();
//
//        return cardList.stream()
//                .map(card -> {
//                           modelMapper.map(card, CardDTO.class)
//                            return CardDTO.builder()
//                                    .cvv(card.getCvv())
//                                    .cardExpiry(card.getCardExpiry())
//                                    .cardNumber(card.getCardNumber())
//                                    .cardType(card.getCardType().toString())
//                                    .cardHolderName(card.getCardHolderName())
//                                    .provider(card.getProvider().toString())
//                                    .build();
//                        }
//                )
//                .toList();
        return null;
    }

    public User uploadUserProfilePhoto(User toUpdate, MultipartFile file) {
        log.info("User profile photo uploading started.");
        if (file != null) {
            log.info("Calling file storage api.");
            String profilePictureId = fileStorageClient.uploadImageToFileSystem(file).getBody();
            log.info("Response from File Storage service : {}", profilePictureId);
            if (profilePictureId != null) {
                toUpdate.getUserDetails().setProfilePicture(profilePictureId);
                log.info("User profile photo uploading completed.");
            }
        }
        return toUpdate;
    }

    @Override
    public String saveToken(TokenRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with username: " + request.getUsername())
                );

        revokeAllUserTokens(user);
        Token token = Token.builder()
                .token(request.getToken())
                .user(user)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
        return "Token stored successfully";
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            log.info("Revoking all user tokens");
            if (validUserTokens.size() > 0) {
                validUserTokens.forEach(token -> {
                    token.setRevoked(true);
                });
                tokenRepository.saveAll(validUserTokens);
            }
            tokenRepository.saveAll(validUserTokens);
        } else
            log.info("No valid tokens found for user: {}", user.getUsername());
    }

    private void revokeUserToken(String token) {
        var validUserToken = tokenRepository.findByTokenAndExpired(token, false)
                .orElseThrow(() ->
                        new ResourceNotFoundException("No active token is found")
                );
        validUserToken.setRevoked(true);
        tokenRepository.save(validUserToken);
    }

    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String token = extractJwtFromRequest(request);
        if (token != null) revokeUserToken(token);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AppConstant.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(AppConstant.JWT_BEARER)) {
            return bearerToken.substring(AppConstant.JWT_BEARER.length());
        }
        return null;
    }

}
