package com.lomasity.jscore.manager;

import com.lomasity.jscore.badminton.model.BadmintonGame;
import com.lomasity.jscore.badminton.model.BadmintonPointsTarget;
import com.lomasity.jscore.badminton.model.BadmintonScoring;
import com.lomasity.jscore.model.TeamType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class BadmintonGameManagerTest {

	private BadmintonGameManager badmintonGameManager;
	private BadmintonScoring scoring;

	@Test
	public void to21NoSetting_HomeWins() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(21, 20);
		checkGameIsFinished();
	}

	@Test
	public void to21NoSetting_AwayWins() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(20, 21);
		checkGameIsFinished();
	}

	@Test
	public void to21Setting_HomeWins() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(21, 20);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(22, 20);
		checkGameIsFinished();
	}

	@Test
	public void to21Setting_AwayWins() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(20, 21);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(20, 22);
		checkGameIsFinished();
	}

	@Test
	public void to21Setting_HomeWinsAt30() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(29);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(30, 29);
		checkGameIsFinished();
	}

	@Test
	public void to21Setting_AwayWinsAt30() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, true);
		takeBothScoresTo(29);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(29, 30);
		checkGameIsFinished();
	}

	@Test
	public void to15NoSetting_HomeWins() {
		startGame(BadmintonPointsTarget.FIFTEEN, false);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(15, 14);
		checkGameIsFinished();
	}

	@Test
	public void to15NoSetting_AwayWins() {
		startGame(BadmintonPointsTarget.FIFTEEN, false);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(14, 15);
		checkGameIsFinished();
	}

	@Test
	public void to15Setting_HomeWins() {
		startGame(BadmintonPointsTarget.FIFTEEN, true);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(15, 14);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(16, 14);
		checkGameIsFinished();
	}

	@Test
	public void to15Setting_AwayWins() {
		startGame(BadmintonPointsTarget.FIFTEEN, true);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(14, 15);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(14, 16);
		checkGameIsFinished();
	}

	@Test
	public void to15Setting_HomeWinsAt21() {
		startGame(BadmintonPointsTarget.FIFTEEN, true);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(21, 20);
		checkGameIsFinished();
	}

	@Test
	public void to15Setting_AwayWinsAt21() {
		startGame(BadmintonPointsTarget.FIFTEEN, true);
		takeBothScoresTo(20);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(20, 21);
		checkGameIsFinished();
	}

	@Test
	public void to11NoSetting_HomeWins() {
		startGame(BadmintonPointsTarget.ELEVEN, false);
		takeBothScoresTo(10);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(11, 10);
		checkGameIsFinished();
	}

	@Test
	public void to11NoSetting_AwayWins() {
		startGame(BadmintonPointsTarget.ELEVEN, false);
		takeBothScoresTo(10);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(10, 11);
		checkGameIsFinished();
	}

	@Test
	public void to11Setting_HomeWins() {
		startGame(BadmintonPointsTarget.ELEVEN, true);
		takeBothScoresTo(10);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(11, 10);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(12, 10);
		checkGameIsFinished();
	}

	@Test
	public void to11Setting_AwayWins() {
		startGame(BadmintonPointsTarget.ELEVEN, true);
		takeBothScoresTo(10);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(10, 11);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(10, 12);
		checkGameIsFinished();
	}

	@Test
	public void to11Setting_HomeWinsAt15() {
		startGame(BadmintonPointsTarget.ELEVEN, true);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		homeWins(1);
		checkCurrentScore(15, 14);
		checkGameIsFinished();
	}

	@Test
	public void to11Setting_AwayWinsAt15() {
		startGame(BadmintonPointsTarget.ELEVEN, true);
		takeBothScoresTo(14);
		checkGameIsNotFinished();
		awayWins(1);
		checkCurrentScore(14, 15);
		checkGameIsFinished();
	}

	@Test
	public void scoresCanBeUndone() {
		startGame(BadmintonPointsTarget.ELEVEN, true);
		homeWins(5);
		awayWins(6);
		checkCurrentScore(5, 6);
		awayWins(1);
		checkCurrentScore(5, 7);
		badmintonGameManager.undoScoreChange();
		checkCurrentScore(5, 6);
		homeWins(1);
		checkCurrentScore(6, 6);
		badmintonGameManager.undoScoreChange();
		checkCurrentScore(5, 6);
	}

	@Test
	public void scoresCanBeRepeatedlyUndone() {
		startGame(BadmintonPointsTarget.ELEVEN, true);
		takeBothScoresTo(5);
		checkCurrentScore(5, 5);
		awayWins(1);
		checkCurrentScore(5, 6);
		homeWins(1);
		checkCurrentScore(6, 6);
		homeWins(1);
		checkCurrentScore(7, 6);

		badmintonGameManager.undoScoreChange();
		checkCurrentScore(6, 6);
		badmintonGameManager.undoScoreChange();
		checkCurrentScore(5, 6);
		badmintonGameManager.undoScoreChange();
		checkCurrentScore(5, 5);
	}

	@Test
	public void undoingNewGameAchievesNothing() {
		startGame(BadmintonPointsTarget.ELEVEN, true);
		checkCurrentScore(0, 0);
		badmintonGameManager.undoScoreChange();
		checkCurrentScore(0, 0);
	}

	@Test
	public void scoresOfFinishedGameCanBeUndone() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(20);
		homeWins(1);
		checkCurrentScore(21, 20);
		checkGameIsFinished();
		badmintonGameManager.undoScoreChange();
		checkCurrentScore(20, 20);
		checkGameIsNotFinished();
	}

	@Test(expected = IllegalStateException.class)
	public void pointCannotBeWonOnFinishedGame() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, false);
		awayWins(21);
		awayWins(1);
	}

	@Test
	public void leader_neither() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(5);
		assertThat(badmintonGameManager.getLeader(scoring.getGames().get(0)), equalTo(TeamType.NEITHER));
	}

	@Test
	public void leader_home() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(5);
		homeWins(1);
		assertThat(badmintonGameManager.getLeader(scoring.getGames().get(0)), equalTo(TeamType.HOME));
	}

	@Test
	public void leader_away() {
		startGame(BadmintonPointsTarget.TWENTY_ONE, false);
		takeBothScoresTo(5);
		awayWins(1);
		assertThat(badmintonGameManager.getLeader(scoring.getGames().get(0)), equalTo(TeamType.AWAY));
	}

	private void startGame(BadmintonPointsTarget badmintonPointsTarget, boolean setting) {

		scoring = new BadmintonScoring(false, 1, badmintonPointsTarget.getTarget(), setting);
		scoring.getGames().add(new BadmintonGame());
		badmintonGameManager = new BadmintonGameManager(scoring);
		checkCurrentScore(0,0);
	}

	private void takeBothScoresTo(int requiredPoints) {
		checkCurrentScore(0,0);
		for (int i = 0; i < requiredPoints; i++) {
			badmintonGameManager.homePointWon();
			badmintonGameManager.awayPointWon();
		}
		checkCurrentScore(requiredPoints,requiredPoints);
	}

	private void homeWins(int requiredPoints) {
		for (int i = 0; i < requiredPoints; i++) {
			badmintonGameManager.homePointWon();
		}
	}

	private void awayWins(int requiredPoints) {
		for (int i = 0; i < requiredPoints; i++) {
			badmintonGameManager.awayPointWon();
		}
	}

	private void checkGameIsNotFinished() {
		assertThat(badmintonGameManager.isCurrentGameFinished(), equalTo(false));
	}

	private void checkGameIsFinished() {
		assertThat(badmintonGameManager.isCurrentGameFinished(), equalTo(true));
	}

	private void checkCurrentScore(int expectedHomeScore, int expectedAwayScore) {
		assertThat(scoring.getGames().get(0).getCurrentScore().getHome(), equalTo(expectedHomeScore));
		assertThat(scoring.getGames().get(0).getCurrentScore().getAway(), equalTo(expectedAwayScore));
	}

}
