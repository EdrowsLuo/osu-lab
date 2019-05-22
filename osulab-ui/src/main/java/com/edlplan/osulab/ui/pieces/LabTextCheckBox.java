package com.edlplan.osulab.ui.pieces;

import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.widget.TextView;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.ui.text.font.bmfont.BMFont;
import com.edlplan.framework.ui.drawable.ColorDrawable;

public class LabTextCheckBox extends RelativeLayout {
    private TextView text;

    private LabCheckBox checkBox;

    public LabTextCheckBox(MContext c) {
        super(c);
        ColorDrawable bg = new ColorDrawable(c);
        bg.setColor(Color4.rgba(1f, 1f, 1f, 0.1f), Color4.rgba(1f, 1f, 1f, 0.1f),
                Color4.rgba(1f, 1f, 1f, 0.1f), Color4.rgba(1f, 1f, 1f, 0.1f));
        setBackgroundColor(bg);
        setPadding(ViewConfiguration.dp(2));
        setClickable(true);

        children(
                text = new TextView(c){{
                    setTextColor(Color4.gray(0.9f));
                    gravity(Gravity.CenterLeft);
                    setTextSize(ViewConfiguration.dp(20));
                    setFont(BMFont.Exo_20_Semi_Bold);
                    setOffsetY(ViewConfiguration.dp(-1));

                    layoutParam(
                            new RelativeLayout.RelativeParam() {{
                                height = Param.MODE_WRAP_CONTENT;
                                width = Param.makeUpDP(200);
                                marginLeft = ViewConfiguration.dp(1);
                                gravity = Gravity.CenterLeft;
                            }});
                }},

                checkBox = new LabCheckBox(c){{
                    layoutParam(
                            new RelativeLayout.RelativeParam() {{
                                height = Param.makeUpDP(7);
                                width = Param.makeUpDP(50);
                                marginRight = ViewConfiguration.dp(5);
                                gravity = Gravity.CenterRight;
                            }});
                }}
        );
    }

    public void setChecked(boolean c) {
        checkBox.setChecked(c);
    }

    public void setText(String tex) {
        text.setText(tex);
    }

    public void setOnCheckListener(OnCheckListener l) {
        checkBox.setOnCheckListener(l);
    }

    @Override
    public void onClickEvent() {

        super.onClickEvent();
        checkBox.performCheckEvent();
    }
}
