package com.edplan.nso.ruleset.std.playing.controlpoint;

public class TimingControlPoint extends ControlPoint
{
	private int meter;
	
	private double beatLength;


	public void setMeter(int meter) {
		this.meter=meter;
	}

	public int getMeter() {
		return meter;
	}

	public void setBeatLength(double beatLength) {
		this.beatLength=beatLength;
	}

	public double getBeatLength() {
		return beatLength;
	}
}
