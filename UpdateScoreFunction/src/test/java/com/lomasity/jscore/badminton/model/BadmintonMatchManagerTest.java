package com.lomasity.jscore.badminton.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lomasity.jscore.model.TeamType;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class BadmintonMatchManagerTest {

    private BadmintonMatch match;
    private BadmintonMatchManager manager;

    // Points Tests

    @Test
    public void pointsCanBeWon() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.SINGLE_GAME, true);
        assertThat(match.getGames().size(), equalTo(1));
        winPoints(TeamType.HOME, 7);
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.HOME), equalTo(7));
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.AWAY), equalTo(0));
        winPoints(TeamType.AWAY, 18);
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.HOME), equalTo(7));
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.AWAY), equalTo(18));
    }

    @Test
    public void pointsCannotBeWonOnFinishedMatch() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.SINGLE_GAME, true);
        winPoints(TeamType.HOME, 21);
        checkMatchFinished();
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.HOME), equalTo(21));
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.AWAY), equalTo(0));
    }

    @Test
    public void lastPointWonBy_AfterSetting() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.BEST_OF_3, true);
        winPoints(TeamType.HOME, 20);
        assertThat(manager.getLastPointWonBy(), equalTo(TeamType.HOME));
        winPoints(TeamType.AWAY, 21);
        assertThat(manager.getLastPointWonBy(), equalTo(TeamType.AWAY));
        winPoints(TeamType.HOME, 1);
        assertThat(manager.getLastPointWonBy(), equalTo(TeamType.HOME));
        winPoints(TeamType.AWAY, 1);
        assertThat(manager.getLastPointWonBy(), equalTo(TeamType.AWAY));
        winPoints(TeamType.AWAY, 1);
        assertThat(manager.getLastPointWonBy(), equalTo(TeamType.AWAY));
    }

    @Test
    public void lastPointWonBy() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.SINGLE_GAME, true);
        winPoints(TeamType.AWAY, 10);
        winPoints(TeamType.HOME, 20);
        assertThat(manager.getLastPointWonBy(), equalTo(TeamType.HOME));
        winPoints(TeamType.AWAY, 1);
        assertThat(manager.getLastPointWonBy(), equalTo(TeamType.AWAY));
        winPoints(TeamType.HOME, 1);
        assertThat(manager.getLastPointWonBy(), equalTo(TeamType.HOME));
    }

    // Match Leader Tests

    @Test
    public void leaderIsTracked_SingleGame() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.SINGLE_GAME, true);
        checkLeader(TeamType.NEITHER);
        winPoints(TeamType.HOME, 7);
        checkLeader(TeamType.HOME);
        winPoints(TeamType.AWAY, 18);
        checkLeader(TeamType.AWAY);
        winPoints(TeamType.HOME, 13);
        winPoints(TeamType.AWAY, 2);
        checkLeader(TeamType.NEITHER);
    }

    @Test
    public void leaderIsTracked_BestOf3_AwayWins1_HomeLeads2() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.BEST_OF_3, true);
        winPoints(TeamType.AWAY, 21);
        checkLeader(TeamType.AWAY);
        winPoints(TeamType.HOME, 13);
        checkLeader(TeamType.AWAY);
    }

    @Test
    public void leaderIsTracked_BestOf3_AwayWins1_HomeWins2() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.BEST_OF_3, true);
        winPoints(TeamType.AWAY, 21);
        checkLeader(TeamType.AWAY);
        winPoints(TeamType.HOME, 21);
        checkLeader(TeamType.NEITHER);
    }

    @Test
    public void leaderIsTracked_BestOf3_AwayWins1_HomeWins2_AwayLeads3() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.BEST_OF_3, true);
        winPoints(TeamType.AWAY, 21);
        checkLeader(TeamType.AWAY);
        winPoints(TeamType.HOME, 21);
        checkLeader(TeamType.NEITHER);
        winPoints(TeamType.AWAY, 1);
        checkLeader(TeamType.AWAY);
    }

    // Match Finishing Tests

    @Test
    public void singleGameMatchFinishesAtEndOfFirstGame() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.SINGLE_GAME, true);
        winPoints(TeamType.HOME, 21);
        checkLeader(TeamType.HOME);
        checkMatchFinished();
    }

    @Test
    public void BestOfThreeGameMatchCanFinishAtEndOfSecondGame() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.BEST_OF_3, true);
        winPoints(TeamType.HOME, 21);
        checkLeader(TeamType.HOME);
        checkMatchNotFinished();
        winPoints(TeamType.HOME, 21);
        checkLeader(TeamType.HOME);
        checkMatchFinished();
    }

    @Test
    public void BestOfThreeGameMatchFinishesAtEndOfThirdGame() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.BEST_OF_3, true);
        winPoints(TeamType.HOME, 21);
        checkLeader(TeamType.HOME);
        checkMatchNotFinished();
        winPoints(TeamType.AWAY, 21);
        checkLeader(TeamType.NEITHER);
        checkMatchNotFinished();
        winPoints(TeamType.AWAY, 21);
        checkLeader(TeamType.AWAY);
        checkMatchFinished();
    }

    // Undo Tests

    @Test
    public void undoAtEndOfSingleGameMatch() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.SINGLE_GAME, false);
        winPoints(TeamType.HOME, 20);
        winPoints(TeamType.AWAY, 20);
        checkLeader(TeamType.NEITHER);
        winPoints(TeamType.HOME, 1);
        checkLeader(TeamType.HOME);
        checkMatchFinished();
        manager.undoScoreChange();
        checkLeader(TeamType.NEITHER);
        checkMatchNotFinished();
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.HOME), equalTo(20));
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.AWAY), equalTo(20));
    }

    @Test
    public void undoAtEndOfFirstGame() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.BEST_OF_3, false);
        winPoints(TeamType.HOME, 21);
        assertThat(match.getCurrentGame().isNewGame(), equalTo(true));
        manager.undoScoreChange();
        assertThat(match.getCurrentGame().isNewGame(), equalTo(false));
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.HOME), equalTo(20));
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.AWAY), equalTo(0));
    }

    @Test
    public void singleGameMatch_HomeWins() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.SINGLE_GAME, false);
        winPoints(TeamType.HOME, 21);
        checkMatchFinished();
        checkLeader(TeamType.HOME);
    }

    @Test
    public void singleGameMatch_AwayWins() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.SINGLE_GAME, false);
        winPoints(TeamType.AWAY, 21);
        checkMatchFinished();
        checkLeader(TeamType.AWAY);
    }

    @Test
    public void gamesCanBeWon() throws IOException, IllegalAccessException {
        startMatch(BadmintonMatchType.SINGLE_GAME, false);
        winPoints(TeamType.HOME, 21);
        assertThat(match.getGames().size(), equalTo(1));
        assertThat(match.getCurrentGame().isFinished(), equalTo(true));
    }

    // Utilities

    private void startMatch(BadmintonMatchType matchType, boolean setting) throws IOException, IllegalAccessException {

        JsonNode body = new ObjectMapper().readTree("{\"games\": [{ \"currentScore\": { \"home\": 0, \"away\": 0 }, \"pointsPlayed\": 0, \"pointsHistory\": 0 }],"
                + "\"maxGames\": " + matchType.getGamesTarget()
                + ", \"pointsTarget\": " + PointsTarget.TWENTY_ONE.getTarget()
                + ", \"players\": [\"A\", \"B\"], "
                + "\"finished\": " + false + ","
                + "\"setting\": " + setting + "}");

        match = new BadmintonMatch(body);
        manager = new BadmintonMatchManager(match);

        assertThat(match.getCurrentGame().getCurrentScore(TeamType.HOME), equalTo(0));
        assertThat(match.getCurrentGame().getCurrentScore(TeamType.AWAY), equalTo(0));
        checkLeader(TeamType.NEITHER);
        checkMatchNotFinished();
    }

    private void winPoints(TeamType playerType, int requiredPoints) {
        for (int i = 0; i < requiredPoints; i++) {
            manager.pointWon(playerType);
        }
    }

    private void checkLeader(TeamType playerType) {
        assertThat(manager.getLeader(), equalTo(playerType));
    }

    private void checkMatchFinished() {
        assertThat(match.isFinished(), equalTo(true));
    }

    private void checkMatchNotFinished() {
        assertThat(match.isFinished(), equalTo(false));
    }

}
