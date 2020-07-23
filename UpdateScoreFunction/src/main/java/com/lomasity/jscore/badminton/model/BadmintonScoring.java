package com.lomasity.jscore.badminton.model;

import com.lomasity.jscore.model.Scoring;

import java.util.ArrayList;
import java.util.List;

public class BadmintonScoring implements Scoring {

    private final int gamesTarget;
    private final int pointsTarget;
    private final boolean setting;
    private boolean finished;
    private boolean stopped;
    private List<BadmintonGame> games;

    public BadmintonScoring(boolean finished, int gamesTarget, int pointsTarget, boolean setting, boolean stopped) {

        this.finished = finished;
        this.gamesTarget = gamesTarget;
        this.pointsTarget = pointsTarget;
        this.setting = setting;
        this.stopped = stopped;
    }

    public List<BadmintonGame> getGames() {
        if (this.games == null) {
            this.games = new ArrayList<>();
        }
        return this.games;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isSetting() {
        return this.setting;
    }

    public int getPointsTarget() {
        return this.pointsTarget;
    }

    public int getGamesTarget() {
        return this.gamesTarget;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}

