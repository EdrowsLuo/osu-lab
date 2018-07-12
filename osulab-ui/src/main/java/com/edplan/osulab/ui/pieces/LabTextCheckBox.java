package com.edplan.osulab.ui.pieces;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.RectF;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.ui.drawable.ColorDrawable;

public class LabTextCheckBox extends RelativeLayout
{
	private TextView text;
	
	private LabCheckBox checkBox;
	
	public LabTextCheckBox(MContext c){
		super(c);
		ColorDrawable bg=new ColorDrawable(c);
		bg.setColor(Color4.rgba(1f,1f,1f,0.1f),Color4.rgba(1f,1f,1f,0.1f),
					Color4.rgba(1f,1f,1f,0.1f),Color4.rgba(1f,1f,1f,0.1f));
		setBackground(bg);
		
		setPadding(ViewConfiguration.dp(2));
		text=new TextView(c);
		text.setTextColor(Color4.gray(0.9f));
		text.setGravity(Gravity.CenterLeft);
		text.setTextSize(ViewConfiguration.dp(20));
		text.setFont(BMFont.Exo_20_Semi_Bold);
		checkBox=new LabCheckBox(c);
		setClickable(true);
		//setDebug(true);
		{
			RelativeLayout.RelativeParam p=new RelativeLayout.RelativeParam();
			p.height=Param.MODE_WRAP_CONTENT;
			p.width=Param.makeUpDP(200);
			p.marginLeft=ViewConfiguration.dp(1);
			p.gravity=Gravity.CenterLeft;
			text.setOffsetY(ViewConfiguration.dp(-1));
			addView(text,p);
		}
		{
			RelativeLayout.RelativeParam p=new RelativeLayout.RelativeParam();
			p.height=Param.makeUpDP(7);
			p.width=Param.makeUpDP(50);
			p.marginRight=ViewConfiguration.dp(5);
			p.gravity=Gravity.CenterRight;
			//checkBox.setOffsetY(ViewConfiguration.dp(1));
			addView(checkBox,p);
		}
	}
	
	public void setChecked(boolean c){
		checkBox.setChecked(c);
	}
	
	public void setText(String tex){
		text.setText(tex);
	}
	
	public void setOnCheckListener(OnCheckListener l){
		checkBox.setOnCheckListener(l);
	}

	@Override
	public void onClickEvent(){
		// TODO: Implement this method
		super.onClickEvent();
		checkBox.performCheckEvent();
	}
}
