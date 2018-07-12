package com.edplan.osulab.ui.pieces;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.ui.ViewConfiguration;

public class TextButton extends LabButton
{
	private TextView textView;
	
	public TextButton(MContext c){
		super(c);
		float padding=ViewConfiguration.dp(6);
		setPaddingLeft(padding*3);
		setPaddingRight(padding*3);
		setPaddingTop(padding);
		setPaddingBottom(padding);
		textView=new TextView(c);
		textView.setFont(BMFont.Exo_20_Semi_Bold);
		textView.setTextSize(ViewConfiguration.dp(16));
		RelativeLayout.RelativeParam param=new RelativeLayout.RelativeParam();
		param.width=Param.MODE_WRAP_CONTENT;
		param.height=Param.MODE_WRAP_CONTENT;
		param.gravity=Gravity.Center;
		addView(textView,param);
	}

	public void setTextView(TextView textView){
		this.textView=textView;
	}

	public TextView getTextView(){
		return textView;
	}
	
	public void setText(String text){
		textView.setText(text);
	}
	
}
