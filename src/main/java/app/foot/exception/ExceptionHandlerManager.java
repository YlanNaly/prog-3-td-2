package app.foot.exception;

import app.foot.model.ErrorDetailsFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class ExceptionHandlerManager {

  @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
  public ResponseEntity<ErrorDetailsFormat> NotFoundExceptionHandling(ChangeSetPersister.NotFoundException exception, WebRequest request){
    ErrorDetailsFormat errorDetailsFormat =
            new ErrorDetailsFormat(new Date().toInstant(), exception.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetailsFormat, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorDetailsFormat> BadRequestExceptionHandling(BadRequestException exception, WebRequest request){
    ErrorDetailsFormat errorDetailsFormat =
            new ErrorDetailsFormat(new Date().toInstant(), exception.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetailsFormat, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetailsFormat> GlobalExceptionHandling(Exception exception, WebRequest request){
    ErrorDetailsFormat errorDetailsFormat =
            new ErrorDetailsFormat(new Date().toInstant(), exception.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(errorDetailsFormat, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
