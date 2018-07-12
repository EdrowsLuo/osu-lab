package com.edplan.superutils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

public class AdvancedBufferedReader
{
	protected BufferedReader reader;
	
	protected String bufferedString=null;
	protected String nowString=null;
	protected boolean hasEnd=false;
	protected int lineCount=0;
	
	public AdvancedBufferedReader(BufferedReader r){
		reader=r;
		prepare();
	}
	
	public AdvancedBufferedReader(File f) throws FileNotFoundException{
		this(new FileReader(f));
	}
	
	public AdvancedBufferedReader(InputStream in){
		this(new InputStreamReader(in));
	}
	
	public AdvancedBufferedReader(Reader r){
		this(new BufferedReader(r));
	}
	
	private void prepare(){
		try {
			nowString=null;
			bufferedString=reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getLineCount(){
		return lineCount;
	}
	
	public void bufferToNext() throws IOException{
		nowString=bufferedString;
		bufferedString=reader.readLine();
		if(nowString==null){
			hasEnd=true;
		}else{
			lineCount++;
		}
	}
	
	public String readLine() throws IOException{
		if(hasEnd())return null;
		bufferToNext();
		return nowString;
	}
	
	public String getNowString(){
		return nowString;
	}
	
	public String getBufferedString(){
		return bufferedString;
	}
	
	public boolean hasEnd(){
		return hasEnd;
	}
}
