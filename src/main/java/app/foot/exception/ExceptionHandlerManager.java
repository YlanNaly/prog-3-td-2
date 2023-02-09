package app.foot.exception;

import app.foot.model.ErrorDetailsFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class ExceptionHandlerManager extends ResponseEntityExceptionHandler {
  @ExceptionHandler({ BadRequestException.class })
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
          BadRequestException ex, WebRequest request) {
    String error =
            ex.getMessage() + " should be of type " + ex.getCause();

    ErrorDetailsFormat apiError =
            new ErrorDetailsFormat(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
    return new ResponseEntity<>(
            apiError, new HttpHeaders(), apiError.getStatus());
  }
  @ExceptionHandler({ Exception.class })
  public ResponseEntity<Object> handleMethodServerError(
          Exception ex, WebRequest request) {
    String error =
            request.getHeader(String.valueOf(ex.getMessage()));

    ErrorDetailsFormat apiError =
            new ErrorDetailsFormat(HttpStatus.INTERNAL_SERVER_ERROR , ex.getLocalizedMessage(), error);
    return new ResponseEntity<>(
            apiError, new HttpHeaders(), apiError.getStatus());
  }
}
