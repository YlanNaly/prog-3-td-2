package app.foot.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class PlayerScorer {
    private Player player;
    private Integer minute;
    private Boolean isOwnGoal;
}
