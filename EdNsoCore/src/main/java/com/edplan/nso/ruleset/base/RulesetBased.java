package com.edplan.nso.ruleset.base;

public class RulesetBased
{
	private Ruleset ruleset;
	
	public RulesetBased(Ruleset r){
		this.ruleset=r;
	}

	public Ruleset getRuleset(){
		return ruleset;
	}
	
	public String getRulesetName(){
		return getRuleset().getRulesetName();
	}
}
