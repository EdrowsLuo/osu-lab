package com.edplan.framework.database;

import com.edplan.framework.database.annotation.DBIgnore;
import com.edplan.framework.database.annotation.PrimaryKeyAutoIncrement;
import com.edplan.framework.database.annotation.TableName;

@TableName("test")
public class TestDBLine extends DatabaseLine
{
	@PrimaryKeyAutoIncrement
	public int _id;
	
	public long setId;
	
	public String data;
	
	@DBIgnore
	public Object o;
}
