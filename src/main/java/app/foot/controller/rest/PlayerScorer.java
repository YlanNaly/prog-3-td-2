package app.foot.controller.rest;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerScorer {
  private Player player;
  private Integer scoreTime;
  private Boolean isOG;
}
