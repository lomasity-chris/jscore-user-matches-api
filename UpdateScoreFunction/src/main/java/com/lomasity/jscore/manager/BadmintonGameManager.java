package com.lomasity.jscore.manager;

import com.lomasity.jscore.badminton.model.BadmintonGame;
import com.lomasity.jscore.badminton.model.BadmintonPointsTarget;
import com.lomasity.jscore.badminton.model.BadmintonScoring;
import com.lomasity.jscore.model.TeamType;

public class BadmintonGameManager {

    private final BadmintonScoring scoring;
    private final boolean setting;
    private final BadmintonPointsTarget pointsTarget;

    public BadmintonGameManager(BadmintonScoring scoring) {
        this.scoring = scoring;
        this.setting = scoring.isSetting();
        this.pointsTarget = BadmintonPointsTarget.getWithTarget(scoring.getPointsTarget());
    }
    
    private BadmintonGame getCurrentGame() {
        return scoring.getGames().get(scoring.getGames().size() - 1);
    }

    public void homePointWon() {
        gameCannotHaveFinished();
        incrementPointsPlayed();
    }

    public void awayPointWon() {

        gameCannotHaveFinished();

        long newPointsHistory = getCurrentGame().getPointsHistory() | (1L << getCurrentGame().getPointsPlayed());
        getCurrentGame().setPointsHistory(newPointsHistory);

        incrementPointsPlayed();
    }

    public void undoScoreChange() {
        if (getCurrentGame().getPointsPlayed() > 0) {
            decrementPointsPlayed();
            long newPointsHistory = getCurrentGame().getPointsHistory() & ~(1L << getCurrentGame().getPointsPlayed());
            getCurrentGame().setPointsHistory(newPointsHistory);
        }
    }

    private void gameCannotHaveFinished() {
        if (isGameFinished(getCurrentGame())) {
            throw new IllegalStateException("Game is finished so no more points can be won");
        }
    }

    private void incrementPointsPlayed() {
        getCurrentGame().setPointsPlayed(getCurrentGame().getPointsPlayed() + 1);
    }

    private void decrementPointsPlayed() {
        getCurrentGame().setPointsPlayed(getCurrentGame().getPointsPlayed() - 1);
    }

    public boolean isCurrentGameFinished() {
        return isGameFinished(getCurrentGame());
    }

    public boolean isGameFinished(BadmintonGame game) {

        boolean pointsTargetReached = isPointsTargetReached(game);

        if (this.setting) {
            boolean twoPointsClear = isTwoPointsClear(game);
            boolean maxPointsReached = isMaxPointsReached(game);
            return (pointsTargetReached && twoPointsClear) || maxPointsReached;

        } else {
            return pointsTargetReached;
        }
    }

    private boolean isTwoPointsClear(BadmintonGame game) {
        int pointsDifference = Math.abs(game.getCurrentScore().getHome() - game.getCurrentScore().getAway());
        return pointsDifference > 1;
    }

    private boolean isPointsTargetReached(BadmintonGame game) {
        int highScore = getHighScore(game);
        return highScore >= pointsTarget.getTarget();
    }

    private boolean isMaxPointsReached(BadmintonGame game) {
        return getHighScore(game) == getMaxPoints();
    }

    private int getHighScore(BadmintonGame game) {
        return Math.max(game.getCurrentScore().getHome(), game.getCurrentScore().getAway());
    }

    private int getMaxPoints() {

        if (setting) {
            return pointsTarget.getSettingTarget();
        } else {
            return pointsTarget.getTarget();
        }
    }

    public TeamType getLeader(BadmintonGame game) {
        int homeScore = game.getCurrentScore().getHome();
        int awayScore = game.getCurrentScore().getAway();
        if ( homeScore == awayScore) {
            return TeamType.NEITHER;
        } else {
            if (homeScore > awayScore) {
                return TeamType.HOME;
            } else {
                return TeamType.AWAY;
            }
        }
    }
}

