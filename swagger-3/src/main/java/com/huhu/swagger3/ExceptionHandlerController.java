package com.huhu.swagger3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ExceptionHandlerController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> problem(final Throwable e) {
        String message = e.getMessage();
        //might actually prefer to use a geeric mesasge

        UUID uuid = UUID.randomUUID();
        String logRef = uuid.toString();
        logger.error("logRef=" + logRef, message, e);
        return new ResponseEntity<ApiError>(new ApiError(logRef, message), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
