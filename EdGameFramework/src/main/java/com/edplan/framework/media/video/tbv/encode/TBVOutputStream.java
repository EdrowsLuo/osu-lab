package com.edplan.framework.media.video.tbv.encode;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import org.json.JSONObject;

public class TBVOutputStream
{
	private DataOutputStream out;
	
	public TBVOutputStream(DataOutputStream out){
		this.out=out;
	}
	
	public void writeBoolean(boolean b) throws IOException{
		out.writeBoolean(b);
	}
	
	public void writeShort(short s) throws IOException{
		out.writeShort(s);
	}
	
	public void writeInt(int i) throws IOException{
		out.writeInt(i);
	}
	
	public void writeFloat(float f) throws IOException{
		out.writeFloat(f);
	}
	
	public void writeDouble(double d) throws IOException{
		out.writeDouble(d);
	}
	
	public void writeBytes(byte[] bytes,int offset,int len) throws IOException{
		out.write(bytes,offset,len);
	}
	
	public void writeByteArray(byte[] ary) throws IOException{
		out.write(ary);
	}
	
	public void writeString(String s) throws IOException{
		try {
			byte[] bytes=s.getBytes("UTF-8");
			writeInt(bytes.length);
			writeBytes(bytes,0,bytes.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void writeARGB255(Color4 color) throws IOException{
		writeInt(color.toIntBit());
	}
	
	public void writeTextureVertex3D(TextureVertex3D v) throws IOException{
		writeFloat(v.position.x);
		writeFloat(v.position.y);
		writeFloat(v.position.z);
		writeARGB255(v.color.toPremultipled());
		writeFloat(v.texturePoint.x);
		writeFloat(v.texturePoint.y);
	}
	
	public void writeJSONObject(JSONObject obj) throws IOException{
		writeString(obj.toString());
	}
}
