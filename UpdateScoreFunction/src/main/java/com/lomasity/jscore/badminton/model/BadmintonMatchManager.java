package com.lomasity.jscore.badminton.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lomasity.jscore.badminton.model.BadmintonMatch;
import com.lomasity.jscore.badminton.model.Game;
import com.lomasity.jscore.badminton.model.PointsTarget;
import com.lomasity.jscore.model.TeamType;

import java.util.List;

public class BadmintonMatchManager {

    private static final long serialVersionUID = 1L;

    private final BadmintonMatch match;
    private PointsTarget pointsTarget;
    private boolean setting;
    private List<Game> games;

    public BadmintonMatchManager(BadmintonMatch match) {
        this.match = match;
    }

    public void pointWon(TeamType teamType) {

		if (!match.isFinished()) {

			this.match.getCurrentGame().pointWon(teamType);

			// If the current game has just finished and the match has not finished
			// add a new game.
			if (this.match.getCurrentGame().isFinished()) {
				if (!this.match.isFinished()) {
					this.match.addNewGame();
				}
			}
		}
    }

	public void undoScoreChange() {

		// If the game has just finished need to remove the new game before
		// undoing
		// the change on the required game.
		if (this.match.getCurrentGame().isNewGame()) {
			match.getGames().remove(match.getGames().size() - 1);
		}
		this.match.getCurrentGame().undoScoreChange();
	}

	public TeamType getLastPointWonBy() {
		if (this.match.getCurrentGame().getPointsPlayed() > 0) {
			return this.match.getCurrentGame().getLastPointWonBy();
		} else {
			if (!this.match.isFirstGame()) {
				return this.match.getPreviousGame().getLastPointWonBy();
			}
		}
		return TeamType.NEITHER;
	}



	public TeamType getLeader() {
		short homeWins = 0;
		short awayWins = 0;

		for (Game game : this.match.getGames()) {
			if (game.isFinished()) {
				if (TeamType.HOME.equals(game.getLeader())) {
					homeWins++;
				} else {
					awayWins++;
				}
			}
		}

		if (homeWins > awayWins) {
			return TeamType.HOME;
		} else {
			if (homeWins < awayWins) {
				return TeamType.AWAY;
			} else {
				return this.match.getCurrentGame().getLeader();
			}
		}
	}


}
