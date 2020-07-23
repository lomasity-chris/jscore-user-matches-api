package com.lomasity.jscore.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.lomasity.jscore.badminton.model.BadmintonGame;
import com.lomasity.jscore.badminton.model.BadmintonScoring;
import com.lomasity.jscore.model.TeamType;

public class BadmintonManager implements Manager {

    private static final String FINISHED = "finished";
    private static final String STOPPED = "stopped";
    private static final String GAMES_TARGET = "gamesTarget";
    private static final String POINTS_TARGET = "pointsTarget";
    private static final String SETTING = "setting";
    private static final String GAMES = "games";
    private static final String POINTS_HISTORY = "pointsHistory";
    private static final String POINTS_PLAYED = "pointsPlayed";

    private final BadmintonScoring scoring;

    private final BadmintonGameManager gameManager;

    public BadmintonManager(JsonNode jsonScoring) {

        boolean stopped = is(STOPPED, jsonScoring);
        boolean finished = is(FINISHED, jsonScoring);
        boolean setting = is(SETTING, jsonScoring);
        int gamesTarget = jsonScoring.get(GAMES_TARGET).asInt();
        int pointsTarget = jsonScoring.get(POINTS_TARGET).asInt();

        this.scoring = new BadmintonScoring(finished, gamesTarget, pointsTarget, setting, stopped);

        for (final JsonNode jsonGame : jsonScoring.get(GAMES)) {

            long pointsHistory = jsonGame.get(POINTS_HISTORY).asLong();
            int pointsPlayed = jsonGame.get(POINTS_PLAYED).asInt();
            BadmintonGame game = new BadmintonGame(pointsHistory, pointsPlayed);
            scoring.getGames().add(game);
        }

        gameManager = new BadmintonGameManager(scoring);
    }

    private boolean is(String fieldName,JsonNode jsonScoring) {
        return jsonScoring.get(fieldName) != null && jsonScoring.get(fieldName).asBoolean();
    }

    public void incrementScoreOf(TeamType teamType) {

        if (!isMatchFinished()) {

            switch (teamType) {
                case HOME:
                    gameManager.homePointWon();
                    break;
                case AWAY:
                    gameManager.awayPointWon();
                    break;
                default:
                    throw new IllegalArgumentException("Score of type " + teamType + " score cannot be incremented");
            }
        }

        // If the current game has just finished and the match has not finished
        // add a new game.
        if (gameManager.isCurrentGameFinished()) {
            if (!isMatchFinished()) {
                this.scoring.getGames().add(new BadmintonGame());
            }
        }

        this.scoring.setFinished(isMatchFinished());
    }

    private boolean isMatchFinished() {

        return getGameCount(TeamType.HOME) == scoring.getGamesTarget()
                || getGameCount(TeamType.AWAY) == scoring.getGamesTarget();
    }

    private int getGameCount(TeamType teamType) {
        int count = 0;
        for (BadmintonGame game : this.scoring.getGames()) {
            if (gameManager.isGameFinished(game) && teamType.equals(gameManager.getLeader(game))) {
                count++;
            }
        }
        return count;
    }

    public void undoScoreChange() {

        if (scoring.isStopped()) {
            scoring.setStopped(false);
            scoring.setFinished(false);
        } else {

            BadmintonGame currentGame = scoring.getGames().get(scoring.getGames().size() - 1);

            // If the game has just finished need to remove the new game before undoing the change on the required game.
            if (currentGame.getPointsPlayed() == 0) {
                scoring.getGames().remove(scoring.getGames().size() - 1);
            }

            gameManager.undoScoreChange();

            this.scoring.setFinished(isMatchFinished());
        }
    }

    @Override
    public BadmintonScoring getScoring() {
        return this.scoring;
    }
}
