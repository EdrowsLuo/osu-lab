package com.edlplan.osulab.ui.pieces;

import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.widget.TextView;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.text.font.bmfont.BMFont;
import com.edlplan.framework.ui.ViewConfiguration;

public class TextButton extends LabButton {
    private TextView textView;

    public TextButton(MContext c) {
        super(c);
        float padding = ViewConfiguration.dp(6);
        setPaddingLeft(padding * 3);
        setPaddingRight(padding * 3);
        setPaddingTop(padding);
        setPaddingBottom(padding);

        children(
                textView = new TextView(c) {{
                    setFont(BMFont.Exo_20_Semi_Bold);
                    setTextSize(ViewConfiguration.dp(16));
                    layoutParam(
                            new RelativeLayout.RelativeParam() {{
                                width = Param.MODE_WRAP_CONTENT;
                                height = Param.MODE_WRAP_CONTENT;
                                gravity = Gravity.Center;
                            }});
                }}
        );
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setText(String text) {
        textView.setText(text);
    }

}
