package com.edplan.osulab.ui.popup;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.additions.popupview.PopupView;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.test.performance.ui.FrameRenderMonitor;

public class RenderStatePopupView extends PopupView
{
	private static RenderStatePopupView instance;
	
	TextView text;
	
	public RenderStatePopupView(MContext c){
		super(c);
		setRounded(ViewConfiguration.dp(5));
		//.setShadow(ViewConfiguration.dp(3),Color4.rgba(0.5f,0.5f,0.5f,0.3f),Color4.Alpha);
		LinearLayout l=new LinearLayout(c);
		l.setGravity(Gravity.Center);
		l.setBackground(Color4.rgba(0,0,0,0.4f));
		RelativeLayout.RelativeParam p=new RelativeLayout.RelativeParam();
		p.width=Param.MODE_WRAP_CONTENT;
		p.height=Param.MODE_WRAP_CONTENT;
		p.gravity=Gravity.BottomRight;
		p.marginBottom=ViewConfiguration.dp(6);
		p.marginRight=ViewConfiguration.dp(6);
		text=new TextView(c);
		text.setTextSize(ViewConfiguration.dp(17));
		text.setText("time");
		setLayoutParam(p);
		{
			RelativeLayout.RelativeParam pl=new RelativeLayout.RelativeParam();
			pl.width=Param.MODE_WRAP_CONTENT;
			pl.height=Param.MODE_WRAP_CONTENT;
			pl.gravity=Gravity.BottomCenter;
			addView(l,pl);
		}
		{
			MarginLayoutParam pl=new MarginLayoutParam();
			pl.width=Param.MODE_WRAP_CONTENT;
			pl.height=Param.MODE_WRAP_CONTENT;
			pl.postMargin(ViewConfiguration.dp(6));
			pl.marginLeft=ViewConfiguration.dp(10);
			pl.marginRight=ViewConfiguration.dp(10);
			l.addView(text,pl);
		}
		setHideWhenBackpress(false);
	}

	public static void setInstance(RenderStatePopupView _instance){
		instance=_instance;
	}

	public static RenderStatePopupView getInstance(){
		return instance;
	}

	@Override
	protected void onDraw(BaseCanvas canvas){
		// TODO: Implement this method
		super.onDraw(canvas);
		//double frameTime=getContext().getFrameDeltaTime();
		text.setText(
			FrameRenderMonitor.getFPS()+"/60fps\n"
			+"PB:"+FrameRenderMonitor.possibleBlockTimes+"\n"
			+"DrawCalls:"+FrameRenderMonitor.drawCalls+"\n"
			+"Total:"+(int)FrameRenderMonitor.frameRenderTime.avg+"ms\n"
			+"UI:"+(int)FrameRenderMonitor.drawUI.avg+"ms\n"
			+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1000/1000+"/"+Runtime.getRuntime().maxMemory()/1000/1000+"MB"
		);
	}
}
