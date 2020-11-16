package com.lens.epay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by Emir Gökdemir
 * on 20 Şub 2020
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends ResponseStatusException {

    public BadRequestException(@Nullable String statusText) {
        super(HttpStatus.BAD_REQUEST, statusText);
    }
}
