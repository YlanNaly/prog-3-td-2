  import app.foot.model.PlayerScorer;
  import app.foot.model.Team;
  import app.foot.repository.MatchRepository;
  import app.foot.repository.PlayerRepository;
  import app.foot.model.Player;
  import app.foot.repository.entity.MatchEntity;
  import app.foot.repository.entity.PlayerEntity;
  import app.foot.repository.entity.PlayerScoreEntity;
  import app.foot.repository.entity.TeamEntity;
  import app.foot.repository.mapper.PlayerMapper;
  import app.foot.repository.mapper.TeamMapper;
  import lombok.extern.slf4j.Slf4j;
  import org.junit.jupiter.api.Test;
  import org.springframework.boot.test.mock.mockito.MockBean;

  import java.util.List;
  import java.util.Optional;

  import static org.junit.jupiter.api.Assertions.assertEquals;
  import static org.mockito.ArgumentMatchers.any;
  import static org.mockito.Mockito.mock;
  import static org.mockito.Mockito.when;


  //TODO-2: complete these tests
  @Slf4j
  public class PlayerMapperTest {

      TeamMapper teamMapper = mock(TeamMapper.class);

      PlayerMapper playerMapper = mock(PlayerMapper.class);

      @MockBean
      MatchRepository matchRepository = mock(MatchRepository.class);
      @MockBean
      PlayerRepository playerRepository = mock(PlayerRepository.class);
      PlayerMapper subject = new PlayerMapper(matchRepository ,playerRepository);
      @Test
      void player_to_domain_ok() {

          Team team = teamModelGhana(teamGhana());
          Player entity = playerModelOne(playerEntityTwo(teamGhana()));

          when(playerMapper.toDomain((PlayerEntity) any())).thenReturn(playerModelOne(playerOne()));
          when(teamMapper.toDomain(teamGhana())).thenReturn(team);

          Player expected = Player.builder()
                  .name(entity.getName())
                  .isGuardian(entity.getIsGuardian())
                  .teamName(team.getName())
                  .build();


          Player actual = subject.toDomain(playerOne());

          assertEquals(expected, actual);
      }
      private static Player playerModelOne(PlayerEntity playerEntityOne) {
          return Player.builder()
                  .id(playerEntityOne.getId())
                  .name(playerEntityOne.getName())
                  .isGuardian(playerEntityOne.isGuardian())
                  .build();
      }
    private static Player playerScorer(PlayerScoreEntity playerEntityOne) {
      return Player.builder()
              .id(playerEntityOne.getId())
              .name(playerEntityOne.getPlayer().getName())
              .teamName(playerEntityOne.getPlayer().getTeam().getName())
              .isGuardian(playerEntityOne.getPlayer().isGuardian())
              .build();
    }
      private static PlayerEntity playerEntityTwo(TeamEntity teamEntityTwo) {
          return PlayerEntity.builder()
                  .name(playerOne().getName())
                  .guardian(false)
                  .team(teamEntityTwo)
                  .build();
      }
      private static PlayerScoreEntity scorerOne(PlayerEntity playerEntityRakoto) {
          return PlayerScoreEntity.builder()
                  .player(playerEntityRakoto)
                  .minute(null)
                  .build();
      }
    private static PlayerEntity playerOne() {
      return PlayerEntity.builder()
              .name("Rakoto")
              .guardian(false)
              .team(teamGhana())
              .build();
    }
    private static PlayerScorer secondModelScorer(Player player, PlayerScoreEntity scorerOne) {
      return PlayerScorer.builder()
              .player(player)
              .isOwnGoal(false)
              .minute(scorerOne.getMinute())
              .build();
    }
    private static TeamEntity teamGhana() {
      return TeamEntity.builder()
              .id(2)
              .name("Ghana")
              .build();
    }
    private static Team teamModelGhana(TeamEntity teamEntityGhana) {
      return Team.builder()
              .id(teamEntityGhana.getId())
              .name(teamEntityGhana.getName())
              .build();
    }
    private static TeamEntity teamBarea() {
      return TeamEntity.builder()
              .id(1)
              .name("Barea")
              .build();
    }
    private static MatchEntity matching(){
        return MatchEntity.builder()
                .id(1)
                .datetime(null)
                .stadium(null)
                .teamB(teamBarea())
                .teamA(teamGhana())
                .scorers(List.of())
                .build();
    }

      @Test
      void player_scorer_to_domain_ok() {
        PlayerScoreEntity playerScorerEntity = scorerOne(playerOne());
        Team team = teamModelGhana(teamGhana());

        when(playerMapper.toDomain(playerScorerEntity)).thenReturn(PlayerScorer.builder().build());
        when(teamMapper.toDomain(teamGhana())).thenReturn(team);

        PlayerScorer expected = PlayerScorer.builder()
                .isOwnGoal(playerScorerEntity.isOwnGoal())
                .player(playerScorer(playerScorerEntity))
                .minute(playerScorerEntity.getMinute())
                .build();
        PlayerScorer actual   = subject.toDomain(playerScorerEntity);

        assertEquals(expected , actual);
      }

      @Test
      void player_scorer_to_entity_ok() {
        PlayerScoreEntity playerScorerEntity = scorerOne(playerOne());
        PlayerScorer playerScorer = secondModelScorer(playerScorer(playerScorerEntity),playerScorerEntity);

        when(matchRepository.findById(any())).thenReturn(Optional.ofNullable(matching()));
        when(playerRepository.findById(any())).thenReturn(Optional.ofNullable(playerOne()));

        PlayerScoreEntity expected = PlayerScoreEntity.builder()
                .id(playerScorerEntity.getId())
                .match(matching())
                .player(playerScorerEntity.getPlayer())
                .build();

        PlayerScoreEntity actual = subject.toEntity(1,playerScorer);

        assertEquals(expected , actual);
      }
  }
