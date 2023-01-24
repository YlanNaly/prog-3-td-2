package app.foot.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PlayerScorer {
  private Player player;
  private Integer scoreTime;
  private Boolean isOG;
}
