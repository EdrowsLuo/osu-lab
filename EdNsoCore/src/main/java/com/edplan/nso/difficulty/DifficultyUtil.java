package com.edplan.nso.difficulty;

public class DifficultyUtil
{
	public static final float STD_TIME_PREEMPT_MAX=1800;
	public static final float STD_TIME_PREEMPT_MID=1200;
	public static final float STD_TIME_PREEMPT_MIN=450;
	
	public static final float STD_TIME_FADEIN_MAX=1200;
	public static final float STD_TIME_FADEIN_MID=800;
	public static final float STD_TIME_FADEIN_MIN=300;
	
	
	/**
	 *线性插值计算difficulty对应的数值
	 *@param difficulty:默认范围为0～10，实际可能会有大于10的情况（比如dt ar10）
	 */
	public static float difficultyRange(float difficulty,float min,float mid,float max){
		if(difficulty>5){
			return mid+(max-mid)*(difficulty-5)/5;
		}else if(difficulty<5){
			return mid-(mid-min)*(5-difficulty)/5;
		}else{
			return mid;
		}
	}
	
	/**
	 *看名说话系列
	 */
	public static int stdHitObjectTimePreempt(float ar){
		return (int)
			difficultyRange(
				ar,
				STD_TIME_PREEMPT_MAX,
				STD_TIME_PREEMPT_MID,
				STD_TIME_PREEMPT_MIN
			);
	}
	
	/**
	 *看名说话系列
	 */
	public static int stdHitObjectTimeFadein(float ar){
		return (int)
			difficultyRange(
				ar,
				STD_TIME_FADEIN_MAX,
				STD_TIME_FADEIN_MID,
				STD_TIME_FADEIN_MIN
			);
	}
	
	public static float stdCircleSizeScale(float cs){
		return (1.0f-0.7f*(cs-5)/5)/2;
	}
}
