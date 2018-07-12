package com.edplan.framework.utils;
import java.util.Arrays;

public class BadMap<K,V>
{
	private Object[] keys=new Object[16];
	private Object[] values=new Object[16];
	private int size;
	
	public int size(){
		return size;
	}
	
	public V getValueByIndex(int idx){
		if(idx<values.length)return (V)values[idx];
		return null;
	}
	
	public V get(Object k){
		int idx=getKeyIndex(k);
		if(idx<keys.length){
			return (V)values[idx];
		}else{
			return null;
		}
	}
	
	public boolean containsKey(K k){
		for(Object o:keys){
			if(o==null)continue;
			if(o.equals(k))return true;
		}
		return false;
	}
	
	public int getValueIndex(Object k){
		for(int i=0;i<size;i++){
			final Object o=values[i];
			if(o==null)continue;
			if(o==k)return i;
		}
		return size;
	}
	
	public int getKeyIndex(Object k){
		for(int i=0;i<size;i++){
			final Object o=keys[i];
			if(o==null)continue;
			if(o.equals(k))return i;
		}
		return size;
	}
	
	private void directPut(int idx,K key,V value){
		if(idx>=keys.length){
			keys=Arrays.copyOf(keys,idx+4);
			values=Arrays.copyOf(values,idx+4);
		}
		keys[idx]=key;
		values[idx]=value;
		size=Math.max(idx+1,size);
	}
	
	public void put(K key,V value){
		int idx=getKeyIndex(key);
		directPut(idx,key,value);
	}
	
	public boolean containsValue(V v){
		for(Object o:values){
			if(o==v)return true;
		}
		return false;
	}
}
