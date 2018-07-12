package com.edplan.superutils.interfaces;

public abstract class Loopable
{

	public void setFlag(Flag flag) {
		this.flag=flag;
	}

	public Flag getFlag() {
		return flag;
	}
	public enum Flag{
		Run,Skip,Stop
	}
	
	private Flag flag=Flag.Run;
	
	private AbstractLooper looper;
	
	public void setLooper(AbstractLooper lp){
		this.looper=lp;
	}
	
	public AbstractLooper getLooper(){
		return looper;
	}
	
	public void onRemove(){
		
	}
	
	public abstract void onLoop(double deltaTime);
}
