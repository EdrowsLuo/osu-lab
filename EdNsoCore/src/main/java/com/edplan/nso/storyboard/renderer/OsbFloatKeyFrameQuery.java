package com.edplan.nso.storyboard.renderer;
import com.edplan.framework.ui.animation.Easing;
import com.edplan.nso.storyboard.elements.TypedCommand;
import com.edplan.nso.storyboard.renderer.OsbRenderer.IFloatMainFrameHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

public class OsbFloatKeyFrameQuery
{
	public static Comparator<TypedCommand> COMPARATOR=new Comparator<TypedCommand>(){
		@Override
		public int compare(TypedCommand p1,TypedCommand p2){

			return (int)Math.signum(p1.getStartTime()-p2.getStartTime());
		}
	};
	
	private final OsbFloatKeyFrame[] frames;
	private final float[] nextFrameStartTime;
	private final int stopFlag;
	private int currentFrameIndex;
	private OsbFloatKeyFrame currentFrame;
	private final IFloatMainFrameHandler handler;
	
	public OsbFloatKeyFrameQuery(List<TypedCommand<Float>> commands,IFloatMainFrameHandler handler,float expandMinTime,float defaultValue,boolean initialWithStartValue){
		this.handler=handler;
		Collections.sort(commands,COMPARATOR);
		final List<TypedCommand<Float>> list=new ArrayList<TypedCommand<Float>>();
		if(commands.size()==0){
			list.add(new TypedCommand<Float>(Easing.None,expandMinTime,expandMinTime,defaultValue,defaultValue));
		}else{
			if(!initialWithStartValue&&expandMinTime<commands.get(0).getStartTime()){
				list.add(new TypedCommand<Float>(Easing.None,expandMinTime,commands.get(0).getStartTime(),defaultValue,defaultValue));
			}
			for(int i=0;i<commands.size();i++){
				final TypedCommand<Float> command=commands.get(i);
				list.add(command);
			}
		}
		Collections.fill(commands,null);
		commands=list;
		frames=new OsbFloatKeyFrame[commands.size()];
		nextFrameStartTime=new float[frames.length];
		for(int i=0;i<frames.length;i++){
			final TypedCommand<Float> command=commands.get(i);
			frames[i]=new OsbFloatKeyFrame(command.getStartValue(),command.getEndValue(),(float)command.getStartTime(),(float)command.getDuration(),command.getEasing());
		}
		for(int i=1;i<frames.length;i++){
			nextFrameStartTime[i-1]=frames[i].startTime;
		}
		nextFrameStartTime[frames.length-1]=Float.MAX_VALUE;
		currentFrameIndex=0;
		currentFrame=frames[0];
		stopFlag=frames.length-1;
		Collections.fill(list,null);
	}
	
	public float calLazyCurrentValue(double time){
		if(time<currentFrame.startTime){
			return currentFrame.startValue;
		}else if(time>currentFrame.endTime||currentFrame.duration==0){
			return currentFrame.endValue;
		}else{
			//return FMath.mix(currentFrame.startValue,currentFrame.endValue,EasingManager.apply(currentFrame.easing
			return (currentFrame.startValue+currentFrame.endValue)/2;
		}
	}
	
	public boolean isSmall(double time){
		return Math.abs(calLazyCurrentValue(time))<0.0005;
	}
	
	private boolean hasChange;
	
	public void update(double time){
		hasChange=false;
		while(nextFrameStartTime[currentFrameIndex]<time&&currentFrameIndex<stopFlag){
			currentFrame=frames[++currentFrameIndex];
			hasChange=true;
		}
		if(hasChange){
			handler.update(currentFrame.startValue,currentFrame.endValue,currentFrame.startTime,currentFrame.duration,currentFrame.easing);
		}
	}
	
	public void dispos(){
		Arrays.fill(frames,null);
	}
}
