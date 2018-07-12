package com.edplan.nso.ruleset.std.objects;

public enum StdHitObjectType
{
	Circle,Slider,Spinner,ManiaHolder;
	public static StdHitObjectType parseType(int i){
		if((i&0x00000001)==1){
			return Circle;
		}else if((i&0x00000002)==0x00000002){
			return Slider;
		}else if((i&0x00000008)==0x00000008){
			return Spinner;
		}else if((i&0x00000080)==0x00000080){
			return ManiaHolder;
		}else{
			return null;
		}
	}
}
