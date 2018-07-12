package com.edplan.nso.resource;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.resource.AResource;
import com.edplan.nso.resource.annotation.AResPath;
import com.edplan.nso.resource.annotation.AResType;
import com.edplan.nso.ruleset.std.playing.IComboColorGenerater;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import com.edplan.nso.ruleset.std.playing.BaseComboColorGenerater;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.nso.resource.annotation.AResList;
import com.edplan.nso.resource.annotation.AFontCharList;
import com.edplan.framework.resource.ResException;
import android.util.Log;

public class OsuSkin
{
	private HashMap<String,ResourceInfo> resourceDictionary=new HashMap<String,ResourceInfo>();
	
	private AResource resource;
	
	@AResType(ResType.TEXTURE)
	@AResPath("hitcircle.png")
	public TextureInfo hitcircle;
	
	@AResType(ResType.TEXTURE)
	@AResPath("hitcircleoverlay.png")
	public TextureInfo hitcircleOverlay;
	
	@AResType(ResType.TEXTURE)
	@AResPath("approachcircle.png")
	public TextureInfo approachCircle;
	
	@AResType(ResType.TEXTURE)
	@AResPath("followpoint.png")
	public TextureInfo followpoint;
	
	
	@AResType(ResType.TEXTUREFONT)
	@AResPath("default")
	@AResList({"0","1","2","3","4","5","6","7","8","9","x","dot","comma","percent"})
	@AFontCharList({'0','1','2','3','4','5','6','7','8','9','x','.',',','%'})
	public TextureFontInfo defaultTextureFont;
	
	@AResType(ResType.FRAMETEXTURE)
	@AResPath("sliderb")
	public FrameTextureInfo sliderb;
	
	public IComboColorGenerater comboColorGenerater;
	
	//public TextureDrawable hitcircleDrawable;
	
	public OsuSkin(){
		initial();
	}

	public void setResource(AResource resource) {
		this.resource=resource;
	}

	public AResource getResource() {
		return resource;
	}
	
	public void initial(){
		comboColorGenerater=
			BaseComboColorGenerater
				 .createBy(
				  Color4.rgb(1,0.2f,0.2f),
				  Color4.rgb(0.2f,1,0.2f),
				  Color4.rgb(0.2f,0.2f,1),
				  Color4.rgb(1,1,1),
				  Color4.rgb(1,1,0.2f),
				  Color4.rgb(1,0.2f,1),
				  Color4.rgb(0.2f,1,1));
		try{
			Class klass=OsuSkin.class;
			Field[] fields=klass.getFields();
			for(Field f:fields){
				//Log.v("load","check "+f);
				if(f.isAnnotationPresent(AResType.class)){
					//Log.v("load","check ok. "+f);
					//Log.v("load","parse annotation. "+f);
					AResType type=f.getAnnotation(AResType.class);
					AResPath path=f.getAnnotation(AResPath.class);
					f.setAccessible(true);
					switch(type.value()){
						case TEXTURE:
							f.set(this,new TextureInfo(path.value()));
							//Log.v("load","create info {"+path+"}. "+f);
							break;
						case TEXTUREFONT:
							f.set(this,new TextureFontInfo(path.value()));
							break;
						case FRAMETEXTURE:
							f.set(this,new FrameTextureInfo(path.value()));
							break;
						default:break;
					}
					resourceDictionary.put(f.getName(),(ResourceInfo)f.get(this));
				}
			}
		}catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new OsuResException("reflect err illaccs: "+e.getMessage(),e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new OsuResException("reflect err illagr: "+e.getMessage(),e);
		}
	}
	
	public void load(AResource res){
		this.resource=res;
		try{
			Class klass=OsuSkin.class;
			Field[] fields=klass.getFields();
			for(Field f:fields){
				if(f.isAnnotationPresent(AResType.class)){
					AResType type=f.getAnnotation(AResType.class);
					f.setAccessible(true);
					switch(type.value()){
						case TEXTURE:
							TextureInfo info=(TextureInfo)f.get(this);
							try {
								loadTextureRes(info, res);
							} catch (IOException e) {
								e.printStackTrace();
								throw new OsuResException("res io err : "+e.getMessage(),e);
							}
							break;
						case TEXTUREFONT:
							TextureFontInfo fontinfo=(TextureFontInfo)f.get(this);
							try {
								loadTextureFontRes(fontinfo,res,f.getAnnotation(AResList.class).value(),f.getAnnotation(AFontCharList.class).value());
							} catch (IOException e) {
								e.printStackTrace();
								throw new OsuResException("res io err : "+e.getMessage(),e);
							}
							break;
						case FRAMETEXTURE:
							FrameTextureInfo frameinfo=(FrameTextureInfo)f.get(this);
							try {
								loadFrameTextureRes(frameinfo,res);
							} catch (IOException e) {
								e.printStackTrace();
								throw new OsuResException("res io err : "+e.getMessage(),e);
							}
							break;
						default:break;
					}
				}
			}
		}catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new OsuResException("reflect err illaccs: "+e.getMessage(),e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new OsuResException("reflect err illagr: "+e.getMessage(),e);
		}
	}
	
	public void loadTextureRes(TextureInfo info,AResource res) throws IOException{
		info.setTexture(GLTexture.decodeResource(res,info.getPath()));
	}
	
	public void loadTextureFontRes(TextureFontInfo info,AResource res,String[] tag,char[] chars) throws IOException{
		for(int i=0;i<tag.length;i++){
			info.addToPool(chars[i],GLTexture.decodeResource(res,info.getPath()+'-'+tag[i]+".png"));
		}
	}
	
	public void loadFrameTextureRes(FrameTextureInfo info,AResource res)throws IOException{
		int index=0;
		String path=info.getPath()+index+".png";
		GLTexture curTexture=GLTexture.decodeResource(res,path);
		if(curTexture==null)throw new ResException("no texture found");
		do{
			info.addFrame(curTexture);
			index++;
			path=info.getPath()+index+".png";
			curTexture=GLTexture.decodeResource(res,path);
		}while(curTexture!=null);
		Log.v("frame-test",info.getRes().frameCount()+"");
	}
}
