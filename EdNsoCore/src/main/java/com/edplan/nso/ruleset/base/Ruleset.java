package com.edplan.nso.ruleset.base;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.ui.EdContainer;
import com.edplan.nso.ruleset.base.beatmap.Beatmap;

public abstract class Ruleset
{
	/**
	 *获取Ruleset的名称，用来唯一标识Ruleset的
	 */
	public abstract String getRulesetName();
	
	public abstract AbstractTexture getModeIcon();
	
	public abstract EdContainer createGamePlayContainer();
	
	/**
	 *Ruleset被安装的时候被调用
	 */
	public abstract void onInitialLoad();
	
	/**
	 *加载Ruleset的时候被调用
	 */
	public abstract void onLoad();
	
	/**
	 *被disable的时候被调用
	 */
	public abstract void onDisable();
	
	/**
	 *Ruleset被卸载的时候被调用
	 */
	public abstract void onUninstall();
	
	public AbstractTexture getModeIconBig(){
		return getModeIcon();
	}

	public AbstractTexture getModeIconSmall(){
		return getModeIcon();
	}

	/**
	 *是否接受对传入的Ruleset的专铺的转铺
	 */
	public abstract boolean acceptBeatmap(String beatmapType);
	
	/**
	 *将beatmap转换成这个Ruleset的铺面
	 */
	public abstract Beatmap convertBeatmap(Beatmap beatmap);
	
}
