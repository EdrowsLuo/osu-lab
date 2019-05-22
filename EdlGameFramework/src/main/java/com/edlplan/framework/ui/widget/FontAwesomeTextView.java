package com.edlplan.framework.ui.widget;

import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.text.font.FontAwesome;
import com.edlplan.framework.ui.text.font.bmfont.BMFont;

public class FontAwesomeTextView extends TextView {
    public FontAwesomeTextView(MContext c) {
        super(c);
        setFont(BMFont.getFont(BMFont.FontAwesome));
        setTextSize(getFont().getCommon().lineHeight * 0.6f);
    }

    public void setIcon(FontAwesome fontAwesome) {
        setText(fontAwesome.charvalue + "");
    }

    @Override
    public void onInitialLayouted() {

        super.onInitialLayouted();
        setTextSize(Math.min(getWidth(), getHeight()));
    }

    @Override
    protected void onLayout(boolean changed, float left, float top, float right, float bottom) {

        super.onLayout(changed, left, top, right, bottom);
    }
}
