package app.foot.controller.rest;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@EqualsAndHashCode
public class TeamMatch {
  private Team team;
  private Integer score;
  private List<PlayerScorer> scorers;
}
