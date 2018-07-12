package com.edplan.nso.ruleset.std.playing.judgment;
import com.edplan.nso.ruleset.base.playing.Judgment;

public class StdJudgment extends Judgment
{
	private Level level;
	
	public Level getLevel(){
		return level;
	}
	
	public enum Level{
		None(""),Miss("x"),S50("50"),S100("100"),S300("300");
		private final String text;
		Level(String text){
			this.text=text;
		}
	}
}
