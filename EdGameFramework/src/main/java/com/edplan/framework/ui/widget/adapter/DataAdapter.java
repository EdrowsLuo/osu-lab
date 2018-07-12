package com.edplan.framework.ui.widget.adapter;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter<T>
{
	private ArrayList<T> list=new ArrayList<T>();
	
	private Callback<T> callback;
	
	public void setDatas(List<T> data){
		list.clear();
		list.addAll(data);
		callback.onDatasetUpdate(this);
	}
	
	public void appendData(T data){
		list.add(data);
		callback.onDataAdd(this,list.size()-1);
	}

	public void setCallback(Callback<T> callback){
		this.callback=callback;
	}
	
	public interface Callback<T>{
		
		/**
		 *几种情况：
		 *1：整体数据更新
		 * onDatasetUpdate -> end;
		 *
		 *2：单个数据更新
		 * onSingleDataChange -> end
		 *
		 *3：插入一个数据在末尾
		 * onDataAdd -> end;
		 *
		 *4：删除一个数据
		 * onDataRemove -> onDataIndexChange -> end;
		 *
		 *5：
		 * 
		 */
		
		
		/**
		 *一般是在大量数据发生改动的时候被回调
		 */
		public void onDatasetUpdate(DataAdapter<T> adpt);
		
		/**
		 *单个数据发生变动
		 */
		public void onSingleDataupdate(DataAdapter<T> adpt,int position);
		
		/**
		 *单个数据被删除
		 */
		public void onDataRemove(DataAdapter<T> adpt,int position);
		
		/**
		 *数据的位置发生改变
		 */
		public void onDataIndexChange(DataAdapter<T> adpt,T data,int preIndex,int nowIndex);
		
		/**
		 *添加单个数据到position
		 */
		public void onDataAdd(DataAdapter<T> adpt,int position);
	}
}
