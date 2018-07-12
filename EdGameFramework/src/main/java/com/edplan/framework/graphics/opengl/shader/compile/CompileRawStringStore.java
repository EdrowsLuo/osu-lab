package com.edplan.framework.graphics.opengl.shader.compile;
import java.util.HashMap;
import com.edplan.framework.utils.StringUtil;

public class CompileRawStringStore
{
	public static CompileRawStringStore DEFAULT;
	
	static{
		DEFAULT=new CompileRawStringStore();
	}
	
	private HashMap<String,ProgramNode> map;
	
	public CompileRawStringStore(){
		map=new HashMap<String,ProgramNode>();
	}
	
	public String[] getDatas(String name){
		return map.get(name).lines;
	}
	
	public void addToStore(String program){
		Preprocessor comp=new Preprocessor(program,this);
		comp.compile();
		if(comp.getProgramName()==null){
			throw new PreCompileException(
				"you can't add a no name program to store, \n"
				+"use \"name <@String/name>\" to set name");
		}
		if(map.containsKey(comp.getProgramName())){
			throw new PreCompileException("program @"+comp.getProgramName()+" already exists");
		}
		map.put(comp.getProgramName(),new ProgramNode(comp.getProgramName(),comp.getResult().split(StringUtil.LINE_BREAK)));
	}
	
	
	public static CompileRawStringStore get(){
		return DEFAULT;
	}
	
	public class ProgramNode{
		public String name;
		public String[] lines;
		
		public ProgramNode(String name,String[] lines){
			this.name=name;
			this.lines=lines;
		}
	}
}
