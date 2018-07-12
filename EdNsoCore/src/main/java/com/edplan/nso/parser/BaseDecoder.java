package com.edplan.nso.parser;

import android.util.Log;
import com.edplan.nso.NsoBeatmapParsingException;
import com.edplan.nso.NsoException;
import com.edplan.nso.ParsingBeatmap;
import com.edplan.nso.parser.partParsers.PartParser;
import com.edplan.superutils.AdvancedBufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import com.edplan.framework.utils.MLog;
import java.util.Arrays;
import java.io.UnsupportedEncodingException;

public class BaseDecoder 
{
	public static String FORMAT_LINE_HEAD="osu file format v";

	protected AdvancedBufferedReader reader;

	protected ParsingBeatmap parsingBeatmap;
	
	private Map<String,PartParser> parsers=new HashMap<String,PartParser>();

	protected PartParser nowParser=null;
	
	//protected boolean enableProgressNotice=true;
	
	//protected int totalLine;
	
	protected int parsedLine;
	
	public BaseDecoder(InputStream in,String resInfo){
		AdvancedBufferedReader r=new AdvancedBufferedReader(in);
		ParsingBeatmap b=new ParsingBeatmap();
		b.setResInfo(resInfo!=null?resInfo:"unknow");
		initial(r,b);
	}

	public BaseDecoder(File file) throws FileNotFoundException{
		AdvancedBufferedReader br=new AdvancedBufferedReader(file);
		ParsingBeatmap pb=new ParsingBeatmap();
		pb.setResInfo("file://"+file.getAbsolutePath());
		initial(br,pb);
	}

	public BaseDecoder(AdvancedBufferedReader _reader,ParsingBeatmap bdmsg){
		initial(_reader,bdmsg);
	}
	
	private final void initial(AdvancedBufferedReader _reader,ParsingBeatmap bdmsg){
		this.reader=_reader;
		this.parsingBeatmap=bdmsg;
	}
	
	public void addParser(String tag,PartParser parser){
		parsers.put(tag,parser);
	}
	
	public void parse() throws IOException, NsoBeatmapParsingException, NsoException{
		onParse();
		String tagTmp=null;
		while(true){
			nextLine();
			if(reader.hasEnd()){
				break;
			}
			onParseLine(parsingBeatmap.getParsingLineIndex(),reader.getNowString());
			tagTmp=parseTag(reader.getNowString());
			if(tagTmp!=null){
				nowParser=parsers.get(tagTmp);
				if(nowParser==null){
					throw new NsoBeatmapParsingException("Invalid tag : "+tagTmp,parsingBeatmap);
				}
				onSelectParser(nowParser);
			}else{
				if(nowParser!=null){
					try{
						if(!nowParser.parse(reader.getNowString())){
							throw new NsoBeatmapParsingException("\nParser:"+nowParser.getClass().getName()+"\nParse line err: "+nowParser.getErrMessage()+" \nres: "+reader.getNowString(), parsingBeatmap);
						}
					}
					catch(NsoException e){
						throw e;
					}
				}
			} 
		}
	}
	
	protected void onParseLine(int line,String l){
		
	}

	protected void onParse() throws NsoException,NsoBeatmapParsingException,IOException{
		
	}

	protected void onSelectParser(PartParser parser) throws NsoException,NsoBeatmapParsingException,IOException{
		
	}
	
	protected void nextLine() throws IOException{
		reader.bufferToNext();
		parsingBeatmap.nextLine();
		parsedLine++;
	}

	public static String reparseTag(String t){
		StringBuilder sb=new StringBuilder("[");
		sb.append(t).append("]");
		return sb.toString();
	}

	public static String parseTag(String s){
		s=s.trim();
		if(s.isEmpty()){
			return null;
		}else{
			if(s.charAt(0)=='['&&s.charAt(s.length()-1)==']'){
				return s.substring(1,s.length()-1);
			}else{
				return null;
			}
		}
	}

	public static String reparseFormatLine(int f){
		return (new StringBuilder(FORMAT_LINE_HEAD)).append(f).toString();
	}

	public static int parseFormatLine(String s){
		if (s.contains(FORMAT_LINE_HEAD)) {
			try{
				String[] l=s.split("v");
				return Integer.parseInt(l[l.length-1]);
			}catch(NumberFormatException e){
				e.printStackTrace();
				Log.w("parsing format line",e.getMessage());
				return -1;
			}
		}else{
			return -1;
		}
	}
}
