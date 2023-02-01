package integration;

import app.foot.FootApi;
import app.foot.controller.rest.Match;
import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.rest.TeamMatch;
import app.foot.controller.validator.GoalValidator;
import app.foot.exception.BadRequestException;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.ServletException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
@Transactional
public class MatchIntegretionTest {

  @Autowired
  private MockMvc mockMvc;
  StringBuilder exceptionBuilder = new StringBuilder();
  @MockBean
  GoalValidator validator;
  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
  private Match convertFromHttpResponse(MockHttpServletResponse response)
          throws JsonProcessingException, UnsupportedEncodingException {

    return objectMapper.readValue(
            response.getContentAsString(),
            Match.class);
  }
  private PlayerScorer convertFromHttpResponseToPlayer(MockHttpServletResponse response)
          throws JsonProcessingException, UnsupportedEncodingException {
    return objectMapper.readValue(
            response.getContentAsString(),
            PlayerScorer.class);
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
  private static Match matchNotFound(){
    return Match.builder()
            .id(100)
            .teamA(TeamMatch.builder()
                    .build())
            .teamB(TeamMatch.builder()
                    .build())
            .build();
  }
    PlayerScorer playerScorer(){
    return PlayerScorer.builder()
            .scoreTime(30)
            .player(Player.builder()
                    .id(1)
                    .teamName("E1")
                    .name("J1")
                    .isGuardian(false)
                    .build())
            .isOG(false)
            .build();
  }
  private static PlayerScorer playerScoringInLess0(){
    return PlayerScorer.builder()
            .scoreTime(-1)
            .player(Player.builder()
                    .id(7)
                    .isGuardian(true)
                    .build())
            .isOG(false)
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

  @Test
  void get_match_by_id_ko() throws Exception {
    exceptionBuilder.append("Match#").append(matchNotFound().getId()).append(" not found.");
    ServletException error = assertThrows(ServletException.class , () -> mockMvc
            .perform(get("/matches/"+matchNotFound().getId() ,exceptionBuilder.toString())
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse());
    assertEquals(exceptionBuilder.toString() , error.getRootCause().getMessage());
  }

  @Test
  void post_match_adding_goal_ok() throws Exception {
    MockHttpServletResponse response = mockMvc
            .perform(post("/matches/"+3+"/goals")
                    .content(objectMapper.writeValueAsString(List.of(playerScorer())))
                    .contentType("application/json")
                    .accept("application/json"))
            .andReturn()
            .getResponse();

    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(54 , convertFromHttpResponse(response).getTeamA().getScore());
  }

  @Test
  void post_match_adding_goal_ko() throws Exception {
    mockMvc.perform(post("/matches/"+3 +"/goals", exceptionBuilder.toString())
             .content(List.of(playerScoringInLess0()).toString())
             .contentType(MediaType.APPLICATION_JSON)
             .accept(MediaType.APPLICATION_JSON))
             .andExpect(status().isBadRequest())
             .andExpect(result -> {
               result.getResponse();
               assertTrue(true);
             });
  }

}
