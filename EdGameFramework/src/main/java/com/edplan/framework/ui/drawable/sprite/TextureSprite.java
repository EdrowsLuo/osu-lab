package com.edplan.framework.ui.drawable.sprite;
import com.edplan.framework.MContext;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.Anchor;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;

public class TextureSprite extends BaseRectTextureSprite<TextureSpriteShader>
{
	public TextureSprite(MContext c){
		super(c);
	}

	public void setAreaFitTexture(RectF rect){
		float w=rect.getWidth()/getTexture().getWidth();
		float h=rect.getHeight()/getTexture().getHeight();
		if(w>h){
			setArea(RectF.anchorOWH(Anchor.Center,rect.getCenterHorizon(),rect.getCenterVertical(),rect.getHeight()*getTexture().getWidth()/getTexture().getHeight(),rect.getHeight()));
		}else{
			setArea(RectF.anchorOWH(Anchor.Center,rect.getCenterHorizon(),rect.getCenterVertical(),rect.getWidth(),rect.getWidth()*getTexture().getHeight()/getTexture().getWidth()));
		}
	}
	
	public void setAreaFillTexture(RectF rect){
		float w=rect.getWidth()/getTexture().getWidth();
		float h=rect.getHeight()/getTexture().getHeight();
		if(w<h){
			setArea(RectF.anchorOWH(Anchor.Center,rect.getCenterHorizon(),rect.getCenterVertical(),rect.getHeight()*getTexture().getWidth()/getTexture().getHeight(),rect.getHeight()));
		}else{
			setArea(RectF.anchorOWH(Anchor.Center,rect.getCenterHorizon(),rect.getCenterVertical(),rect.getWidth(),rect.getWidth()*getTexture().getHeight()/getTexture().getWidth()));
		}
	}
	
	@Override
	protected TextureSpriteShader createShader(){

		return TextureSpriteShader.get();
	}
}
