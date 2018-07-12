package com.edplan.framework.database;

public enum DataType
{
	Int("INTEGER"),
	Long("INTEGER"),
	Real("REAL"),
	Text("TEXT");
	
	public final String SQLType;
	
	DataType(String s){
		SQLType=s;
	}
}
