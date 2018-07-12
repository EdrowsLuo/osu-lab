package com.edplan.framework.ui;

public class ViewTree
{
	
	public class FocusList{
		public Node first;
		
		public void add(EdView view){
			
		}
		
		public class Node{
			public Node pre;
			public EdView view;
			public float l,t,r,b;
			public Node next;
		}
	}
}
