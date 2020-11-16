package com.lens.epay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Created by Emir Gökdemir
 * on 9 May 2020
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundException extends ResponseStatusException {

    public NotFoundException(@Nullable String statusText) {
        super(HttpStatus.NOT_FOUND, statusText);
    }
}
