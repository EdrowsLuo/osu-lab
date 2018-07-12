package com.edplan.framework.graphics.opengl.shader;

public class ShaderSetInfo
{
	public String vertexShaderPath;
	
	public String premultipledFSPath;
	
	public String noPremultipledFSPath;
	
	public ShaderSetInfo(String head){
		vertexShaderPath=head+".vs";
		premultipledFSPath=head+".fs";
		noPremultipledFSPath=head+".npm.fs";
	}
}
