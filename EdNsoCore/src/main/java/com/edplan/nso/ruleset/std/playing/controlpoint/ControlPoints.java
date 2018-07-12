package com.edplan.nso.ruleset.std.playing.controlpoint;
import com.edplan.nso.timing.TimingPoint;
import com.edplan.nso.timing.TimingPoints;
import java.util.ArrayList;
import java.util.List;
import com.edplan.nso.NsoException;
import android.util.Log;

public class ControlPoints
{
	private ArrayList<TimingControlPoint> timingPoints=new ArrayList<TimingControlPoint>();
	
	private ArrayList<DifficultyControlPoint> difficultyPoints=new ArrayList<DifficultyControlPoint>();
	
	private ArrayList<EffectControlPoint> effectPoints=new ArrayList<EffectControlPoint>();
	
	private ArrayList<SampleControlPoint> samplePoints=new ArrayList<SampleControlPoint>();
	
	public ControlPoints(){
		
	}
	
	public void load(TimingPoints points){
		ArrayList<TimingPoint> res=points.getTimingPointList();
		TimingPoint tmp;
		TimingControlPoint preTp;
		DifficultyControlPoint preDcp;
		EffectControlPoint preEcp;
		SampleControlPoint preScp;
		if(res.size()<1){
			throw new IllegalArgumentException("a beatmap must has at least 1 timing point");
		}
		tmp=res.get(0);
		if(!tmp.isInherited()){
			Log.w("err-map","Beatmap's first timing point should be isInherited()");
		}
		preTp=new TimingControlPoint();
		preTp.setTime(tmp.getTime());
		preTp.setBeatLength(tmp.getBeatLength());
		preTp.setMeter(tmp.getMeter());
		timingPoints.add(preTp);
		
		preDcp=new DifficultyControlPoint();
		preDcp.setTime(tmp.getTime());
		preDcp.setSpeedMultiplier(tmp.getSpeedMultiplier());
		difficultyPoints.add(preDcp);
		
		preScp=new SampleControlPoint();
		preScp.setTime(tmp.getTime());
		samplePoints.add(preScp);
		
		preEcp=new EffectControlPoint();
		preEcp.setTime(tmp.getTime());
		preEcp.setKiaiModeOn(tmp.isKiaiMode());
		preEcp.setOmitFirstBarLine(tmp.isOmitFirstBarSignature());
		effectPoints.add(preEcp);
		
		for(int i=1;i<res.size();i++){
			tmp=res.get(i);
			if(tmp.isInherited()){
				preTp=new TimingControlPoint();
				preTp.setTime(tmp.getTime());
				preTp.setBeatLength(tmp.getBeatLength());
				preTp.setMeter(tmp.getMeter());
				timingPoints.add(preTp);
			}
			
			if(preDcp.getSpeedMultiplier()!=tmp.getSpeedMultiplier()){
				preDcp=new DifficultyControlPoint();
				preDcp.setTime(tmp.getTime());
				preDcp.setSpeedMultiplier(tmp.getSpeedMultiplier());
				difficultyPoints.add(preDcp);
			}
			
			//添加Sample相关控制点，暂时跳过
			
			if(tmp.isKiaiMode()!=preEcp.isKiaiModeOn()||tmp.isOmitFirstBarSignature()!=preEcp.isOmitFirstBarLine()){
				preEcp=new EffectControlPoint();
				preEcp.setTime(tmp.getTime());
				preEcp.setKiaiModeOn(tmp.isKiaiMode());
				preEcp.setOmitFirstBarLine(tmp.isOmitFirstBarSignature());
				effectPoints.add(preEcp);
			}
		}
		Log.v("ControlPoints","t: "+timingPoints.size());
		Log.v("ControlPoints","d: "+difficultyPoints.size());
		Log.v("ControlPoints","s: "+samplePoints.size());
		Log.v("ControlPoints","e: "+effectPoints.size());
	}
	
	public TimingControlPoint getTimingPointAt(int time){
		return binarySearch(timingPoints,time,TimingControlPoint.class);
	}
	
	public EffectControlPoint getEffectPointAt(int time){
		return binarySearch(effectPoints,time,EffectControlPoint.class);
	}
	
	public SampleControlPoint getSamplePointAt(int time){
		return binarySearch(samplePoints,time,SampleControlPoint.class);
	}
	
	public DifficultyControlPoint getDifficultyPointAt(int time){
		return binarySearch(difficultyPoints,time,DifficultyControlPoint.class);
	}
	
	private <T extends ControlPoint> T binarySearch(List<T> l,int value,Class<T> klazz){
		if(value<l.get(0).getTime()){
			return l.get(0);
		}else if(value>=l.get(l.size()-1).getTime()){
			return l.get(l.size()-1);
		}else{
			return binarySearch(l,0,l.size(),value,klazz);
		}
	}
	
	private <T extends ControlPoint> T binarySearch(List<T> l,int s,int e,int value,Class<T> klazz){
		if(e-s<=1){
			return l.get(s);
		}else{
			int m=(s+e)/2;
			T tmp=l.get(m);
			if(tmp.getTime()<=value){
				return binarySearch(l,m,e,value,klazz);
			}else{
				return binarySearch(l,s,m,value,klazz);
			}
		}
	}
}
