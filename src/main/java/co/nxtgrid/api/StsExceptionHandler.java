package co.nxtgrid.api;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import co.nxtgrid.token.exceptions.InvalidRangeException;

@RestControllerAdvice
public class StsExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(StsExceptionHandler.class);
    private static final String ISSUE_DATE_FORMAT_MESSAGE =
        "issueDate must be an ISO 8601 datetime, e.g. \"2024-03-15T10:30:00\" or "
            + "\"2026-07-07T10:12:54.289Z\" (time zone offset ignored)";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            return new ErrorResponse(fieldError.getDefaultMessage(), fieldError.getField());
        }
        return new ErrorResponse("Validation failed", null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMalformedJson(HttpMessageNotReadableException ex) {
        return new ErrorResponse(messageForMalformedJson(ex), null);
    }

    @ExceptionHandler(InvalidRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainRange(InvalidRangeException ex) {
        return new ErrorResponse(ex.getMessage(), null);
    }

    @ExceptionHandler(UnsupportedTokenTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnsupportedType(UnsupportedTokenTypeException ex) {
        return new ErrorResponse(ex.getMessage(), "type");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return new ErrorResponse("Method not allowed for this endpoint", null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NoResourceFoundException ex) {
        return new ErrorResponse("Not found", null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected(Exception ex) {
        log.error("Token generation failed", ex);
        return new ErrorResponse("An unexpected error occurred during token generation", null);
    }

    private static String messageForMalformedJson(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormat) {
            if (invalidFormat.getTargetType() == TokenType.class) {
                return "type must be one of: TOP_UP_KWH, TOP_UP (deprecated alias), "
                    + "CLEAR_CREDIT, CLEAR_TAMPER, SET_POWER_LIMIT";
            }
            if (invalidFormat.getTargetType() == LocalDateTime.class || isField(invalidFormat, "issueDate")) {
                return ISSUE_DATE_FORMAT_MESSAGE;
            }
            if (isField(invalidFormat, "randomNumber")) {
                return "randomNumber must be an integer between 0 and 15";
            }
        }
        if (cause instanceof JsonMappingException jsonMapping) {
            if (isField(jsonMapping, "issueDate")) {
                return ISSUE_DATE_FORMAT_MESSAGE;
            }
            if (isField(jsonMapping, "randomNumber")) {
                return "randomNumber must be an integer between 0 and 15";
            }
        }
        return "Malformed JSON request";
    }

    private static boolean isField(JsonMappingException exception, String fieldName) {
        return !exception.getPath().isEmpty()
            && fieldName.equals(exception.getPath().get(0).getFieldName());
    }
}
