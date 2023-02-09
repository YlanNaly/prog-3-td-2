package app.foot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ErrorDetailsFormat {
  private Instant timestamp;
  private String message;
  private String details;
}
