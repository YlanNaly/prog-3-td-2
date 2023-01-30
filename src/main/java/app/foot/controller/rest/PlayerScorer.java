package app.foot.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Setter
public class PlayerScorer {
  private Player player;
  private Integer scoreTime;
  private Boolean isOG;
}
