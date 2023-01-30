import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.validator.GoalValidator;
import app.foot.controller.rest.Player;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO-1: complete these tests
import static utils.TestUtils.*;

public class GoalValidatorTest {

    GoalValidator subject = new GoalValidator();
    StringBuilder exceptionBuilder = new StringBuilder();

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
                .isGuardian(false)
                .name(playerEntityOne.getPlayer().getName())
                .build();
    }

    private static Player guardianScorer(PlayerScoreEntity playerEntityOne) {
        return Player.builder()
                .id(playerEntityOne.getId())
                .isGuardian(true)
                .name(playerEntityOne.getPlayer().getName())
                .build();
    }
    private static PlayerScoreEntity scorerOne(PlayerEntity playerEntityRakoto) {
        return PlayerScoreEntity.builder()
                .player(playerEntityRakoto)
                .minute(10)
                .build();
    }

    private static PlayerEntity playerOne() {
        return PlayerEntity.builder()
                .id(1)
                .name("Rakoto")
                .guardian(false)
                .team(teamGhana())
                .build();
    }
    private static PlayerScorer secondModelScorer(Player player, PlayerScoreEntity scorerOne) {
        return PlayerScorer.builder()
                .player(player)
                .isOG(false)
                .scoreTime(scorerOne.getMinute())
                .build();
    }

    private static PlayerScorer playerScoringInMandatoryTime(){
        return PlayerScorer.builder()
                .scoreTime(null)
                .isOG(false)
                .player(playerModelOne(playerOne()))
                .build();
    }
    private static PlayerScorer playerScoringIn90(){
        return PlayerScorer.builder()
                .scoreTime(91)
                .isOG(false)
                .player(playerModelOne(playerOne()))
                .build();
    }
    private static PlayerScorer playerScoringInLess0(){
        return PlayerScorer.builder()
                .scoreTime(-1)
                .isOG(false)
                .player(playerModelOne(playerOne()))
                .build();
    }
    private static TeamEntity teamGhana() {
        return TeamEntity.builder()
                .id(2)
                .name("Ghana")
                .build();
    }
    @Test
    void accept_ok() {
        assertDoesNotThrow(() -> subject.accept(scorer1()));

        PlayerScoreEntity playerScorerEntity = scorerOne(playerOne());
        PlayerScorer actual = secondModelScorer(playerScorer(playerScorerEntity),playerScorerEntity);

        subject.accept(actual);
        assertDoesNotThrow(() -> new RuntimeException());
    }

    //Mandatory attributes not provided : scoreTime
    @Test
    void accept_ko() {
        assertThrowsExceptionMessage("400 BAD_REQUEST : Score minute is mandatory.",
                BadRequestException.class, () -> subject.accept(nullScoreTimeScorer()));

        exceptionBuilder.append("Score minute is mandatory.");

        RuntimeException error = assertThrows(RuntimeException.class , () -> subject.accept(playerScoringInMandatoryTime()));

        assertEquals(exceptionBuilder.toString() , error.getMessage());
    }

    @Test
    void when_guardian_throws_exception() {
        scorer1().setPlayer(player1().toBuilder()
                        .isGuardian(true)
                        .build());
        assertThrows(RuntimeException.class, () -> subject.accept(
                scorer1()));
        PlayerScoreEntity playerScorerEntity = scorerOne(playerOne());

        PlayerScorer playerScorer = secondModelScorer(guardianScorer(playerScorerEntity),playerScorerEntity);

        exceptionBuilder
                .append("Player#")
                .append(playerScorer.getPlayer().getId())
                .append(" is a guardian ").append("so they cannot score.");

        RuntimeException error = assertThrows(RuntimeException.class , () -> subject.accept(playerScorer));

        assertEquals(exceptionBuilder.toString() , error.getMessage());
    }

    @Test
    void when_score_time_greater_than_90_throws_exception() {
        scorer1().setScoreTime(91);
        assertThrows(RuntimeException.class, () -> subject.accept(
                scorer1()));
        PlayerScorer playerScorer = playerScoringIn90();

        exceptionBuilder.append("Player#")
                .append(playerScorer.getPlayer().getName())
                .append(" cannot score before after minute 90.");

        RuntimeException error = assertThrows(RuntimeException.class , () -> subject.accept(playerScorer));

        assertEquals(exceptionBuilder.toString() , error.getMessage());
    }

    @Test
    void when_score_time_less_than_0_throws_exception() {
        PlayerScorer playerScorer = playerScoringInLess0();
        scorer1().setScoreTime(-1);

        exceptionBuilder.append("Player#")
                .append(playerScorer.getPlayer().getId())
                .append(" cannot score before before minute 0.");

        RuntimeException error = assertThrows(RuntimeException.class , () -> subject.accept(playerScorer));

        assertEquals(exceptionBuilder.toString() , error.getMessage());
        assertThrows(RuntimeException.class, () -> subject.accept(
                scorer1()));
    }


}
