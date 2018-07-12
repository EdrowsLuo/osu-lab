package com.edplan.framework.resource;
import java.io.IOException;
import java.io.InputStream;

public class SubResource extends AResource
{
	private String rPath;
	
	private AResource rootRes;
	
	public SubResource(AResource res,String path){
		if(res instanceof SubResource){
			this.rootRes=((SubResource)res).getRootRes();
			this.rPath=((SubResource)res).getRPath()+"/"+path;
		}else{
			this.rootRes=res;
			this.rPath=path;
		}
	}

	public AResource getRootRes(){
		return rootRes;
	}
	
	public void setRPath(String rPath) {
		this.rPath=rPath;
	}

	public String getRPath() {
		return rPath;
	}
	
	public String toRealPath(String path){
		return getRPath()+((path!=null&&path.length()!=0)?("/"+path):"");
	}

	@Override
	public InputStream openInput(String path) throws IOException {

		return rootRes.openInput(toRealPath(path));
	}

	@Override
	public String[] list(String dir) throws IOException
	{

		return rootRes.list(toRealPath(dir));
	}

	@Override
	public boolean contain(String file) {

		return rootRes.contain(toRealPath(file));
	}
}
