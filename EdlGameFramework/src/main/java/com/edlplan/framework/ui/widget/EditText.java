package com.edlplan.framework.ui.widget;

import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.utils.systemops.IntentHelper;
import com.edlplan.framework.ui.layout.MarginLayoutParam;
import com.edlplan.framework.ui.layout.Orientation;
import com.edlplan.framework.ui.layout.Param;

public class EditText extends LinearLayout {

    private TextView textView;

    private ColorRectSprite editLine;

    public EditText(MContext c) {
        super(c);
        setOrientation(Orientation.DIRECTION_T2B);
        addAll(
                new EdView(c) {{
                    layoutParam(
                            new MarginLayoutParam() {{
                                width = Param.makeUpDP(60);
                                height = Param.makeUpDP(0);
                            }}
                    );
                }},
                textView = new TextView(c) {{
                    gravity(Gravity.Center);
                    layoutParam(
                            new MarginLayoutParam() {{
                                width = Param.MODE_WRAP_CONTENT;
                                height = Param.MODE_WRAP_CONTENT;
                            }}
                    );
                    setText("");
                }}
        );
        editLine = new ColorRectSprite(c);
        editLine.setAccentColor(Color4.White);

        setClickable(true);
    }

    @Override
    public void onClickEvent() {
        super.onClickEvent();
        IntentHelper.requireTextInput(
                getContext(),
                textView.getText(),
                s -> getContext().runOnUIThread(() -> textView.setText(s))
        );
    }

    public TextView getTextView() {
        return textView;
    }

    public ColorRectSprite getEditLine() {
        return editLine;
    }

    @Override
    protected void onDrawOverlayLayer(BaseCanvas canvas) {
        super.onDrawOverlayLayer(canvas);
        editLine.setArea(RectF.xywh(0, canvas.getHeight() - ViewConfiguration.dp(2), canvas.getWidth(), ViewConfiguration.dp(2)));
        editLine.draw(canvas);
    }
}
