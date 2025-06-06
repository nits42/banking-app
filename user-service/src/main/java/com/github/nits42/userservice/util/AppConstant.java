package com.github.nits42.userservice.util;

public class AppConstant {

    public static final String USER_SERVICE_BASE_URL = "/v1/users";
    public static final String USER_SERVICE_ADDRESS_BASE_URL = "/v1/user/address";
    public static final String FILE_STORAGE_SERVICE_BASE_URL = "/v1/file-storage";
    public static final String USERNAME_TAKEN = "Username is already taken";
    public static final String EMAIL_TAKEN = "Email is already taken";
    public static final String USER_DELETED = "User deleted successfully";
    public static final String USER_UPDATED = "User updated successfully";
    public static final String USER_CREATED = "User created successfully";
    public static final String USER_NOT_FOUND_BY_ID = "User not found by id";
    public static final String USER_NOT_FOUND_BY_USERNAME = "User not found by username";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email";
    public static final String USER_NOT_FOUND_BY_USERNAME_OR_EMAIL = "User not found by username or email";
    public static final String USER_NOT_FOUND_BY_USERNAME_OR_EMAIL_OR_PASSWORD = "{} not found by username or email or password";
    public static final String LOGOUT_URL = "/v1/users/logout";
    public static final String ACTIVE_TOKEN_NOT_FOUND = "Active Token not found";
    public static final String FILE_UPLOAD_SUCCESS = "User profile photo uploading completed.";
    public static final String FILE_UPLOAD_FAILED = "User profile photo uploading failed.";
    public static final String WRONG_CURRENT_PASSWORD = "Current password is incorrect";
    public static final String PASSWORD_UPDATED = "Password updated successfully";
    public static final String PROFILE_PHOTO_SAVED = "User's profile photo is uploaded successfully.";
    public static final String TOKEN_SAVED = "Token stored successfully";
    public static final String PROFILE_PHOTO_NOT_FOUND = "Profile Photo is not available";
    public static final String USER_NOT_CREATED = "User not created";
    public static final String USER_NOT_UPDATED = "User not updated";
    public static final String USER_NOT_DELETED = "User not deleted";
    public static final String TOKEN_NOT_FOUND = "Token not found";
    public static final String TOKEN_NOT_VALID = "Token is not valid";
    public static final String TOKEN_NOT_EXPIRED = "Token is not expired";
    public static final String TOKEN_NOT_REVOKED = "Token is not revoked";
    public static final String TOKEN_NOT_SAVED = "Token not saved";
    public static final String TOKEN_NOT_DELETED = "Token not deleted";
    public static final String FILE_DOWNLOAD_FAILED = "User profile photo downloading failed.";
    public static final String FILE_DOWNLOAD_SUCCESS = "User profile photo downloading completed.";
    public static final String PASSWORD_NOT_UPDATED = "Password not updated";
    public static final String PASSWORD_NOT_MATCHED = "Password and confirm password not matched";

    public static final String ADDRESS_DELETED = "Address deleted successfully";
    public static final String ADDRESS_UPDATED = "Address updated successfully";
    public static final String ADDRESS_CREATED = "Address created successfully";
    public static final String ADDRESS_NOT_CREATED = "Address not created";
    public static final String ADDRESS_NOT_FOUND_BY_ID = "Address not found by id";
    public static final String ADDRESS_NOT_FOUND_BY_USER_ID = "Address not found by user id";
    public static final String ADDRESS_NOT_FOUND_BY_USER_USERNAME = "Address not found by user username";
    public static final String ADDRESS_NOT_FOUND_BY_USER_EMAIL = "Address not found by user email";
    public static final String ADDRESS_NOT_DELETED = "Address not deleted";
    public static final String ADDRESS_NOT_UPDATED = "Address not updated";
    public static final String ADDRESS_NOT_FOUND_BY_USER_USERNAME_OR_EMAIL = "Address not found by user username or email";
    public static final String ADDRESS_NOT_FOUND = "Address not found";
    public static final String ADDRESS_ALREADY_EXISTS = "Address already exists";

    public static final String MISSING_REQUIRED_FIELD = "Missing required field: ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ISSUER = "JWT";
    public static final String BEARER = "Bearer";
    public static final String BEARER_AUTH = "bearerAuth";
    public static final String JWT_BEARER = "Bearer ";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String TOKEN_EXPIRED = "Your token has expired, Please login again!!";
    public static final String AUTHORIZATION_HEADER_MISSING = "Authorization header is missing in request";
    public static final String JWT_TOKEN_MISSING = "JWT Token is missing or token not start with \"Bearer\" in request";
    public static final String ROLES = "Roles";
    public static final String LOGOUT_ERROR = "Your token has either expired or been revoked. Please sign in again to continue.";

}
