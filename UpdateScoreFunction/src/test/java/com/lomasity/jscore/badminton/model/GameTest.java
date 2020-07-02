package com.lomasity.jscore.badminton.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.lomasity.jscore.model.TeamType;

public class GameTest {

	private Game game;

	@Test
	public void to21NoSetting_HomeWins() {
		startGame(PointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(21, 20);
		checkGameIsFinished();
	}

	@Test
	public void to21NoSetting_AwayWins() {
		startGame(PointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(20, 21);
		checkGameIsFinished();
	}

	@Test
	public void to21Setting_HomeWins() {
		startGame(PointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(21, 20);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(22, 20);
		checkGameIsFinished();
	}

	@Test
	public void to21Setting_AwayWins() {
		startGame(PointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(20, 21);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(20, 22);
		checkGameIsFinished();
	}

	@Test
	public void to21Setting_HomeWinsAt30() {
		startGame(PointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(29);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(30, 29);
		checkGameIsFinished();
	}

	@Test
	public void to21Setting_AwayWinsAt30() {
		startGame(PointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(29);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(29, 30);
		checkGameIsFinished();
	}

	@Test
	public void to15NoSetting_HomeWins() {
		startGame(PointsTarget.FIFTEEN, false);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(15, 14);
		checkGameIsFinished();
	}

	@Test
	public void to15NoSetting_AwayWins() {
		startGame(PointsTarget.FIFTEEN, false);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(14, 15);
		checkGameIsFinished();
	}

	@Test
	public void to15Setting_HomeWins() {
		startGame(PointsTarget.FIFTEEN, true);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(15, 14);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(16, 14);
		checkGameIsFinished();
	}

	@Test
	public void to15Setting_AwayWins() {
		startGame(PointsTarget.FIFTEEN, true);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(14, 15);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(14, 16);
		checkGameIsFinished();
	}

	@Test
	public void to15Setting_HomeWinsAt21() {
		startGame(PointsTarget.FIFTEEN, true);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(21, 20);
		checkGameIsFinished();
	}

	@Test
	public void to15Setting_AwayWinsAt21() {
		startGame(PointsTarget.FIFTEEN, true);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(20, 21);
		checkGameIsFinished();
	}

	@Test
	public void to11NoSetting_HomeWins() {
		startGame(PointsTarget.ELEVEN, false);
		takeBothScoresTo(10);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(11, 10);
		checkGameIsFinished();
	}

	@Test
	public void to11NoSetting_AwayWins() {
		startGame(PointsTarget.ELEVEN, false);
		takeBothScoresTo(10);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(10, 11);
		checkGameIsFinished();
	}

	@Test
	public void to11Setting_HomeWins() {
		startGame(PointsTarget.ELEVEN, true);
		takeBothScoresTo(10);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(11, 10);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(12, 10);
		checkGameIsFinished();
	}

	@Test
	public void to11Setting_AwayWins() {
		startGame(PointsTarget.ELEVEN, true);
		takeBothScoresTo(10);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(10, 11);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(10, 12);
		checkGameIsFinished();
	}

	@Test
	public void to11Setting_HomeWinsAt15() {
		startGame(PointsTarget.ELEVEN, true);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		homeWins(1);
		checkScore(15, 14);
		checkGameIsFinished();
	}

	@Test
	public void to11Setting_AwayWinsAt15() {
		startGame(PointsTarget.ELEVEN, true);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		awayWins(1);
		checkScore(14, 15);
		checkGameIsFinished();
	}

	@Test
	public void scoresCanBeUndone() {
		startGame(PointsTarget.ELEVEN, true);
		homeWins(5);
		awayWins(6);
		checkScore(5, 6);
		awayWins(1);
		checkScore(5, 7);
		game.undoScoreChange();
		checkScore(5, 6);
		homeWins(1);
		checkScore(6, 6);
		game.undoScoreChange();
		checkScore(5, 6);
	}

	@Test
	public void scoresCanBeRepeatedlyUndone() {
		startGame(PointsTarget.ELEVEN, true);
		takeBothScoresTo(5);
		checkScore(5, 5);
		awayWins(1);
		checkScore(5, 6);
		homeWins(1);
		checkScore(6, 6);
		homeWins(1);
		checkScore(7, 6);

		game.undoScoreChange();
		checkScore(6, 6);
		game.undoScoreChange();
		checkScore(5, 6);
		game.undoScoreChange();
		checkScore(5, 5);
	}

	@Test
	public void undoingNewGameAchievesNothing() {
		startGame(PointsTarget.ELEVEN, true);
		checkScore(0, 0);
		game.undoScoreChange();
		checkScore(0, 0);
	}

	@Test
	public void scoresOfFinishedGameCanBeUndone() {
		startGame(PointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(20);
		homeWins(1);
		checkScore(21, 20);
		checkGameIsFinished();
		game.undoScoreChange();
		checkScore(20, 20);
		checkGameIsNotFinished();
	}

	@Test
	public void leader_neither() {
		startGame(PointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(5);
		assertThat(game.getLeader(), equalTo(TeamType.NEITHER));
	}

	@Test
	public void leader_home() {
		startGame(PointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(5);
		homeWins(1);
		assertThat(game.getLeader(), equalTo(TeamType.HOME));
	}

	@Test
	public void leader_away() {
		startGame(PointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(5);
		awayWins(1);
		assertThat(game.getLeader(), equalTo(TeamType.AWAY));
	}

	@Test
	public void lastPointWonBy_away() {
		startGame(PointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(5);
		awayWins(1);
		assertThat(game.getLastPointWonBy(), equalTo(TeamType.AWAY));
	}

	@Test
	public void lastPointWonBy_home() {
		startGame(PointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(5);
		homeWins(1);
		assertThat(game.getLastPointWonBy(), equalTo(TeamType.HOME));
	}

	@Test
	public void lastPointWonBy_homeWhilstSetting() {
		startGame(PointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(20);
		homeWins(1);
		awayWins(1);
		homeWins(2);
		assertThat(game.getLastPointWonBy(), equalTo(TeamType.HOME));
	}

	@Test
	public void lastPointWonBy_awayWhilstSetting() {
		startGame(PointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(20);
		awayWins(1);
		homeWins(1);
		awayWins(2);
		assertThat(game.getLastPointWonBy(), equalTo(TeamType.AWAY));
	}

	@Test(expected = IllegalStateException.class)
	public void pointCannotBeWonOnFinishedGame() {
		startGame(PointsTarget.TWENTY_ONE, false);
		awayWins(21);
		assertThat(game.isFinished(), equalTo(true));
		awayWins(1);
	}

	private void startGame(PointsTarget pointsTarget, boolean setting) {
		game = new Game(pointsTarget, 0, (short) 0, setting);
		assertThat(game.isNewGame(), equalTo(true));
	}

	private void takeBothScoresTo(int requiredPoints) {
		assertThat(game.isNewGame(), equalTo(true));
		for (int i = 0; i < requiredPoints; i++) {
			game.pointWon(TeamType.HOME);
			game.pointWon(TeamType.AWAY);
		}
		checkScore(requiredPoints, requiredPoints);
	}

	private void checkScore(int h, int a) {
		assertThat(game.getCurrentScore(TeamType.HOME), equalTo(h));
		assertThat(game.getCurrentScore(TeamType.AWAY), equalTo(a));
	}

	private void homeWins(int requiredPoints) {
		for (int i = 0; i < requiredPoints; i++) {
			game.pointWon(TeamType.HOME);
		}
	}

	private void awayWins(int requiredPoints) {
		for (int i = 0; i < requiredPoints; i++) {
			game.pointWon(TeamType.AWAY);
		}
	}

	private void checkGameIsNotFinished() {
		assertThat(game.isFinished(), equalTo(false));
	}

	private void checkGameIsFinished() {
		assertThat(game.isFinished(), equalTo(true));
	}

}
