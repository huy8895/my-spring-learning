package com.example.uploadbucket.exception;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@RestControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        log.debug("test");
        System.out.println("test handleMethodArgumentNotValid----------------- = ");
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult()
                                  .getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult()
                                   .getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = ApiError.builder()
                                    .status(HttpStatus.BAD_REQUEST)
                                    .message(ex.getLocalizedMessage())
                                    .errors(errors)
                                    .build();
        return ResponseEntity.badRequest()
                             .headers(headers)
                             .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        log.debug("test");
        System.out.println("test handleMissingServletRequestParameter----------------- = ");
        String error = ex.getParameterName() + " parameter is missing";

        ApiError apiError = ApiError.builder()
                                    .status(HttpStatus.BAD_REQUEST)
                                    .errors(Collections.singletonList(error))
                                    .message(ex.getLocalizedMessage())
                                    .build();
        return ResponseEntity.badRequest()
                             .headers(headers)
                             .body(apiError);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ApiError handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass()
                                .getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        return ApiError.builder()
                       .status(HttpStatus.BAD_REQUEST)
                       .message(ex.getLocalizedMessage())
                       .errors(errors)
                       .build();
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ApiError handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType()
                                                         .getName();

        return ApiError.builder()
                       .status(HttpStatus.BAD_REQUEST)
                       .errors(Collections.singletonList(error))
                       .message(ex.getLocalizedMessage())
                       .build();

    }

    @ExceptionHandler(AmazonS3Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCustomException(AmazonS3Exception e, HttpServletRequest request) {
        return ApiError.builder()
                       .status(HttpStatus.NOT_FOUND)
                       .errors(Collections.singletonList(HttpStatus.NOT_FOUND.getReasonPhrase()))
                       .message(e.getErrorMessage())
                       .build();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleCustomException(Throwable e, HttpServletRequest request) {
        return ApiError.builder()
                       .status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .errors(Collections.singletonList(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
                       .message(e.getMessage())
                       .build();
    }
}
