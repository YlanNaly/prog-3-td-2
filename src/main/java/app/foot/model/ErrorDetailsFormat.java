package app.foot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class ErrorDetailsFormat {

  private HttpStatus status;
  private String message;
  private List<String> errors;

  public ErrorDetailsFormat(HttpStatus status, String message, String error) {
    super();
    this.status = status;
    this.message = message;
    errors = Collections.singletonList(error);
  }
}
