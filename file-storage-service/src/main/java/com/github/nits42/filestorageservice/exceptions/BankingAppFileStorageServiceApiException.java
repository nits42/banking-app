package com.github.nits42.filestorageservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Author: Nitin Kumar
 * Date:10/05/25
 * Time:21:08
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankingAppFileStorageServiceApiException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public BankingAppFileStorageServiceApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
