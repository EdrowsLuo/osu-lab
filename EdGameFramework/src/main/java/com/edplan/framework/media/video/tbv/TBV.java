package com.edplan.framework.media.video.tbv;

import com.edplan.framework.graphics.opengl.BlendType;
import com.edplan.framework.media.video.tbv.decode.TBVInputStream;
import com.edplan.framework.media.video.tbv.encode.TBVOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import com.edplan.framework.utils.Tag;
import com.edplan.framework.graphics.opengl.objs.GLTexture;

/**
 *一种渲染材质组成的视频，将所有信息存在文件里然后播放
 *
 *文件格式：.tbv
 *
 *数据格式(v0.0.0)：
 * String：长度(int)+长度个byte组成的string，编码：UTF-8
 * Json：就是一个String
 * Color255：一个int表示的颜色
 * Boolean：一个usign byte，0为false，其余为true
 * BlendType(short)：渲染模式
 * BaseVertex：基础顶点，x(float)+y(float)+z(float)+顶点颜色(Color255)
 * TextureVertex：描述材质的顶点，BaseVertex+textureX(float)+textureY(float)
 * RenderData：描述绘制的顶点数组，按三角形存储
 * BaseTexture：纹理id(int)+RenderData
 *
 *HEADER：
 * String：固定为TextureBasedVideo，用来判断文件种类
 * Json：基础数据用Json来表示
 * xxxx：保存纹理映射
 *
 *BODY：
 * BODY由许多帧数据组成
 *	每一帧包含帧头数据以及许多事件，以帧结束事件结束
 *
 *  帧头数据：
 *    帧Flag(short)，判断特殊事件的（如为开头帧或者为结束帧)
 *    本帧的数据长度(int)（为0表示跳过）
 *    本帧是否要重绘(boolean)
 *    帧开始时间(float) 结束时间(float)
 *
 *  事件种类：
 *    帧结束（-1）
 *    #绘制操作
 *    画板操作(0)
 *    绘制材质（1）
 *    绘制色块（2）
 *    #end 绘制操作
 *    #util
 *    切换设置（1000）
 *    #end util
 *
 *  基础格式：事件种类(short)+事件对应的数据
 *
 *  详细：
 *    绘制材质：跟一个BaseTexture数据，并绘制
 *    绘制色块：无材质的绘制
 *    切换设置：设置种类(short)+具体数据
 *    帧结束：无对应数据
 *
 */


public class TBV 
{
	
	public static final int VERSION=0;
	public static final String HEADER_TYPE="TextureBasedVideo";

	public static class Header{
		public String type;
		public int width;
		public int height;
		public JSONObject jsonData;
		public TextureNode[] textures;
		//用来映射到实际的
		@Tag("no-write-no-read") public HashMap<Integer,Integer> textureIdReflects;
		
		public HashMap<Integer,Integer> getTextureReflections(){
			if(textureIdReflects==null)textureIdReflects=new HashMap<Integer,Integer>();
			return textureIdReflects;
		}

		public static Header read(TBVInputStream in,Header h) throws IOException, TBVException, JSONException{
			if(h==null)h=new Header();
			h.type=in.readString();
			if(!HEADER_TYPE.equals(h.type)){
				throw new TBVException("err type: "+h.type);
			}
			h.width=in.readInt();
			h.height=in.readInt();
			h.jsonData=in.readJSONObject();
			int textureCount=in.readInt();
			h.textures=new TextureNode[textureCount];
			for(int i=0;i<textureCount;i++){
				TextureNode node=new TextureNode();
				node.id=i;
				node.texture=in.readString();
				h.textures[i]=node;
			}
			return h;
		}

		public static void write(TBVOutputStream out,Header h) throws TBVException, IOException{
			if(!HEADER_TYPE.equals(h.type)){
				throw new TBVException("err type: "+h.type);
			}
			out.writeString(h.type);
			out.writeInt(h.width);
			out.writeInt(h.height);
			out.writeJSONObject(h.jsonData);
			out.writeInt(h.textures.length);
			for(int i=0;i<h.textures.length;i++){
				out.writeString(h.textures[i].texture);
			}
		}
		
		public static class Builder{
			public ArrayList<TextureNode> textures=new ArrayList<TextureNode>();
			public Header target;
			
			public Builder setSize(int width,int height){
				target.width=width;
				target.height=height;
				return this;
			}
			
			public Builder setJson(JSONObject obj){
				target.jsonData=obj;
				return this;
			}
			
			public Builder setJson(String json) throws JSONException{
				return setJson(new JSONObject(json));
			}
			
			public int addTexture(String path,GLTexture texture){
				int id=textures.size();
				final TextureNode node=new TextureNode();
				node.texture=path;
				node.id=id;
				textures.add(node);
				target.getTextureReflections().put(texture.getTextureId(),id);
				return id;
			}
			
			public Header build(){
				target.textures=new TextureNode[textures.size()];
				for(int i=0;i<textures.size();i++){
					target.textures[i]=textures.get(i);
				}
				return target;
			}
			
			public static Builder create(Header target){
				if(target==null)target=new Header();
				Builder b=new Builder();
				b.target=target;
				b.target.type=HEADER_TYPE;
				return b;
			}
		}
	}

	public static class FrameHeader{
		public short flag;
		public float startTime;
		public float endTime;
		public boolean clearCanvas;
		public int blockSize;

		public static FrameHeader read(TBVInputStream in,FrameHeader h) throws IOException{
			if(h==null)h=new FrameHeader();
			h.flag=in.readShort();
			h.startTime=in.readFloat();
			h.endTime=in.readFloat();
			h.clearCanvas=in.readBoolean();
			h.blockSize=in.readInt();
			return h;
		}

		public static void write(TBVOutputStream out,FrameHeader h) throws IOException{
			out.writeShort(h.flag);
			out.writeFloat(h.startTime);
			out.writeFloat(h.endTime);
			out.writeBoolean(h.clearCanvas);
			out.writeInt(h.blockSize);
		}
	}
	
	public static class EventHeader{
		public short eventType;
		
		public static EventHeader read(TBVInputStream in,EventHeader e) throws IOException{
			if(e==null)e=new EventHeader();
			e.eventType=in.readShort();
			return e;
		}
		
		public static void write(TBVOutputStream out,EventHeader h) throws IOException{
			out.writeShort(h.eventType);
		}
	}
	
	
	public static class Frame{
		public static final short NORMAL_FRAME=1;
		public static final short START_FRAME=2;
		public static final short END_FRAME=-1;
	}
	
	public static class FrameEvent{
		public static final short FRAME_END=-1;
		public static final short CANVAS_OPERATION=0;
		public static final short DRAW_BASE_TEXTURE=1;
		public static final short DRAW_COLOR_VERTEX=2;
		public static final short CHANGE_SETTING=1000;
	}
	
	public static class CanvasOperation{
		public static final short CLEAR_COLOR_BUFFER=1;
		public static final short CLEAR_DEPTH_BUFFER=2;
		public static final short CLEAR_COLOR_AND_DEPTHBUFFER=3;
	}
	
	public static class SettingEvent{
		public static final short CHANGE_BLEND_TYPE=1;
		public static final short CHANGE_DEPTH_TEST=2;
		
		public static final int DATA_SIZE=32;
		
		public short type;
		
		public byte[] data;
		
		public SettingEvent(){
			data=new byte[32];
		}
		
		public void setBlend(boolean enable,BlendType btype){
			type=CHANGE_BLEND_TYPE;
			data[0]=(byte)(enable?1:0);
			data[1]=(byte)(btype.ordinal());
		}
		
		public static SettingEvent read(TBVInputStream in,SettingEvent e) throws IOException{
			if(e==null)e=new SettingEvent();
			e.type=in.readShort();
			in.readByteArray(e.data);
			return e;
		}
		
		public static void write(TBVOutputStream out,SettingEvent e) throws IOException{
			out.writeShort(e.type);
			out.writeByteArray(e.data);
		}
		
	}
}
