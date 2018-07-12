package com.edplan.framework.ui.widget.component;
import java.util.Arrays;
import com.edplan.framework.interfaces.Setter;
import com.edplan.framework.Framework;

public class Scroller
{
	public static int RECODE_ARRAY_SIZE=10;
	
	public static double AUTO_SCROLL_MIN_SPEED=400;
	
	public static double SCROLL_BACK_TIME_MS=400;
	
	public static double AUTO_SCROLL_SPEED_DECREASE_PER_SECOND=500;
	
	public static final int STATE_SCROLLING=1;
	
	public static final int STATE_AUTO_SCROLLING=2;
	
	public static final int STATE_SCROLL_BACK=3;
	
	public static final int STATE_STOPED=4;
	
	private boolean enableOverscroll=true;
	
	private Setter<Float> setter;
	
	private float clampWidth=200;
	
	private float startValue,endValue;
	
	private float currentValue;
	
	private int state=STATE_STOPED;
	
	private double[] recodeArray=new double[RECODE_ARRAY_SIZE*2];
	private int idx;

	public Scroller(Setter<Float> s){
		setter=s;
	}

	public void setEnableOverscroll(boolean enableOverscroll){
		this.enableOverscroll=enableOverscroll;
	}

	public boolean isEnableOverscroll(){
		return enableOverscroll;
	}
	
	public void setStartValue(float startValue){
		this.startValue=startValue;
	}

	public float getStartValue(){
		return startValue;
	}

	public void setEndValue(float endValue){
		this.endValue=endValue;
	}

	public float getEndValue(){
		return endValue;
	}
	
	public void start(float value){
		state=STATE_SCROLLING;
		currentValue=value;
	}
	
	public void add(float value,double deltaTime){
		if(idx==recodeArray.length){
			for(int i=2;i<recodeArray.length;i++){
				recodeArray[i-2]=recodeArray[i];
			}
			recodeArray[recodeArray.length-2]=value;
			recodeArray[recodeArray.length-1]=deltaTime;
		}else{
			recodeArray[idx++]=value;
			recodeArray[idx++]=deltaTime;
		}
		addValue(value);
	}
	
	protected void addValue(float v){
		float p=currentValue;
		b:{
			if(!enableOverscroll){
				currentValue+=v;
				currentValue=Math.min(endValue,Math.max(startValue,currentValue));
				break b;
			}
			if(currentValue<startValue){
				final float curoffset=startValue-currentValue;
				if(v>0){
					currentValue+=v*(2-edageFunction(curoffset/clampWidth));
				}else{
					currentValue=startValue-curoffset+v*edageFunction(curoffset/clampWidth);
				}
			}else if(currentValue>endValue){
				final float curoffset=currentValue-endValue;
				if(v<0){
					currentValue+=v*(2-edageFunction(curoffset/clampWidth));
				}else{
					currentValue=endValue+curoffset+v*edageFunction(curoffset/clampWidth);
				}
			}else{
				currentValue+=v;
			}
		}
		
		/*
		if(state!=STATE_SCROLLING&&state!=STATE_SCROLL_BACK&&Math.abs(p-currentValue)<0.1){
			state=STATE_STOPED;
		}
		*/
	}
	
	public void end(){
		if(!checkScrollBack()){
			final double spd=getAvgSpeedPreSecond();
			if(Math.abs(spd)>AUTO_SCROLL_MIN_SPEED){
				autoScroll(spd);
			}else{
				state=STATE_STOPED;
			}
		}
	}
	
	protected void flush(){
		setter.set(currentValue);
	}
	
	protected boolean checkScrollBack(){
		if(currentValue<=endValue&&currentValue>=startValue){
			return false;
		}else{
			scrollBack();
			return true;
		}
	}

	boolean isAddback;
	double scrollBackClock;
	protected void scrollBack(){
		isAddback=currentValue<startValue;
		state=STATE_SCROLL_BACK;
		scrollBackClock=Framework.relativePreciseTimeMillion();
	}
	
	protected double scrollBackSpeed(){
		return clampWidth/SCROLL_BACK_TIME_MS;
	}
	
	protected void updateScrollBack(){
		if(isAddback){
			final double pre=scrollBackClock;
			scrollBackClock=Framework.relativePreciseTimeMillion();
			addValue((float)((scrollBackClock-pre)*scrollBackSpeed()));
			if(currentValue>startValue){
				currentValue=startValue;
				state=STATE_STOPED;
			}
			flush();
		}else{
			final double pre=scrollBackClock;
			scrollBackClock=Framework.relativePreciseTimeMillion();
			addValue(-(float)((scrollBackClock-pre)*scrollBackSpeed()));
			if(currentValue<endValue){
				currentValue=endValue;
				state=STATE_STOPED;
			}
			flush();
		}
	}
	
	boolean isBacking;
	double autoScrollSpeed;
	double autoScrollClock;
	protected void autoScroll(double spd){
		isBacking=spd>0;
		autoScrollSpeed=spd;
		autoScrollClock=Framework.relativePreciseTimeMillion();
		state=STATE_AUTO_SCROLLING;
	}
	
	protected void updateAutoScroll(){
		final double pre=autoScrollClock;
		autoScrollClock=Framework.relativePreciseTimeMillion();
		final double dt=(autoScrollClock-pre)/1000;
		double dc=dt*AUTO_SCROLL_SPEED_DECREASE_PER_SECOND;
		if(isBacking){
			autoScrollSpeed-=dc;
			if(autoScrollSpeed<0){
				autoScrollSpeed=0;
				if(!checkScrollBack()){
					state=STATE_STOPED;
				}
				flush();
			}else{
				addValue((float)(autoScrollSpeed*dt));
				if(currentValue-endValue>clampWidth*0.8){
					checkScrollBack();
				}
				flush();
			}
		}else{
			autoScrollSpeed+=dc;
			if(autoScrollSpeed>0){
				autoScrollSpeed=0;
				if(!checkScrollBack()){
					state=STATE_STOPED;
				}
				flush();
			}else{
				addValue((float)(autoScrollSpeed*dt));
				if(startValue-currentValue>clampWidth*0.8){
					checkScrollBack();
				}
				flush();
			}
		}
	}
	
	public void cancel(){
		autoScrollSpeed=0;
		idx=0;
		state=STATE_STOPED;
		currentValue=Math.max(startValue,Math.min(endValue,currentValue));
	}

	public void update(){
		if(state==STATE_STOPED){
			return;
		}else if(state==STATE_AUTO_SCROLLING){
			updateAutoScroll();
		}else if(state==STATE_SCROLL_BACK){
			updateScrollBack();
		}else{
			flush();
		}
	}
	
	public double getAvgSpeedPreSecond(){
		if(idx<2)return 0;
		double time=0;
		double sum=0;
		for(int i=0;i<idx;){
			sum+=recodeArray[i++];
			time+=recodeArray[i++];
		}
		return sum/time*1000;
	}
	
	public int getState(){
		return state;
	}

	public float getCurrentValue(){
		return currentValue;
	}
	
	protected float edageFunction(float v){
		return Math.max(0,1-v);
	}
}
