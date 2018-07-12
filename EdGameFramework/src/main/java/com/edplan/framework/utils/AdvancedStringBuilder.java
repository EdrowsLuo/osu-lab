package com.edplan.framework.utils;

public class AdvancedStringBuilder
{
	public final char NEXT_LNE='\n';
	
	public final char WHITE_SPACE=' ';
	
	private StringBuilder sb;
	
	public AdvancedStringBuilder(){
		sb=new StringBuilder();
	}
	
	public AdvancedStringBuilder(String text){
		sb=new StringBuilder(text);
	}
	
	public AdvancedStringBuilder append(String text){
		sb.append(text);
		return this;
	}
	
	public AdvancedStringBuilder append(char c){
		sb.append(c);
		return this;
	}
	
	public AdvancedStringBuilder append(int i){
		sb.append(i);
		return this;
	}
	
	public AdvancedStringBuilder append(float f){
		sb.append(f);
		return this;
	}
	
	public AdvancedStringBuilder append(long l){
		sb.append(l);
		return this;
	}
	
	public AdvancedStringBuilder append(double d){
		sb.append(d);
		return this;
	}
	
	public AdvancedStringBuilder nextLine(){
		return append(NEXT_LNE);
	}
	
	public AdvancedStringBuilder addWhiteSpace(){
		return append(WHITE_SPACE);
	}
	
	public AdvancedStringBuilder addWhiteSpace(int count){
		for(int i=0;i<count;i++){
			append(WHITE_SPACE);
		}
		return this;
	}
	
	public AdvancedStringBuilder append(Object obj){
		sb.append(obj);
		return this;
	}

	@Override
	public String toString() {

		return sb.toString();
	}
}
