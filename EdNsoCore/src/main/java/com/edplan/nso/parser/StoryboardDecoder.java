package com.edplan.nso.parser;

import com.edplan.nso.ParsingBeatmap;
import com.edplan.superutils.AdvancedBufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.edplan.nso.parser.partParsers.StoryboardPartParser;
import com.edplan.nso.filepart.PartEvents;
import com.edplan.nso.filepart.PartVariables;
import com.edplan.nso.storyboard.Storyboard;

public class StoryboardDecoder extends BaseDecoder 
{
	StoryboardPartParser parser;
	
	public StoryboardDecoder(InputStream in,String resInfo){
		super(in,resInfo);
		initialParsers();
	}

	public StoryboardDecoder(File file) throws FileNotFoundException{
		super(file);
		initialParsers();
	}


	public StoryboardDecoder(AdvancedBufferedReader _reader,ParsingBeatmap bdmsg){
		super(_reader,bdmsg);
		initialParsers();
	}
	
	
	
	private void initialParsers(){
		parser=new StoryboardPartParser(parsingBeatmap);
		addParser(PartEvents.TAG,parser);
		addParser(PartVariables.TAG,parser.variableDecoder);
	}
	
	public Storyboard getStoryboard(){
		return parser.getStoryboard();
	}
}
