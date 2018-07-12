package com.edplan.framework.ui.text.font.bmfont;
import com.edplan.superutils.U;
import java.util.ArrayList;
import java.util.List;

public class FNTHelper
{
	public static String parseString(String head,int adv,String res){
		String s=res.substring(head.length()+adv);
		return s.substring(1,s.length()-1);
	}
	
	public static String[] removeEmpty(String[] raw){
		List<String> ret=new ArrayList<String>(raw.length);
		for(String s:raw){
			if(!s.isEmpty()){
				ret.add(s);
			}
		}
		String[] data=new String[ret.size()];
		return ret.toArray(data);
	}
	
	public static int parseInt(String head,int adv,String res){
		return U.toInt(res.substring(head.length()+adv).trim());
	}
	
	public static int[] parseIntArray(String head,int adv,String res){
		String[] list=res.substring(head.length()+adv).trim().split(",");
		int[] ilist=new int[list.length];
		for(int i=0;i<ilist.length;i++){
			ilist[i]=U.toInt(list[i]);
		}
		return ilist;
	}
}
