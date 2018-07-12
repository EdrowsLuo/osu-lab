package com.edplan.nso.storyboard;
import com.edplan.framework.math.Vec2;
import java.util.HashMap;
import com.edplan.framework.ui.Anchor;
import java.util.Map;
import com.edplan.nso.NsoBeatmap;

public class Storyboard
{
	private HashMap<String,StoryboardLayer> layers=new HashMap<String,StoryboardLayer>();
	
	public Storyboard(){
		layers.put(Layer.Background.name(),Layer.Background.createLayer());
		layers.put(Layer.Fail.name(),Layer.Fail.createLayer());
		layers.put(Layer.Pass.name(),Layer.Pass.createLayer());
		layers.put(Layer.Foreground.name(),Layer.Foreground.createLayer());
	}
	
	public void applyBeatmap(NsoBeatmap beatmap){
		if(beatmap.getStoryboard()!=null){
			append(beatmap.getStoryboard());
		}
	}
	
	public void append(Storyboard sb){
		for(Map.Entry<String,StoryboardLayer> l:sb.layers.entrySet()){
			if(layers.containsKey(l.getKey())){
				layers.get(l.getKey()).add(l.getValue());
			}else{
				layers.put(l.getKey(),Layer.valueOf(l.getKey()).createLayer());
				layers.get(l.getKey()).add(l.getValue());
			}
		}
	}

	public HashMap<String, StoryboardLayer> getLayers() {
		return layers;
	}
	
	public StoryboardLayer getLayer(String name){
		return layers.get(name);
	}
	
	public int getObjectCount(){
		int c=0;
		for(StoryboardLayer l:layers.values()){
			c+=l.getElementsCount();
		}
		return c;
	}
	
	public enum EventObjType{
		Background(0),
		Video(1),
		Break(2),
		Colour(3),
		Sprite(4),
		Sample(5),
		Animation(6);
		private final int value;
		EventObjType(int v){
			value=v;
		}

		public static EventObjType parse(String s){
			if(s.length()==0)return null;
			char f=s.charAt(0);
			if(f>='0'&&f<='6'){
				return EventObjType.values()[f-'0'];
			}else{
				return EventObjType.valueOf(s);
			}
		}
	}
	
	public enum Layer{
		Background(3,true,true),
		Fail(2,false,true),
		Pass(1,true,false),
		Foreground(0,true,true);
		private final int depth;
		private final boolean enableWhenPassing;
		private final boolean enableWhenFailing;
		Layer(int depth,boolean ewp,boolean ewf){
			this.depth=depth;
			this.enableWhenPassing=ewp;
			this.enableWhenFailing=ewf;
		}
		
		public StoryboardLayer createLayer(){
			return new StoryboardLayer(name(),depth,enableWhenPassing,enableWhenFailing);
		}
		
		public static Layer parse(String s){
			if(s.length()==0)return null;
			char f=s.charAt(0);
			if(f>='0'&&f<='3'){
				return Layer.values()[f-'0'];
			}else{
				return Layer.valueOf(s);
			}
		}
	}
	
	public enum AnimationLoopType{
		LoopOnce,
		LoopForever;
	}
	
	public enum Origin{
		TopLeft(Anchor.TopLeft),
		Centre(Anchor.Center),
		CentreLeft(Anchor.CenterLeft),
		TopRight(Anchor.TopRight),
		BottomCentre(Anchor.BottomCenter),
		TopCentre(Anchor.TopCenter),
		Custom(Anchor.Center),
		CentreRight(Anchor.CenterRight),
		BottomLeft(Anchor.BottomLeft),
		BottomRight(Anchor.BottomRight);
		private final Anchor value;
		Origin(Anchor anchor){
			value=anchor;
		}
		
		public Anchor getAnchor(){
			return value;
		}
		
		public static Origin parse(String s){
			return valueOf(s);
		}
	}
}
