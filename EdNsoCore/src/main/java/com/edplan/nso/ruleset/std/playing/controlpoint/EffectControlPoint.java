package com.edplan.nso.ruleset.std.playing.controlpoint;

public class EffectControlPoint extends ControlPoint
{
	private boolean kiaiModeOn;
	
	private boolean omitFirstBarLine;


	public void setKiaiModeOn(boolean kiaiModeOn) {
		this.kiaiModeOn=kiaiModeOn;
	}

	public boolean isKiaiModeOn() {
		return kiaiModeOn;
	}

	public void setOmitFirstBarLine(boolean omitFirstBarLine) {
		this.omitFirstBarLine=omitFirstBarLine;
	}

	public boolean isOmitFirstBarLine() {
		return omitFirstBarLine;
	}
}
