package com.edplan.framework.utils;
import java.util.ArrayList;

public class Observable<T>
{
	private T data;
	
	private ArrayList<OnChangeListener<T>> listeners;
	
	protected ArrayList<OnChangeListener<T>> getListeners(){
		if(listeners==null)listeners=new ArrayList<OnChangeListener<T>>();
		return listeners;
	}
	
	public void registerListener(OnChangeListener<T> l){
		if(!getListeners().contains(l)){
			getListeners().add(l);
		}
	}
	
	public void unregisterListener(OnChangeListener<T> l){
		getListeners().remove(l);
	}

	public void setData(T data){
		if(listeners==null){
			this.data=data;
			return;
		}
		
		synchronized(getListeners()){
			this.data=data;
			for(OnChangeListener<T> l:getListeners()){
				l.onChange(data);
			}
		}
	}

	public T getData(){
		return data;
	}
}
