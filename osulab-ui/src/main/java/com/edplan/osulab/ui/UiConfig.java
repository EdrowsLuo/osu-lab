package com.edplan.osulab.ui;

import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.FMath;

public class UiConfig
{
	public static final String OsuFont="osu!font";
	
	public static float TOOLBAR_HEIGHT_DP=35;
	
	public static float SCENE_SELECT_BUTTON_BAR_HEIGHT=60;
	
	public static float PARALLELOGRAM_ANGEL=FMath.Pi*0.4f;
	
	public static class Color{
		public static Color4 PINK_LIGHT=Color4.rgb255(255,125,183);
		public static Color4 PINK=Color4.rgb255(233,103,161);
		public static Color4 BLUE_DEEP_DARK=Color4.rgb255(3,20,40);
		public static Color4 BLUE_DARK=Color4.rgb255(3,99,141);
		public static Color4 BLUE=Color4.rgb255(0,162,219);
		public static Color4 BLUE_LIGHT=Color4.rgb255(0,192,254);
		public static Color4 YELLOW=Color4.rgb255(255,223,78);
	}
}
