package com.edplan.nso.storyboard.elements;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.utils.stream.MaxMin;
import com.edplan.framework.graphics.opengl.BlendType;
import java.util.List;
import java.util.ArrayList;

public class CommandTimeLineGroup
{
	public CommandTimeLine<Float> X=new CommandTimeLine<Float>();
	public CommandTimeLine<Float> Y=new CommandTimeLine<Float>();
	public CommandTimeLine<Float> ScaleX=new CommandTimeLine<Float>();
	public CommandTimeLine<Float> ScaleY=new CommandTimeLine<Float>();
	public CommandTimeLine<Float> Rotation=new CommandTimeLine<Float>();
	public CommandTimeLine<Color4> Colour=new CommandTimeLine<Color4>();
	public CommandTimeLine<Float> Alpha=new CommandTimeLine<Float>();
	public CommandTimeLine<BlendType> BlendingMode=new CommandTimeLine<BlendType>();
	public CommandTimeLine<Boolean> FlipH=new CommandTimeLine<Boolean>();
	public CommandTimeLine<Boolean> FlipV=new CommandTimeLine<Boolean>();
	
	private CommandTimeLine[] timeLines={X,Y,ScaleX,ScaleY,Rotation,Colour,Alpha,BlendingMode,FlipH,FlipV};
	
	public <T> List<TypedCommand<T>> getCommands(CommandTimelineSelecter<T> timeline,double offset){
		if(offset!=0){
			final List<TypedCommand<T>> res=timeline.select(this);
			final List<TypedCommand<T>> list=new ArrayList<TypedCommand<T>>(res.size());
			for(TypedCommand<T> c:res){
				list.add(
					new TypedCommand<T>(
						c.getEasing(),
						c.getStartTime()+offset,
						c.getEndTime()+offset,
						c.getStartValue(),
						c.getEndValue()));
			}
			return list;
		}else{
			return timeline.select(this);
		}
	}
	
	
	
	public boolean hasCommands(){
		for(CommandTimeLine c:timeLines){
			if(c.hasCommands())return true;
		}
		return false;
	}
	
	public double getCommandsStartTime(){
		double startTime=Double.MAX_VALUE;
		for(CommandTimeLine t:timeLines){
			if(t.hasCommands()){
				if(t.getStartTime()<startTime){
					startTime=t.getStartTime();
				}
			}
		}
		return startTime;
	}
	
	public double getCommandsEndTime(){
		double endTime=Double.MIN_VALUE;
		for(CommandTimeLine t:timeLines){
			if(t.hasCommands()){
				if(t.getEndTime()>endTime){
					endTime=t.getEndTime();
				}
			}
		}
		return endTime;
	}
	
	public double getCommandsDuration(){
		return getCommandsEndTime()-getCommandsStartTime();
	}
	
	public double getStartTime(){
		return getCommandsStartTime();
	}
	
	public double getEndTime(){
		return getCommandsEndTime();
	}
	
	
}
