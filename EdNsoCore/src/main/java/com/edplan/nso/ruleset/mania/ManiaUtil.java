package com.edplan.nso.ruleset.mania;
import com.edplan.nso.ruleset.base.playing.PlayField;

public class ManiaUtil
{
	public static final float[] COLUMN_WIDTH;
	public static final int MAX_COLUMN_COUNT=8;
	static{
		//第一项留空
		COLUMN_WIDTH=new float[MAX_COLUMN_COUNT+1];
		for(int i=1;i<=MAX_COLUMN_COUNT;i++){
			if(i!=7){
				COLUMN_WIDTH[i]=PlayField.CANVAS_SIZE_X/i;
			}else{
				COLUMN_WIDTH[i]=PlayField.CANVAS_SIZE_X/7;
			}
		}
	}
	
	/**
	 *在osu!wiki上osu!_File_Format里面有介绍，
	 *8k模式实际上为7k+1模式（仿某街机游戏的转盘，那个+1是可以设置两个键的）
	 */
	public static int toColumnIndex(int keyCount,float x){
		return (int)(x/COLUMN_WIDTH[keyCount])+(keyCount==8?1:0);
	}
}
