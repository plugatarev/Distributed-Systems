package com.github.plugatarev.cracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ResultSendingException extends RuntimeException {

    public ResultSendingException(Throwable cause) {
        super("Error occurred while sending result cracking to manager", cause);
    }
}
