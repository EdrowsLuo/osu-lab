package com.edplan.nso.storyboard.elements;

import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.Vec2;
import java.util.List;

public class Selecters {
	public static final CommandTimelineSelecter<Float> SX=new CommandTimelineSelecter<Float>(){
		@Override
		public List<TypedCommand<Float>> select(CommandTimeLineGroup group) {

			return group.X.getCommands();
		}
	};
	public static final CommandTimelineSelecter<Float> SY=new CommandTimelineSelecter<Float>(){
		@Override
		public List<TypedCommand<Float>> select(CommandTimeLineGroup group) {

			return group.Y.getCommands();
		}
	};
	public static final CommandTimelineSelecter<Float> SScaleX=new CommandTimelineSelecter<Float>(){
		@Override
		public List<TypedCommand<Float>> select(CommandTimeLineGroup group) {

			return group.ScaleX.getCommands();
		}
	};
	public static final CommandTimelineSelecter<Float> SScaleY=new CommandTimelineSelecter<Float>(){
		@Override
		public List<TypedCommand<Float>> select(CommandTimeLineGroup group) {

			return group.ScaleY.getCommands();
		}
	};
	public static final CommandTimelineSelecter<Float> SRotation=new CommandTimelineSelecter<Float>(){
		@Override
		public List<TypedCommand<Float>> select(CommandTimeLineGroup group) {

			return group.Rotation.getCommands();
		}
	};
	public static final CommandTimelineSelecter<Color4> SColour=new CommandTimelineSelecter<Color4>(){
		@Override
		public List<TypedCommand<Color4>> select(CommandTimeLineGroup group) {

			return group.Colour.getCommands();
		}
	};
	public static final CommandTimelineSelecter<Float> SAlpha=new CommandTimelineSelecter<Float>(){
		@Override
		public List<TypedCommand<Float>> select(CommandTimeLineGroup group) {

			return group.Alpha.getCommands();
		}
	};
	public static final CommandTimelineSelecter<BlendType> SBlendType=new CommandTimelineSelecter<BlendType>(){
		@Override
		public List<TypedCommand<BlendType>> select(CommandTimeLineGroup group) {

			return group.BlendingMode.getCommands();
		}
	};
	public static final CommandTimelineSelecter<Boolean> SFlipH=new CommandTimelineSelecter<Boolean>(){
		@Override
		public List<TypedCommand<Boolean>> select(CommandTimeLineGroup group) {

			return group.FlipH.getCommands();
		}
	};
	public static final CommandTimelineSelecter<Boolean> SFlipV=new CommandTimelineSelecter<Boolean>(){
		@Override
		public List<TypedCommand<Boolean>> select(CommandTimeLineGroup group) {

			return group.FlipV.getCommands();
		}
	};
}
