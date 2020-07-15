package com.lomasity.jscore.manager;

import com.lomasity.jscore.model.Scoring;
import com.lomasity.jscore.model.TeamType;

public interface Manager {
    public void incrementScoreOf(TeamType teamType);
    public void undoScoreChange();
    public Scoring getScoring();
}
