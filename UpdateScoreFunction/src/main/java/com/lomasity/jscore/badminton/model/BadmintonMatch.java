package com.lomasity.jscore.badminton.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.lomasity.jscore.badminton.model.BadmintonMatchType;
import com.lomasity.jscore.badminton.model.Game;
import com.lomasity.jscore.badminton.model.PointsTarget;
import com.lomasity.jscore.model.TeamType;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.lomasity.jscore.badminton.model.InputParams.*;

public class BadmintonMatch {

    private final int maxGames;
    private final int pointsTarget;

    private final boolean finished;
    private final boolean setting;
    private final BadmintonMatchType matchType;
    private final PointsTarget pointsTargetAsEnum;
    private List<Game> games;

    public BadmintonMatch(JsonNode body) throws IllegalAccessException {

        for (InputParams inputParam : InputParams.values()) {
            if (inputParam.isRequired() && null == body.get(inputParam.getName())) {
                throw new IllegalAccessException(inputParam.getName() + " is required in the request body");
            }
        };

        this.pointsTarget = body.get(POINTS_TARGET.getName()).asInt();
        this.pointsTargetAsEnum = PointsTarget.getWithTarget(pointsTarget);
        this.maxGames = body.get(MAX_GAMES.getName()).asInt();
        this.matchType = maxGames == 1 ? BadmintonMatchType.SINGLE_GAME : BadmintonMatchType.BEST_OF_3;
        this.setting = body.get(SETTING.getName()).asBoolean();
        this.finished = body.get(FINISHED.getName()).asBoolean();

        for (final JsonNode inputGame : body.get(GAMES.getName())) {

            long pointsHistory = inputGame.get(POINTS_HISTORY.getName()).asLong();
            short pointsPlayed = (short)inputGame.get(POINTS_PLAYED.getName()).asInt();
            Game game = new Game(pointsTargetAsEnum, pointsHistory, pointsPlayed, setting);
            getGames().add(game);
        }
    }

    public List<Game> getGames() {
        if (this.games == null) {
            this.games = new ArrayList<>();
        }
        return this.games;
    }

    public boolean isFinished() {
        return getGameCount(TeamType.HOME) == getMatchType().getGamesTarget()
                || getGameCount(TeamType.AWAY) == getMatchType().getGamesTarget();
    }

    public boolean isSetting() {
        return this.setting;
    }

    public int getPointsTarget() {
        return this.pointsTarget;
    }

    public int getMaxGames() {
        return this.maxGames;
    }

    BadmintonMatchType getMatchType() {
        return this.matchType;
    }

    PointsTarget getPointsTargetAsEnum() {
        return pointsTargetAsEnum;
    }

    void addNewGame() {
        Game newGame = new Game(this.pointsTargetAsEnum, 0, 0, this.setting);
        getGames().add(newGame);
    }

    int getGameCount(TeamType teamType) {
        int count = 0;
        for (Game game : getGames()) {
            if (game.isFinished() && teamType.equals(game.getLeader())) {
                count++;
            }
        }
        return count;
    }

    Game getCurrentGame() {
        Game currentGame = getGames().get(this.games.size() - 1);
        return currentGame;
    }

    Game getPreviousGame() {
        Game previousGame = null;
        if (!isFirstGame()) {
            previousGame = getGames().get(this.games.size() - 2);
        }
        return previousGame;
    }

    boolean isFirstGame() {
        return this.games.size() == 1;
    }


}
