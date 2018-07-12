package com.edplan.nso.ruleset.std.playing.controlpoint;

public class DifficultyControlPoint extends ControlPoint
{
	private double speedMultiplier;


	public void setSpeedMultiplier(double speedMultiplier) {
		this.speedMultiplier=speedMultiplier;
	}

	public double getSpeedMultiplier() {
		return speedMultiplier;
	}
}
