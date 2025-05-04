package com.github.nits42.apigateway.util;

public class AppConstant {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String TOKEN_EXPIRED = "Token is expired, Please login again!!";
    public static final String AUTHORIZATION_HEADER_MISSING = "Authorization header is missing in request";
    public static final String JWT_TOKEN_MISSING = "JWT Token is missing or token not start with \"Bearer\" in request";
    public static final String ROLES = "Roles";
    public static final String USER_ID = "userId";
    public static final String EMAIL = "email";
    public static final String USER_AGENT = "userAgent";
    public static final String IP = "ip";
    public static final String AUTH_SERVICE_BASE_URL = "/v1/auth";
    public static final String AUTH_SERVICE_API_DOC_URL = "/auth-service/v3/api-docs";
    public static final String USER_SERVICE_API_DOC_URL = "/user-service/v3/api-docs";
    public static final String API_DOCS_URLS = "/v3/api-docs/**";
    public static final String SWAGGER_UI_URLS = "/swagger-ui/**";
    public static final String SWAGGER_UI_HTML_URLS = "/swagger-ui.html";
    public static final String EUREKA_URL = "/eureka";
    public static final String ACTUATOR_HEALTH_URL = "/actuator/health";
    public static final String ACTUATOR_HEALTH_URLS = "/actuator/health/**";
    public static final String ACTUATOR_URLS = "/actuator/**";
    public static final String USER_SERVICE_UNAVAILABLE = "Sorry for inconvenience, User service is temporarily unavailable. Please try after some time!";
    public static final String SERVICE_UNAVAILABLE = "Service is temporarily unavailable. Please try again later.";


}
