package com.lomasity.jscore.badminton.model;

import com.lomasity.jscore.model.TeamType;

import java.io.Serializable;

public class Game implements Serializable {

    private final PointsTarget pointsTarget;
    private final boolean setting;
    // pointHistory holds up to 63 bytes that identify who won the point.
    // Starting from the least significant bit, a 0 means that home won the
    // point and a 1 means away won.
    private long pointsHistory;
    private int pointsPlayed;


    Game(PointsTarget pointsTarget, long pointsHistory, int pointsPlayed, boolean setting) {

        this.pointsTarget = pointsTarget;
        this.pointsHistory = pointsHistory;
        this.pointsPlayed = pointsPlayed;
        this.setting = setting;
    }

    void pointWon(TeamType playerType) {

        if (isFinished()) {
            throw new IllegalStateException("Game is finished so no more points can be won");
        } else {

            if (playerType == TeamType.AWAY) {
                setAwayWin(pointsPlayed);
            }

            pointsPlayed++;
        }
    }

    int getCurrentScore(TeamType playerType) {

        int awayScore = 0;

        for (short p = 0; p < pointsPlayed; p++) {
            if (isAwayWin(p)) {
                awayScore++;
            }
        }

        if (playerType == TeamType.HOME) {
            return pointsPlayed - awayScore;
        } else {
            return awayScore;
        }

    }

    TeamType getLastPointWonBy() {
        int lastPointPlayed = pointsPlayed - 1;
        return getPointWonBy((short) lastPointPlayed);
    }

    TeamType getPointWonBy(short point) {

        if (point >= 0) {
            if (isAwayWin(point)) {
                return TeamType.AWAY;
            } else {
                return TeamType.HOME;
            }
        } else {
            return TeamType.NEITHER;
        }
    }

    void setAwayWin(int point) {
        pointsHistory = pointsHistory | (1L << point);
    }

    boolean isAwayWin(int point) {
        return ((pointsHistory & (1L << point)) != 0);
    }

    public void undoScoreChange() {
        if (pointsPlayed > 0) {
            pointsPlayed--;
            pointsHistory = pointsHistory & ~(1L << pointsPlayed);
        }
    }

    boolean isNewGame() {
        return pointsPlayed == 0;
    }

    boolean isFinished() {

        boolean pointsTargetReached = isPointsTargetReached();

        if (setting) {
            boolean twoPointsClear = isTwoPointsClear();
            boolean maxPointsReached = isMaxPointsReached();
            return (pointsTargetReached && twoPointsClear) || maxPointsReached;

        } else {
            return pointsTargetReached;
        }
    }

    int getMaxPointsInGame() {
        return (getMaxPoints() * 2) - 1;
    }

    private boolean isTwoPointsClear() {
        int pointsDifference = Math.abs(getCurrentScore(TeamType.HOME) - getCurrentScore(TeamType.AWAY));
        return pointsDifference > 1;
    }

    private boolean isPointsTargetReached() {
        int highScore = getHighScore();
        return highScore >= pointsTarget.getTarget();
    }

    private boolean isMaxPointsReached() {
        return getHighScore() == getMaxPoints();
    }

    private int getHighScore() {
        return Math.max(getCurrentScore(TeamType.HOME), getCurrentScore(TeamType.AWAY));
    }

    private int getMaxPoints() {

        if (setting) {
            return pointsTarget.getSettingTarget();
        } else {
            return pointsTarget.getTarget();
        }
    }

    TeamType getLeader() {
        if (getCurrentScore(TeamType.HOME) == getCurrentScore(TeamType.AWAY)) {
            return TeamType.NEITHER;
        } else {
            if (getCurrentScore(TeamType.HOME) > getCurrentScore(TeamType.AWAY)) {
                return TeamType.HOME;
            } else {
                return TeamType.AWAY;
            }
        }
    }

    public long getPointsHistory() {
        return pointsHistory;
    }

    public int getPointsPlayed() {
        return pointsPlayed;
    }

    public int[] getCurrentScore() {
        return new int[]{getCurrentScore(TeamType.HOME), getCurrentScore(TeamType.AWAY)};
    }
}

