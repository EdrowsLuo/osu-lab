package com.edplan.nso.storyboard;
import com.edplan.nso.storyboard.elements.IStoryboardElements;
import java.util.ArrayList;
import java.util.List;

public class StoryboardLayer
{
	private String name;
	private int depth;
	private boolean enableWhenPassing=true;
	private boolean enableWhenFailing=true;
	
	private List<IStoryboardElements> elements=new ArrayList<IStoryboardElements>();
	
	public StoryboardLayer(String name,int depth,boolean enableWhenPassing,boolean enableWhenFailing){
		this.name=name;
		this.depth=depth;
		this.enableWhenPassing=enableWhenPassing;
		this.enableWhenFailing=enableWhenFailing;
	}

	public void setElements(List<IStoryboardElements> elements) {
		this.elements=elements;
	}

	public List<IStoryboardElements> getElements() {
		return elements;
	}
	
	public int getElementsCount(){
		return elements.size();
	}
	
	public void add(IStoryboardElements e){
		elements.add(e);
	}
	
	public void add(StoryboardLayer l){
		for(IStoryboardElements e:l.elements){
			add(e);
		}
	}
	
}
