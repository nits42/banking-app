package com.github.nits42.userservice.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
public class BankingAppUserServiceException extends RuntimeException{

    private String message;
    private HttpStatus httpStatus;

    public BankingAppUserServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
