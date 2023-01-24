package app.foot.repository.mapper;

import app.foot.model.Player;
import app.foot.model.PlayerScorer;
import app.foot.repository.MatchRepository;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PlayerMapper {
  private final MatchRepository matchRepository;
  private final PlayerRepository playerRepository;


  public Player toDomain(PlayerEntity entity) {
    return Player.builder()
        .id(entity.getId())
        .name(entity.getName())
        .isGuardian(entity.isGuardian())
        .teamName(entity.getTeam().getName())
        .build();
  }

  public PlayerScorer toDomain(PlayerScoreEntity entity) {
    return PlayerScorer.builder()
        .player(toDomain(entity.getPlayer()))
        .minute(entity.getMinute())
        .isOwnGoal(entity.isOwnGoal())
        .build();
  }

  public PlayerScoreEntity toEntity(int matchId, PlayerScorer scorer) {
    Optional<PlayerEntity> playerScoreEntity = playerRepository.findById(scorer.getPlayer().getId());
    Optional<MatchEntity> matchEntity = matchRepository.findById(matchId);
    return PlayerScoreEntity.builder()
        .player(PlayerEntity.builder()
                .team( playerScoreEntity.isPresent() ? playerScoreEntity.get().getTeam() : null)
                .id(scorer.getPlayer().getId())
                .guardian(playerScoreEntity.get().isGuardian())
                .name(playerScoreEntity.get().getName())
                .build())
        .match(MatchEntity.builder()
                .id(matchId)
                .scorers(playerScoreEntity.isPresent() ? matchEntity.get().getScorers() : null)
                .teamA(matchEntity.get().getTeamA())
                .teamB(matchEntity.get().getTeamB())
                .datetime(matchEntity.get().getDatetime())
                .stadium(matchEntity.get().getStadium())
                .build())
        .ownGoal(scorer.getIsOwnGoal())
        .minute(scorer.getMinute())
        .build();
  }
}
