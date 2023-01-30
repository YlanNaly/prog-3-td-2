package integration;

import app.foot.FootApi;
import app.foot.controller.MatchController;
import app.foot.controller.rest.Match;
import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.rest.Team;
import app.foot.controller.rest.TeamMatch;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.service.MatchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
public class MatchIntegretionTest {

  @Autowired
  private MockMvc mockMvc;
  StringBuilder exceptionBuilder = new StringBuilder();

  MatchService service ;
  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
  private Match convertFromHttpResponse(MockHttpServletResponse response)
          throws JsonProcessingException, UnsupportedEncodingException {

    return objectMapper.readValue(
            response.getContentAsString(),
            Match.class);
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
}
