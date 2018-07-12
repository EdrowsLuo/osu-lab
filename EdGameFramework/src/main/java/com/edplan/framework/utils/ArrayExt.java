package com.edplan.framework.utils;
import com.edplan.framework.interfaces.ArrayConstructor;

public class ArrayExt
{
	public static <T> T[] expend(T[] raw,int targetSize,Constructor<T> constructor,ArrayConstructor<T> aryConstructor){
		if(targetSize>raw.length){
			T[] r=aryConstructor.create(targetSize);
			for(int i=0;i<raw.length;i++){
				r[i]=raw[i];
			}
			if(constructor!=null)for(int i=raw.length;i<targetSize;i++){
				r[i]=constructor.createNewObject();
			}
			return r;
		}else{
			return raw;
		}
	}
	
	public static <T> void swap(T[] ary,T[] another,int i,int j){
		T tmp=ary[i];
		ary[i]=another[j];
		another[j]=tmp;
	}
}
