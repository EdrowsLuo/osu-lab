package com.edplan.osulab.ui;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.drawable.ColorDrawable;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import java.io.IOException;
import com.edplan.framework.ui.drawable.sprite.TextureSprite;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.math.RectF;
import com.edplan.osulab.ui.popup.PopupToast;
import com.edplan.framework.ui.widget.RelativeLayout;

public class MainBackground extends RelativeLayout
{
	private AbstractTexture testTexture;
	
	private TextureSprite textureSprite;
	
	public MainBackground(MContext c){
		super(c);
		ColorDrawable cd=new ColorDrawable(c);
		float t=0.1f,b=0.1f;
		cd.setColor(Color4.gray(t),Color4.gray(t),
					Color4.gray(b),Color4.gray(b));
		setBackground(cd);
		
		textureSprite=new TextureSprite(c);
		
		try{
			testTexture=getContext().getAssetResource().subResource("osu/ui").loadTexture("menu-background-1.jpg");
		}catch(IOException e){
			e.printStackTrace();
			PopupToast.toast(getContext(),"err "+e.getMessage()).show();
		}
	}

	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		super.onDraw(canvas);
		if(testTexture!=null){
			textureSprite.setAlpha(0.5f);
			textureSprite.setTexture(testTexture);
			textureSprite.setAreaFillTexture(RectF.xywh(0,0,canvas.getWidth(),canvas.getHeight()));
			textureSprite.draw(canvas);
		}
	}
	
}
