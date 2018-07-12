package com.edplan.framework.utils;
import java.util.Collection;
import com.edplan.framework.interfaces.InvokeGetter;

public class CollectionUtil
{
	public static <T> double getMaxDouble(Collection<T> coll,InvokeGetter<T,Double> getter){
		double max=Double.MIN_VALUE;
		for(T t:coll){
			max=Math.max(max,getter.invoke(t));
		}
		return max;
	}
}
