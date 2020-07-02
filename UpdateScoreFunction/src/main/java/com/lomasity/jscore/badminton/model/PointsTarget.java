package com.lomasity.jscore.badminton.model;

public enum PointsTarget {

	ELEVEN(11, 15), FIFTEEN(15, 21), TWENTY_ONE(21, 30);

	private int target;
	private int settingTarget;

	private PointsTarget(int target, int settingTarget) {
		this.target = target;
		this.settingTarget = settingTarget;
	}

	int getTarget() {
		return this.target;
	}

	int getSettingTarget() {
		return this.settingTarget;
	}

	static PointsTarget getWithTarget(int target) {
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
