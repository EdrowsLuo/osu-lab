package com.edplan.nso.beatmapComponent;
import java.util.ArrayList;
import com.edplan.superutils.interfaces.StringMakeable;
import android.graphics.Color;
import com.edplan.superutils.U;
import java.util.TreeMap;
import java.util.Map;
import com.edplan.framework.graphics.opengl.objs.Color4;

public class Colours implements StringMakeable
{
	/**
	 *SliderBorder : 255,255,255
	 *SliderTrackOverride : 0,0,0
	 */
	
	private TreeMap<Integer,Integer> colours;
	
	private Color4 sliderBorder=Color4.White.copyNew();
	
	private Color4 sliderTrackOverride=Color4.White.copyNew();
	
	public Colours(){
		colours=new TreeMap<Integer,Integer>();
	}

	public void setSliderBorder(Color4 sliderBorder) {
		this.sliderBorder.set(sliderBorder);
	}

	public Color4 getSliderBorder() {
		return sliderBorder;
	}

	public void setSliderTrackOverride(Color4 sliderTrackOverride) {
		this.sliderTrackOverride.set(sliderTrackOverride);
	}

	public Color4 getSliderTrackOverride() {
		return sliderTrackOverride;
	}
	
	public void addColour(int index,int c){
		colours.put(index,c);
	}

	@Override
	public String makeString(){

		StringBuilder sb=new StringBuilder();
		int i;
		for(Map.Entry<Integer,Integer> e:colours.entrySet()){
			i=e.getValue();
			sb.append("Combo").append(e.getKey()).append(" : ");
			sb.append(Color.red(i)).append(",");
			sb.append(Color.green(i)).append(",");
			sb.append(Color.blue(i)).append(U.NEXT_LINE);
		}
		return sb.toString();
	}
}
