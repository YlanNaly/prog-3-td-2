package integration;

import app.foot.FootApi;
import app.foot.controller.rest.Match;
import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.rest.Team;
import app.foot.controller.rest.TeamMatch;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
public class MatchIntegretionTest {

  @Autowired
  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
  private Match convertFromHttpResponse(MockHttpServletResponse response)
          throws JsonProcessingException, UnsupportedEncodingException {

    return objectMapper.readValue(
            response.getContentAsString(),
            Match.class);
  }
  static PlayerScorer scorer = rakotoModelScorer(
          playerModelRakoto(playerEntityRakoto(teamBarea())),
          scorerRakoto(playerEntityRakoto(teamBarea())));
  private static PlayerScorer rakotoModelScorer(Player playerModelRakoto, PlayerScoreEntity scorerRakoto) {
    return PlayerScorer.builder()
            .player(playerModelRakoto)
            .isOG(false)
            .scoreTime(scorerRakoto.getMinute())
            .build();
  }

  private static Team teamModelGhana(TeamEntity teamEntityGhana) {
    return Team.builder()
            .id(teamEntityGhana.getId())
            .name(teamEntityGhana.getName())
            .build();
  }

  private static Team teamModelBarea(TeamEntity teamEntityBarea) {
    return Team.builder()
            .id(teamEntityBarea.getId())
            .name(teamEntityBarea.getName())
            .build();
  }

  private static PlayerScoreEntity scorerRakoto(PlayerEntity playerEntityRakoto) {
    return PlayerScoreEntity.builder()
            .id(1)
            .player(playerEntityRakoto)
            .minute(10)
            .build();
  }

  private static Player playerModelRakoto(PlayerEntity playerEntityRakoto) {
    return Player.builder()
            .id(playerEntityRakoto.getId())
            .name(playerEntityRakoto.getName())
            .isGuardian(false)
            .build();
  }

  private static PlayerEntity playerEntityRakoto(TeamEntity teamEntityBarea) {
    return PlayerEntity.builder()
            .id(1)
            .name("Rakoto")
            .guardian(false)
            .team(teamEntityBarea)
            .build();
  }

  private static TeamEntity teamGhana() {
    return TeamEntity.builder()
            .id(2)
            .name("Ghana")
            .build();
  }

  private static TeamEntity teamBarea() {
    return TeamEntity.builder()
            .id(1)
            .name("Barea")
            .build();
  }

  private static Match match1(Match match){
    return Match.builder()
            .id(1)
            .teamA(TeamMatch.builder()
                    .team(match.getTeamA().getTeam())
                    .score(match.getTeamA().getScore())
                    .scorers(match.getTeamA().getScorers())
                    .build())
            .teamB(TeamMatch.builder()
                    .team(match.getTeamB().getTeam())
                    .score(match.getTeamB().getScore())
                    .scorers(match.getTeamB().getScorers())
                    .build())
            .datetime(match.getDatetime())
            .stadium(match.getStadium())
            .build();
  }
  @Test
  void get_match_by_id_ok() throws Exception {
    MockHttpServletResponse response = mockMvc
            .perform(get("/matches/"+1))
            .andReturn()
            .getResponse();
  Match actual = convertFromHttpResponse(response);
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(match1(actual) ,actual);
  }
}
