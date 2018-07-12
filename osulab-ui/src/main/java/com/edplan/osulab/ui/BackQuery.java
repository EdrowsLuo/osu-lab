package com.edplan.osulab.ui;
import java.util.ArrayList;
import com.edplan.framework.ui.widget.component.Hideable;
import com.edplan.osulab.LabGame;

public class BackQuery
{
	private static BackQuery instance=new BackQuery();
	
	private ArrayList<Hideable> query=new ArrayList<Hideable>();
	
	private ArrayList<Hideable> noButtonQuery=new ArrayList<Hideable>();
	
	private boolean forceHideBackButton=false;

	public void setForceHideBackButton(boolean forceHideBackButton){
		this.forceHideBackButton=forceHideBackButton;
		onChange();
	}

	public boolean isForceHideBackButton(){
		return forceHideBackButton;
	}
	
	public static BackQuery get(){
		return instance;
	}
	
	public void onChange(){
		if(forceHideBackButton)return;
		if(remind()==0){
			if(!LabGame.get().getBackButton().isHidden()){
				LabGame.get().getBackButton().hide();
			}
		}else{
			if(LabGame.get().getBackButton().isHidden()){
				LabGame.get().getBackButton().show();
			}
		}
	}
	
	public void registNoBackButton(Hideable h){
		if(!query.contains(h)){
			query.add(h);
			noButtonQuery.add(h);
		}else{
			unregist(h);
			query.add(h);
			noButtonQuery.add(h);
		}
		onChange();
	}
	
	public void regist(Hideable obj){
		if(!query.contains(obj)){
			query.add(obj);
		}else{
			unregist(obj);
			query.add(obj);
		}
		onChange();
	}
	
	public void unregist(Hideable h){
		final int pre=remind();
		query.remove(h);
		noButtonQuery.remove(h);
		if(pre!=remind()){
			onChange();
		}
	}
	
	public boolean back(){
		for(int i=query.size()-1;i>=0;i--){
			final Hideable h=query.get(i);
			if(h!=null&&!h.isHidden()){
				if(h instanceof BackHandler){
					if(((BackHandler)h).onBack()){
						return true;
					}
				}
			}
			unregist(h);
			if(h!=null&&!h.isHidden()){
				h.hide();
				return true;
			}
		}
		return false;
	}
	
	public int remind(){
		return query.size()-noButtonQuery.size();
	}
	
	public interface BackHandler{
		public boolean onBack();
	}
}
