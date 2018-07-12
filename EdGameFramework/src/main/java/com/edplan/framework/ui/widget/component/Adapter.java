package com.edplan.framework.ui.widget.component;
import com.edplan.framework.ui.EdView;

public interface Adapter<T>
{
	public void onDataChange();
	
	public boolean isEmpty();
	
	public int getObjCount();
	
	public Node<T> getObjAt(int idx);
	
	public int getViewTypeCount();
	
	public EdView inflate(int groupId);
	
	public void onShow(int idx,Node<T> node,EdView view);
	
	public void onHidden(int idx,Node<T> node,EdView view);
	
	public void inject(int idx,Node<T> node,EdView view);
	
	public static class Node<T>{
		private T obj;
		
		private long id;
		
		private int viewType;

		public void setViewType(int viewType){
			this.viewType=viewType;
		}

		public int getViewType(){
			return viewType;
		}

		public void setId(long id){
			this.id=id;
		}

		public long getId(){
			return id;
		}

		public void setObj(T obj){
			this.obj=obj;
		}

		public T getObj(){
			return obj;
		}
	}
}
