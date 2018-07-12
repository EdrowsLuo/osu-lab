package com.edplan.nso;

public class NsoBeatmapParsingException extends NsoException
{
	public NsoBeatmapParsingException(String msg){
		super(msg);
	}
	
	public NsoBeatmapParsingException(String msg,ParsingBeatmap info){
		super(makeText(msg,info));
	}
	
	public NsoBeatmapParsingException(String msg,ParsingBeatmap info,Throwable e){
		super(makeText(msg,info),e);
	}
	
	public static String makeText(String msg,ParsingBeatmap info){
		return "msg : "+ msg+" res : "+info.getResInfo()+"(@Line:"+info.getParsingLineIndex()+")";
	}
}
