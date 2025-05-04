package com.github.nits42.apigateway.filter;

import com.github.nits42.apigateway.util.AppConstant;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> apiEndpoints = List.of(
            AppConstant.AUTH_SERVICE_BASE_URL + "/login",
            AppConstant.AUTH_SERVICE_BASE_URL + "/signin",
            AppConstant.AUTH_SERVICE_BASE_URL + "/register",
            AppConstant.AUTH_SERVICE_BASE_URL + "/signup",
            AppConstant.AUTH_SERVICE_BASE_URL + "/signout",
            AppConstant.AUTH_SERVICE_BASE_URL + "/logout",
            AppConstant.AUTH_SERVICE_API_DOC_URL,
            AppConstant.USER_SERVICE_API_DOC_URL,
            AppConstant.API_DOCS_URLS,
            AppConstant.SWAGGER_UI_URLS,
            AppConstant.SWAGGER_UI_HTML_URLS,
            AppConstant.AUTH_SERVICE_BASE_URL + "/actuator",
            AppConstant.AUTH_SERVICE_BASE_URL + "/actuator/**",
            AppConstant.USER_SERVICE_API_DOC_URL + "/**",
            AppConstant.AUTH_SERVICE_API_DOC_URL + "/**",
            AppConstant.AUTH_SERVICE_BASE_URL + "/**",
            AppConstant.ACTUATOR_HEALTH_URL,
            AppConstant.ACTUATOR_HEALTH_URLS,
            AppConstant.ACTUATOR_URLS,
            AppConstant.EUREKA_URL
    );

    public Predicate<ServerHttpRequest> isApiSecured =
            request -> apiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}