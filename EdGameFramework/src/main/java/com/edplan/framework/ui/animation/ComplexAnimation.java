package com.edplan.framework.ui.animation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.edplan.framework.utils.CollectionUtil;
import com.edplan.framework.interfaces.InvokeGetter;
import java.util.Iterator;
import com.edplan.framework.test.TestStaticData;

public class ComplexAnimation extends BaseAnimation
{
	private ArrayList<Node> nodes=new ArrayList<Node>();

	@Override
	public void start(){

		super.start();
		for(Node n:nodes){
			n.anim.start();
		}
	}
	
	@Override
	public void postProgressTime(double deltaTime){

		super.postProgressTime(deltaTime);
		final double time=getProgressTime();
		final Iterator<Node> iter=nodes.iterator();
		while(true){
			if(iter.hasNext()){
				final Node node=iter.next();
				if(node.startTime>time){
					continue;
				}else if(node.endTime<time){
					if(!node.hasEnd){
						node.hasEnd=true;
						AnimationHandler.handleSingleAnima(node.anim,deltaTime);
					}
					continue;
				}else{
					AnimationHandler.handleSingleAnima(node.anim,deltaTime);
				}
			}else{
				break;
			}
		}
	}

	@Override
	public void setProgressTime(double p){

		super.setProgressTime(p);
		final double time=getProgressTime();
		final Iterator<Node> iter=nodes.iterator();
		while(true){
			if(iter.hasNext()){
				final Node node=iter.next();
				final double pt=time-node.startTime;
				if(pt>node.duration){
					continue;
				}else if(pt<0){
					continue;
				}else{
					node.anim.setProgressTime(pt);
				}
			}else{
				break;
			}
		}
	}
	
	public void addAnimation(AbstractAnimation anim,double startTime){
		nodes.add(new Node(startTime,anim));
	}
	
	public void build(){
		Collections.sort(nodes,new Comparator<Node>(){
				@Override
				public int compare(ComplexAnimation.Node p1,ComplexAnimation.Node p2){

					return (int)Math.signum(p1.startTime-p2.startTime);
				}
			});
		setDuration(CollectionUtil.getMaxDouble(nodes,new InvokeGetter<Node,Double>(){
							@Override
							public Double invoke(ComplexAnimation.Node t){

								return t.endTime;
							}
						}));
	}
	
	public static class Node implements Comparable<Node>{
		public final double startTime;
		public final double duration;
		public final double endTime;
		public final AbstractAnimation anim;
		public boolean hasEnd=false;
		public Node(double startTime,AbstractAnimation anim){
			this.startTime=startTime;
			this.duration=anim.getDuration();
			this.endTime=startTime+duration;
			this.anim=anim;
		}

		@Override
		public int compareTo(ComplexAnimation.Node p1){

			return (int)Math.signum(startTime-p1.startTime);
		}
	}
}
