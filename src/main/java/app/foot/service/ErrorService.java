package app.foot.service;

import app.foot.model.Match;
import app.foot.model.Team;
import app.foot.repository.MatchRepository;
import app.foot.repository.TeamRepository;
import app.foot.repository.mapper.MatchMapper;
import app.foot.repository.mapper.TeamMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ErrorService {
  private  final TeamRepository teamRepository;
  private final TeamMapper teamMapper;
  private final MatchMapper matchMapper;
  private final MatchRepository matchRepository ;

  public ResponseEntity<?> checking(int id , Match postMatch){
    Match match = matchMapper.toDomain(matchRepository.getById(id));
    Team teamA = teamMapper.toDomain(teamRepository.getById(postMatch.getTeamA().getTeam().getId()));
    Team teamB = teamMapper.toDomain(teamRepository.getById(postMatch.getTeamB().getTeam().getId()));

    for(int i= 0 ; i<match.getTeamA().getScorers().size() ; i++){
      if(Objects.equals(teamA.getId(), match.getTeamA().getTeam().getId()) && match.getTeamA().getScorers().get(i).getPlayer().getIsGuardian()){
        return new ResponseEntity<>("guardian did not goaling", HttpStatusCode.valueOf(400));
      } else if (Objects.equals(teamA.getId(), match.getTeamA().getTeam().getId()) && match.getTeamA().getScorers().get(i).getMinute() < -1 &&
              match.getTeamA().getScorers().get(i).getMinute() > 90) {
        return new ResponseEntity<>(" Team A need to make a goal only between 1min - 90min", HttpStatusCode.valueOf(400));
      }
      else if (Objects.equals(teamB.getId(), match.getTeamB().getTeam().getId()) && match.getTeamB().getScorers().get(i).getPlayer().getIsGuardian()){
        return new ResponseEntity<>("guardian did not goaling", HttpStatusCode.valueOf(400));
      }
      else if (Objects.equals(teamB.getId(), match.getTeamB().getTeam().getId()) && match.getTeamB().getScorers().get(i).getMinute() < -1 &&
              match.getTeamB().getScorers().get(i).getMinute() > 90) {
        return new ResponseEntity<>("Team B need to make a goal only between 1min - 90min", HttpStatusCode.valueOf(400));
      }
      return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
    return new ResponseEntity<>(HttpStatusCode.valueOf(200));
  }
}