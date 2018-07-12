package com.edplan.framework.utils.advance;

import com.edplan.framework.interfaces.Setter;
import com.edplan.framework.utils.SRable;
import com.edplan.framework.utils.SRable.SROperation;
import com.edplan.framework.math.RectI;

public class RectISetting 
{
	private SRable<RectI> saves;

	private Setter<RectI> setter;

	private RectI defValue;

	public RectISetting(Setter<RectI> s,RectI d){
		setter=s;
		defValue=d;
	}

	public RectISetting initial(){
		saves=new SRable<RectI>(new SROperation<RectI>(){
				@Override
				public void onSave(RectI v) {

				}

				@Override
				public void onRestore(RectI now,RectI pre) {

					set(now);
				}

				@Override
				public RectI getDefData() {

					return defValue.copy();
				}
			});
		saves.initial();
		return this;
	}

	public void set(RectI v){
		if(!saves.getData().equals(v)){
			setter.set(v);
			saves.getData().set(v);
		}
	}

	public int save(){
		return saves.save();
	}

	public void restore(){
		saves.restore();
	}

	public void restorToCount(int id){
		saves.restoreToCount(id);
	}
}
