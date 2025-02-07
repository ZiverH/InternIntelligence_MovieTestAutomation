package org.app.movie.exception;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex, WebRequest request){

        ErrorResponseDto error = ErrorResponseDto.builder().
                timestamp(OffsetDateTime.now()).
                code(HttpStatus.NOT_FOUND.value()).
                message("NOT FOUND EXCEPTION").
                detail(ex.getEntityName().toUpperCase()+" NOT FOUND").
                path(((ServletWebRequest) request).getRequest().getRequestURI()).
                build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request){


        List<ConstraintsViolationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ConstraintsViolationError(error.getField(), error.getDefaultMessage()))
                .toList();

        String errorFields = validationErrors.stream()
                .map(ConstraintsViolationError::getField)
                .collect(Collectors.joining(", "));

        ErrorResponseDto error = ErrorResponseDto.builder().
                timestamp(OffsetDateTime.now()).
                code(HttpStatus.BAD_REQUEST.value()).
                message("METHOD ARGUMENT NOT VALID").
                detail("Validation failed for one or more fields. Please check the input data.").
                path(((ServletWebRequest) request).getRequest().getRequestURI()).
                build();
        validationErrors.forEach(
                validation -> error.getData().put(validation.getField(), validation.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,WebRequest request){


        String detailMessage = "The request body is invalid or improperly formatted. " +
                "Ensure it is well-formed JSON or XML and matches the expected structure.";
        ErrorResponseDto error = ErrorResponseDto.builder().
                timestamp(OffsetDateTime.now()).
                code(HttpStatus.BAD_REQUEST.value()).
                message("Failed to parse the request body.").
                detail(detailMessage).
                path(((ServletWebRequest) request).getRequest().getRequestURI()).
                build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    @ExceptionHandler(DataNotDeleteableException.class)
    public ResponseEntity<ErrorResponseDto> handleDataNotDeleteableException(DataNotDeleteableException ex, WebRequest request){

        ErrorResponseDto error = ErrorResponseDto.builder().
                timestamp(OffsetDateTime.now()).
                code(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                message("Data not deleteable").
                detail(ex.getMessage()).
                path(((ServletWebRequest) request).getRequest().getRequestURI()).
                build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){

        ErrorResponseDto error = ErrorResponseDto.builder().
                timestamp(OffsetDateTime.now()).
                code(HttpStatus.INTERNAL_SERVER_ERROR.value()).
                message("Data integrity violation.").
                detail("DATA IS ALREADY EXIST").
                path(((ServletWebRequest) request).getRequest().getRequestURI()).
                build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }






}
