package com.example.watchex.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<MessageEntity> processSecurityError(Exception ex) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, "Bạn không có quyền thực hiện chức năng này!", MessageType.ERROR);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
    }

    @ExceptionHandler({ValidException.class})
    protected ResponseEntity<MessageEntity> handleValid(final RuntimeException ex) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, 400, ex.getMessage(), MessageType.ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    @ExceptionHandler({BindException.class})
    protected ResponseEntity<MessageEntity> handleBindException(BindException e, Locale locale) {
        String errorMessage = "Tham số không hợp lệ!";
        if (e.getBindingResult().hasErrors())
            errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageEntity(400, errorMessage));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<MessageEntity> handleNotFound(final RuntimeException ex) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity((String) null, ex.getMessage(), MessageType.ERROR);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class, DataAccessException.class})
    protected ResponseEntity<MessageEntity> handleConflict(final RuntimeException ex, Locale locale) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, 500, "409 Conflict", MessageType.ERROR);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<MessageEntity> handleHttpRequestNotSupported(RuntimeException ex) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, 400, ex.getMessage(), MessageType.ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<MessageEntity> handleInternal(final RuntimeException ex, Locale locale) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, 500, ex.getMessage(), MessageType.ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    private MessageEntity processFieldError(FieldError error, Locale locale) {
        MessageEntity message = null;
        if (error != null) {
            String msg = error.getDefaultMessage();
            message = new MessageEntity(error.getField(), 400, msg, MessageType.ERROR);
        }

        return message;
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<MessageEntity> handleAllException(Exception ex) {
        ex.printStackTrace();
        // todos: log database
        MessageEntity msg = new MessageEntity(null, ex.getMessage(), MessageType.ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }
}
