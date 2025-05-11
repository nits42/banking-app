package com.github.nits42.userservice.service.impl;

import com.github.nits42.userservice.clients.FileStorageClient;
import com.github.nits42.userservice.dto.CardDTO;
import com.github.nits42.userservice.dto.UserDTO;
import com.github.nits42.userservice.entities.Token;
import com.github.nits42.userservice.entities.User;
import com.github.nits42.userservice.entities.UserDetails;
import com.github.nits42.userservice.enums.Role;
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
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, LogoutHandler {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final FileStorageClient fileStorageClient;

    private final TokenRepository tokenRepository;

    @Override
    public String createUser(UserSignupRequest request) {
        log.info("User creation is started");

        isUsernamePresent(request, "User");
        isUsernameExist(request, "User");
        isEmailExist(request, "User");

        User userEntity = convertToEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRole(Role.ADMIN);
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
        return AppConstant.PROFILE_PHOTO_SAVED;
    }

    @Override
    public byte[] downloadImageFromFileSystem(String username) {
        log.info("User's profile photo download process is started.");
        String profilePicture = this.findByUsername(username).getUserDetails().getProfilePicture();

        if (profilePicture == null)
            throw BankingAppUserServiceException.builder()
                    .message(AppConstant.PROFILE_PHOTO_NOT_FOUND)
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();

        log.info("Calling file-storage-service for retrieving user's profile photo");
        byte[] profilePhoto = fileStorageClient.getProfilePhoto(profilePicture).getBody();
        log.info("Received response from file-storage-service");
        log.info("User's profile photo download process is completed.");
        return profilePhoto;
    }

    @Override
    public String updatePassword(UserPasswordUpdateRequest request) {
        log.info("Password update process is started");
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        BankingAppUserServiceException.builder()
                                .message(String.format(AppConstant.USER_NOT_FOUND_BY_USERNAME_OR_EMAIL_OR_PASSWORD, request.getUsername()))
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .build()
                );

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw BankingAppUserServiceException.builder()
                    .message(AppConstant.WRONG_CURRENT_PASSWORD)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password is updated successfully");
        return AppConstant.PASSWORD_UPDATED;
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
                log.info(AppConstant.FILE_UPLOAD_SUCCESS);
            } else {
                log.error(AppConstant.FILE_UPLOAD_FAILED);
                throw BankingAppUserServiceException.builder()
                        .message(AppConstant.FILE_UPLOAD_FAILED)
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }
        }
        log.info(AppConstant.FILE_UPLOAD_SUCCESS);
        return toUpdate;
    }

    @Override
    public String saveToken(TokenRequest request) {
        log.info("Token saving process is started");
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        BankingAppUserServiceException.builder()
                                .message(AppConstant.USER_NOT_FOUND_BY_USERNAME_OR_EMAIL)
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .build()
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
        log.info("Token saving process is completed");
        return AppConstant.TOKEN_SAVED;
    }

    private void revokeAllUserTokens(User user) {
        log.info("Retrieving all active tokens for user: {}", user.getUsername());
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            log.info("Revoking all valid tokens for user: {}", user.getUsername());
            if (!validUserTokens.isEmpty()) {
                validUserTokens.forEach(token -> {
                    token.setRevoked(true);
                });
                tokenRepository.saveAll(validUserTokens);
            }
            tokenRepository.saveAll(validUserTokens);
            log.info("All valid tokens revoked for user: {}", user.getUsername());
        } else
            log.info("No valid tokens found for user: {}", user.getUsername());
    }

    private void revokeUserToken(String token) {
        log.info("Revoking user token");
        var validUserToken = tokenRepository.findByTokenAndExpired(token, false)
                .orElseThrow(() ->
                        BankingAppUserServiceException.builder()
                                .message(AppConstant.ACTIVE_TOKEN_NOT_FOUND)
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .build()
                );
        validUserToken.setRevoked(true);
        tokenRepository.save(validUserToken);
        log.info("User token revoked successfully");
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        log.info("Extracting JWT token from request");
        if (request == null) {
            log.error("Request is null");
            throw BankingAppUserServiceException.builder()
                    .message(AppConstant.AUTHORIZATION_HEADER_MISSING)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        String bearerToken = request.getHeader(AppConstant.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(AppConstant.JWT_BEARER)) {
            return bearerToken.substring(AppConstant.JWT_BEARER.length());
        }
        log.error("JWT token is not found in request");
        throw BankingAppUserServiceException.builder()
                .message(AppConstant.JWT_TOKEN_MISSING)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication
    ) {
        log.info("Logout process is started");
        String token = extractJwtFromRequest(request);

        log.debug("Token extracted from request: {}", token);
        revokeUserToken(token);
        log.info("Logout process is completed");
    }

    @Override
    public UserDTO createCustomer(UserSignupRequest request) {
        log.info("Customer creation is started");

        isUsernamePresent(request, "Customer");
        isUsernameExist(request, "Customer");
        isEmailExist(request, "Customer");

        User userEntity = convertToEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRole(Role.USER);
        userEntity.setStatus(Status.ACTIVE);

        User savedCustomer = userRepository.save(userEntity);

        log.info("Customer creation is completed");
        return convertToDTO(savedCustomer);
    }

    private void isEmailExist(UserSignupRequest request, String type) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("{} creation failed: Email already exists", type);
            throw BankingAppUserServiceException.builder()
                    .message(AppConstant.EMAIL_TAKEN)
                    .httpStatus(HttpStatus.FOUND)
                    .build();
        }
    }

    private void isUsernameExist(UserSignupRequest request, String type) {
        if (userRepository.existsByUsername(request.getUsername())) {
            log.error("{} creation failed: Username already taken", type);
            throw BankingAppUserServiceException.builder()
                    .message(AppConstant.USERNAME_TAKEN)
                    .httpStatus(HttpStatus.FOUND)
                    .build();
        }
    }

    private void isUsernamePresent(UserSignupRequest request, String type) {
        if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
            log.error("{} creation failed: Missing required fields", type);
            throw BankingAppUserServiceException.builder()
                    .message(AppConstant.MISSING_REQUIRED_FIELD)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

}
