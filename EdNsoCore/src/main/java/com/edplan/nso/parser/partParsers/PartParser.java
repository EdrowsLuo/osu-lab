package com.edplan.nso.parser.partParsers;
import com.edplan.nso.parser.LinesParser;
import com.edplan.nso.OsuFilePart;

public abstract class PartParser<T extends OsuFilePart> implements LinesParser
{
	private String errMessage;
	
	public abstract T getPart();
	
	protected void setErrMessage(String msg){
		errMessage=msg;
	}
	
	public String getErrMessage(){
		return errMessage;
	}
}
