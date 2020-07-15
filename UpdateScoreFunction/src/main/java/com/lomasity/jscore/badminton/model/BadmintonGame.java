package com.lomasity.jscore.badminton.model;

import java.io.Serializable;

public class BadmintonGame implements Serializable {

    private BadmintonScoring scoring;
    // pointHistory holds up to 63 bytes that identify who won the point.
    // Starting from the least significant bit, a 0 means that home won the
    // point and a 1 means away won.
    private long pointsHistory;
    private int pointsPlayed;

    public BadmintonGame() {
        this(0,0);
    }

    public BadmintonGame(long pointsHistory, int pointsPlayed) {

        this.pointsHistory = pointsHistory;
        this.pointsPlayed = pointsPlayed;
    }

    public long getPointsHistory() {
        return this.pointsHistory;
    }

    public void setPointsHistory(long pointsHistory) {
        this.pointsHistory = pointsHistory;
    }

    public int getPointsPlayed() {
        return this.pointsPlayed;
    }

    public void setPointsPlayed(int pointsPlayed) {
        this.pointsPlayed = pointsPlayed;
    }

    public CurrentScore getCurrentScore() {
        int awayScore = 0;
        for (short p = 0; p < pointsPlayed; p++) {
            if (isAwayWin(p)) {
                awayScore++;
            }
        }
        int homeScore = pointsPlayed - awayScore;

        return new CurrentScore(homeScore, awayScore);
    }



    private boolean isAwayWin(int point) {
        return ((pointsHistory & (1L << point)) != 0);
    }

    public class CurrentScore {
        private int home;
        private int away;

        CurrentScore(int home, int away) {
            this.home = home;
            this.away = away;
        }

        public int getHome() {
            return home;
        }

        public int getAway() {
            return away;
        }
    }
}

