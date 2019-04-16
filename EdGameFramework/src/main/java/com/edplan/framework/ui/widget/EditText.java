package com.edplan.framework.ui.widget;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.drawable.sprite.ColorRectSprite;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.utils.systemops.IntentHelper;

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
