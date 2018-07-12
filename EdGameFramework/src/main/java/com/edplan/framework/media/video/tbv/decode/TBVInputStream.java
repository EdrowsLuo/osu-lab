package com.edplan.framework.media.video.tbv.decode;
import java.io.DataInputStream;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONException;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.TextureVertex3D;
import com.edplan.framework.graphics.opengl.BlendType;

public class TBVInputStream
{
	private DataInputStream in;
	
	public TBVInputStream(DataInputStream in){
		this.in=in;
	}
	
	public int readInt() throws IOException{
		return in.readInt();
	}
	
	public long readLong() throws IOException{
		return in.readLong();
	}
	
	public short readShort() throws IOException{
		return in.readShort();
	}
	
	public byte[] readByteArray(int length) throws IOException{
		byte[] buffer=new byte[length];
		in.readFully(buffer);
		return buffer;
	}
	
	public void readByteArray(byte[] ary) throws IOException{
		in.readFully(ary);
	}
	
	public boolean readBoolean() throws IOException{
		return in.readBoolean();
	}
	
	public float readFloat() throws IOException{
		return in.readFloat();
	}
	
	public double readDouble() throws IOException{
		return in.readDouble();
	}
	
	public Color4 readARGB255() throws IOException{
		return Color4.argb255(readInt());
	}
	
	public void readTextureVertex3D(TextureVertex3D vertex) throws IOException{
		float x=readFloat();
		float y=readFloat();
		float z=readFloat();
		int color=readInt();
		float tx=readFloat();
		float ty=readFloat();
		vertex.position.set(x,y,z);
		vertex.color.set(color,true);
		vertex.texturePoint.set(tx,ty);
		return;
	}
	
	public BlendType readBlendType() throws IOException{
		int i=readInt();
		return (i<0||i>=BlendTypes.length)?BlendType.Normal:BlendTypes[i];
	}
	
	public String readString() throws IOException{
		int byteLength=readInt();
		System.out.println("length:"+byteLength);
		byte[] bytes=readByteArray(byteLength);
		return new String(bytes,"UTF-8");
	}
	
	public JSONObject readJSONObject() throws JSONException, IOException{
		return new JSONObject(readString());
	}
	
	public void skip(int size) throws IOException{
		in.skipBytes(size);
	}
	
	public void mark(int buffer){
		in.mark(buffer);
	}
	
	public void reset() throws IOException{
		in.reset();
	}
	
	public static BlendType[] BlendTypes=new BlendType[]{
		BlendType.Normal,
		BlendType.Additive,
		BlendType.Delete
	};
	
	
}
