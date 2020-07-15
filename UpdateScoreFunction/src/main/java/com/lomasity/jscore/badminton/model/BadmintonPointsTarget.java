package com.lomasity.jscore.badminton.model;

public enum BadmintonPointsTarget {

	ELEVEN(11, 15), FIFTEEN(15, 21), TWENTY_ONE(21, 30);

	private int target;
	private int settingTarget;

	private BadmintonPointsTarget(int target, int settingTarget) {
		this.target = target;
		this.settingTarget = settingTarget;
	}

	public int getTarget() {
		return this.target;
	}

	public int getSettingTarget() {
		return this.settingTarget;
	}

	public static BadmintonPointsTarget getWithTarget(int target) {
		switch (target) {
		case 11:
			return ELEVEN;
		case 15:
			return FIFTEEN;
		case 21:
			return TWENTY_ONE;
		default:
			throw new IllegalArgumentException(Integer.toString(target) + " is not a valid points target");
		}
	}

}
