package com.depromeet.common;

import com.depromeet.dto.response.ApiResponse;
import com.depromeet.exception.BaseException;
import com.depromeet.type.common.CommonErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintDefinitionException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException ex) {
        Errors errors = ex.getBindingResult();
        Map<String, String> validateDetails = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validateDetails.put(validKeyName, error.getDefaultMessage());
        }
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.REQUEST_VALIDATION, 400, validateDetails),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ApiResponse<?>> handleUnexpectedTypeException(
            final UnexpectedTypeException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.INVALID_TYPE, 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handlerMethodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.INVALID_TYPE, 400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<?>> handlerMissingRequestHeaderException(
            final MissingRequestHeaderException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.INVALID_MISSING_HEADER, 400),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handlerHttpMessageNotReadableException(
            final HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.INVALID_HTTP_REQUEST, 400),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintDefinitionException.class)
    protected ResponseEntity<ApiResponse<?>> handlerConstraintDefinitionException(
            final ConstraintDefinitionException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.VALIDATION_FAILED, 400, ex.toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiResponse<?>> handlerConstraintViolationException(
            final ConstraintViolationException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.VALIDATION_FAILED, 400, ex.toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handlerHttpRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.METHOD_NOT_ALLOWED, 405),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    /** 500 INTERNEL_SERVER */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(
            final Exception ex, final HttpServletRequest request) throws IOException {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.INTERNAL_SERVER, 500),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handlerIllegalArgumentException(
            final IllegalArgumentException ex, final HttpServletRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.INTERNAL_SERVER, 500),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<?>> handlerIoException(
            final IOException ex, final HttpServletRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.INTERNAL_SERVER, 500),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handlerRuntimeException(
            final RuntimeException ex, final HttpServletRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.fail(CommonErrorType.INTERNAL_SERVER, 500),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /** CUSTOM */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(BaseException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ApiResponse.fail(ex), ex.getHttpStatus());
    }
}
