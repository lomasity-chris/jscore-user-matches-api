package com.lomasity.jscore.badminton.model;

import java.util.ArrayList;
import java.util.List;

public enum BadmintonMatchType {

	SINGLE_GAME(1, "Single Game"), BEST_OF_3(2, "Best of 3");

	private int gamesTarget;
	private String label;
	private static List<String> labels;

	private BadmintonMatchType(int gamesTarget, String label) {
		this.gamesTarget = gamesTarget;
		this.label = label;
	}

	public Integer getGamesTarget() {
		return new Integer(gamesTarget);
	}

	public String getLabel() {
		return label;
	}
	
	public static List<String> getLabels() {
		if (labels == null) {
			labels = new ArrayList<String>();
			for (BadmintonMatchType badmintonMatchType : BadmintonMatchType.values()) {
				labels.add(badmintonMatchType.getLabel());
			}
		}
		return labels;
	}

	public static BadmintonMatchType getByLabel(String s) {
		for (BadmintonMatchType badmintonMatchType : BadmintonMatchType.values()) {
			if (badmintonMatchType.getLabel().equals(s)) {
				return badmintonMatchType;
			}
		}
		throw new IllegalArgumentException(s + " is not a valid badminton match type");
	}

}
