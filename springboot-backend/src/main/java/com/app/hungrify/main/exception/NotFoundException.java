package com.app.hungrify.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a requested entity (record) is not found in the database.
 * Example: restaurant, order, or user not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String entity, Object id) {
        super(String.format("%s with ID [%s] not found", entity, id));
    }
}
