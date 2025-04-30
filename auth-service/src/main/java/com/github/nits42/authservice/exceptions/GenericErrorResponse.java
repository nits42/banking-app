package com.github.nits42.authservice.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class GenericErrorResponse extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;

    public GenericErrorResponse(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
