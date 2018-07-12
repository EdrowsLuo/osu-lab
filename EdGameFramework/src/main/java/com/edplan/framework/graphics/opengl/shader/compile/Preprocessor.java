package com.edplan.framework.graphics.opengl.shader.compile;
import com.edplan.framework.utils.StringUtil;

import java.util.Arrays;

/**
 *主要用于处理库函数什么的
 *扩展块需要以"/**"行注解开始，
 *以"** /"行注解结束（删除空格）
 *或者以//*或@开始的单行命令
 *（可以有前导空格，但是这行里不能有其他东西）
 *最后生成的代码会替代这个注解区域
 *
 *
 *在块里面支持以下操作：
 *
 *name <@String/name>    ：设置当前的块名称
 *
 *include <@String/name> ：添加一个文件，文件需要被包括在RawStringStore里
 *
 *
 *
 *
 *
 */
public class Preprocessor
{
	public static final String TYPE_NAME="name";
	
	public static final String TYPE_INCLUDE="include";
	
	private CompileRawStringStore store;
	
	private String programName=null;
	
	private String res;
	
	private String[] lines;
	
	private String result;
	
	private boolean hasChange=false;
	
	public Preprocessor(String res,CompileRawStringStore store){
		this.res=res;
		this.store=store;
	}
	
	public Preprocessor(String res){
		this.res=res;
		store=CompileRawStringStore.get();
	}

	public void setProgramName(String programName){
		this.programName=programName;
	}

	public String getProgramName(){
		return programName;
	}
	
	public Preprocessor compile(){
		lines=res.split(StringUtil.LINE_BREAK);
		boolean isHandlingBlock=false;
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<lines.length;i++){
			String trimd=lines[i].trim();
			if(trimd.length()==0){
				sb.append(lines[i]).append(StringUtil.LINE_BREAK);
				continue;
			}
			if(trimd.charAt(0)=='@'){
				hasChange=true;
				final String specs=lines[i].substring(0,lines[i].length()-trimd.length());
				final String command=trimd.substring(trimd.indexOf('@')+1,trimd.length()).trim();
				handleCommand(command,specs,sb);
				continue;
			}else if(trimd.startsWith("//* ")){
				hasChange=true;
				final String specs=lines[i].substring(0,lines[i].length()-trimd.length());
				final String command=trimd.substring(trimd.indexOf(' ')+1,trimd.length()).trim();
				handleCommand(command,specs,sb);
				continue;
			}
			if(isHandlingBlock){
				if(trimd.equals("**/")){
					//结束块
					isHandlingBlock=false;
					sb.append(StringUtil.LINE_BREAK);
					continue;
				}
				//获取前缀，添加到后面的所有行
				final String specs=lines[i].substring(0,lines[i].length()-trimd.length());
				final String command=trimd;
				handleCommand(command,specs,sb);
			}else{
				if(lines[i].trim().equals("/**")){
					//标志块开始
					hasChange=true;
					isHandlingBlock=true;
					sb.append(lines[i].substring(0,lines[i].trim().length())).append(StringUtil.LINE_BREAK);
				}else{
					sb.append(lines[i]).append(StringUtil.LINE_BREAK);
				}
			}
		}
		result=sb.toString();
		return this;
	}
	
	public boolean hasChange(){
		return hasChange;
	}
	
	public String getResult(){
		return result;
	}
	
	private void handleCommand(String command,String specs,StringBuilder sb){
		final String type=command.substring(0,command.indexOf(' '));
		switch(type){
			case TYPE_INCLUDE:{
					final String fileName=getStringData(command);
					final String[] addLines=store.getDatas(fileName);
					for(String s:addLines){
						sb.append(specs).append(s).append(StringUtil.LINE_BREAK);
					}
				}
				break;
			case TYPE_NAME:{
					if(programName!=null){
						throw new PreCompileException("you can only set name once");
					}
					programName=getStringData(command);
				}
				break;
		}
	}
	
	private static String getStringData(String command){
		return command.substring(command.indexOf('<')+1,command.indexOf('>'));
	}
}
