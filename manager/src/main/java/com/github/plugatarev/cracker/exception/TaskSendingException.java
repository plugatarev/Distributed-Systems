package com.github.plugatarev.cracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class TaskSendingException extends RuntimeException {

    public TaskSendingException(Throwable cause) {
        super("Error occurred while sending cracking tasks to workers", cause);
    }
}
