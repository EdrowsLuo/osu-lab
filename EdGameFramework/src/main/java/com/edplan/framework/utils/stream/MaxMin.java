package com.edplan.framework.utils.stream;
import java.util.Comparator;

public class MaxMin<T>
{
	private Comparator<T> comparator;
	
	private T min,max;
	
	public MaxMin(Comparator<T> comparator){
		this.comparator=comparator;
	}
	
	public void post(T t){
		if(min==null||comparator.compare(t,min)<0){
			min=t;
		}
		if(max==null||comparator.compare(t,max)>0){
			max=t;
		}
	}

	public T getMin() {
		return min;
	}

	public T getMax() {
		return max;
	}
}
