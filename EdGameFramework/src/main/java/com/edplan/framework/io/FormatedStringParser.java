package com.edplan.framework.io;
import com.edplan.superutils.U;

/**
 *对于 键值+分割符1+数据 为单元，中间用统一的分割符2进行单元之间的分割
 */
public class FormatedStringParser
{
	/**
	 *@field unitSpliter : 单元与单元之间的分割符
	 *@filed dataSpliter : 单元里数据的分割符
	 */
	private char unitSpliter=' ';
	private char dataSpliter='=';

	private StringWrapType stringWrapType=StringWrapType.NONE;

	private String source;
	
	private int index=0;
	
	private boolean reachEnd=false;
	
	public FormatedStringParser(String s){
		source=s;
	}

	public void setStringWrapType(StringWrapType stringWrapType) {
		this.stringWrapType=stringWrapType;
	}

	public StringWrapType getStringWrapType() {
		return stringWrapType;
	}
	
	public int nextIntUnit(String tag){
		int nextUnitSpl=source.indexOf(unitSpliter,index);
		if(nextUnitSpl==-1)nextUnitSpl=source.length();
		int dataSplIndex=index+tag.length();
		if(dataSplIndex>=source.length()||source.charAt(dataSplIndex)!=dataSpliter){
			throw new RuntimeException("err tag or index");
		}
		index=nextUnitSpl+1;
		return U.toInt(source.substring(dataSplIndex+1,nextUnitSpl));
	}
	
	public int[] nextIntList(String tag,String splter){
		int nextUnitSpl=source.indexOf(unitSpliter,index);
		if(nextUnitSpl==-1)nextUnitSpl=source.length();
		int dataSplIndex=index+tag.length();
		if(dataSplIndex>=source.length()||source.charAt(dataSplIndex)!=dataSpliter){
			throw new RuntimeException("err tag or index");
		}
		index=nextUnitSpl+1;
		String data=source.substring(dataSplIndex+1,nextUnitSpl);
		String[] ary=data.split(splter);
		int[] list=new int[ary.length];
		for(int i=0;i<list.length;i++){
			list[i]=Integer.parseInt(ary[i]);
		}
		return list;
	}
	
	public String nextStringUnit(String tag){
		int dataSplIndex=index+tag.length();
		if(dataSplIndex>=source.length()||source.charAt(dataSplIndex)!=dataSpliter){
			throw new RuntimeException("err tag or index");
		}
		int nextUnitSpl=0;
		switch(stringWrapType){
			case NONE:
				nextUnitSpl=source.indexOf(unitSpliter,index);
				if(nextUnitSpl==-1)nextUnitSpl=source.length();
				index=nextUnitSpl+1;
				return source.substring(dataSplIndex+1,nextUnitSpl);
			case WITH_QUOTATION:
				int ptr=dataSplIndex+1;
				if(source.charAt(ptr)=='"'){
					ptr++;
					while(source.charAt(ptr)!='"'&&ptr<source.length()){
						ptr++;
					}
					ptr++;
				}
				nextUnitSpl=ptr;
				index=nextUnitSpl+1;
				return source.substring(dataSplIndex+2,nextUnitSpl-1);
		}
		return null;
	}
	
	public void moveToNextUnit(){
		while(index<source.length()&&source.charAt(index)!=unitSpliter){
			index++;
		}
		if(source.charAt(index)==unitSpliter){
			index++;
		}
	}
	
	public boolean hasReachEnd(){
		return reachEnd;
	}
	
	public boolean moveStep(int i){
		if(hasReachEnd())return false;
		index+=i;
		if(index>=source.length()){
			index=source.length();
			reachEnd=true;
			return false;
		}
		return true;
	}
	
	public void setUnitSpliter(char unitSpliter) {
		this.unitSpliter=unitSpliter;
	}

	public char getUnitSpliter() {
		return unitSpliter;
	}

	public void setDataSpliter(char dataSpliter) {
		this.dataSpliter=dataSpliter;
	}

	public char getDataSpliter() {
		return dataSpliter;
	}
	
	public enum StringWrapType{
		NONE,WITH_QUOTATION
	}
}
