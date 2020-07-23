package com.lomasity.jscore.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lomasity.jscore.badminton.model.BadmintonGame;
import com.lomasity.jscore.badminton.model.BadmintonPointsTarget;
import com.lomasity.jscore.badminton.model.BadmintonScoring;
import com.lomasity.jscore.model.TeamType;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class BadmintonManagerTest {

    private BadmintonManager manager;

    // Points Tests

    @Test
    public void pointsCanBeWon() throws IOException {
        startMatch(1, true);
        assertThat(manager.getScoring().getGames().size(), equalTo(1));

        winPoints(TeamType.HOME, 7);
        checkCurrentScore(0,7,0);

        winPoints(TeamType.AWAY, 18);
        checkCurrentScore(0,7,18);

    }
    @Test
    public void pointsCannotBeWonOnFinishedMatch() throws IOException {
        startMatch(1, true);
        winPoints(TeamType.HOME, 21);
        checkMatchFinished();
        checkCurrentScore(0,21,0);
    }

    // Match Finishing Tests

    @Test
    public void singleGameMatchFinishesAtEndOfFirstGame() throws IOException {
        startMatch(1, true);
        winPoints(TeamType.HOME, 21);
        checkMatchFinished();
    }

    @Test
    public void BestOfThreeGameMatchCanFinishAtEndOfSecondGame() throws IOException, IllegalAccessException {
        startMatch(2, true);
        winPoints(TeamType.HOME, 21);
        checkMatchNotFinished();
        winPoints(TeamType.HOME, 21);
        checkMatchFinished();
    }

    @Test
    public void BestOfThreeGameMatchFinishesAtEndOfThirdGame() throws IOException, IllegalAccessException {
        startMatch(2, true);
        checkMatchNotFinished();
        winPoints(TeamType.AWAY, 21);
        checkMatchNotFinished();
        winPoints(TeamType.AWAY, 21);
        checkMatchFinished();
    }

    // Undo Tests

    @Test
    public void undoAtEndOfSingleGameMatch() throws IOException, IllegalAccessException {
        startMatch(1, false);
        winPoints(TeamType.HOME, 20);
        winPoints(TeamType.AWAY, 21);
        checkMatchFinished();
        manager.undoScoreChange();
        checkMatchNotFinished();
        checkCurrentScore(0,20,20);
    }

    @Test
    public void undoAtEndOfFirstGame() throws IOException, IllegalAccessException {
        startMatch(2, false);
        winPoints(TeamType.HOME, 21);
        assertThat(manager.getScoring().getGames().size(), equalTo(2));
        checkCurrentScore(0,21,0);
        checkCurrentScore(1,0,0);
        manager.undoScoreChange();
        assertThat(manager.getScoring().getGames().size(), equalTo(1));
        checkCurrentScore(0,20,0);
    }

    @Test
    public void undoStoppedGameJustRestartsIt() throws IOException {

        JsonNode body = new ObjectMapper().readTree("{\"games\": [{ \"currentScore\": { \"home\": 5, \"away\": 6 }, \"pointsPlayed\": 5, \"pointsHistory\": 231 }],"
                + "\"gamesTarget\": " + 1
                + ", \"pointsTarget\": 21"
                + ", \"players\": [\"A\", \"B\"], "
                + "\"finished\": true, \"stopped\": true, \"setting\": " + false + "}");

        manager = new BadmintonManager(body);

        checkCurrentScore(0,2,3);
        assertThat(manager.getScoring().isStopped(), equalTo(true));
        assertThat(manager.getScoring().isFinished(), equalTo(true));

        manager.undoScoreChange();
        checkCurrentScore(0,2,3);
        assertThat(manager.getScoring().isStopped(), equalTo(false));
        assertThat(manager.getScoring().isFinished(), equalTo(false));
    }

    @Test
    public void singleGameMatch_HomeWins() throws IOException, IllegalAccessException {
        startMatch(1, false);
        winPoints(TeamType.HOME, 21);
        checkMatchFinished();
    }

    @Test
    public void singleGameMatch_AwayWins() throws IOException, IllegalAccessException {
        startMatch(1, false);
        winPoints(TeamType.AWAY, 21);
        checkMatchFinished();
    }

    // Utilities

    private void startMatch(int gamesTarget, boolean setting) throws IOException {

        JsonNode body = new ObjectMapper().readTree("{\"games\": [{ \"currentScore\": { \"home\": 0, \"away\": 0 }, \"pointsPlayed\": 0, \"pointsHistory\": 0 }],"
                + "\"gamesTarget\": " + gamesTarget
                + ", \"pointsTarget\": 21"
                + ", \"players\": [\"A\", \"B\"], "
                + "\"finished\": false, \"stopped\": false, \"setting\": " + setting + "}");

        manager = new BadmintonManager(body);

        checkCurrentScore(0,0,0);
        checkMatchNotFinished();
    }

    private void checkCurrentScore(int gameIndex, int expectedHomeScore, int expectedAwayScore) {
        assertThat(manager.getScoring().getGames().get(gameIndex).getCurrentScore().getHome(), equalTo(expectedHomeScore));
        assertThat(manager.getScoring().getGames().get(gameIndex).getCurrentScore().getAway(), equalTo(expectedAwayScore));
    }


    private void winPoints(TeamType playerType, int requiredPoints) {
        for (int i = 0; i < requiredPoints; i++) {
            manager.incrementScoreOf(playerType);
        }
    }

    private void checkMatchFinished() {
        assertThat(manager.getScoring().isFinished(), equalTo(true));
    }

    private void checkMatchNotFinished() {
        assertThat(manager.getScoring().isFinished(), equalTo(false));
    }

}
