package com.edlplan.framework.ui.additions.popupview.defviews;

import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.test.performance.ui.FrameRenderMonitor;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.additions.popupview.PopupView;
import com.edlplan.framework.ui.layout.MarginLayoutParam;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.LinearLayout;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.TextView;

public class RenderStatPopupView extends PopupView {
    private static RenderStatPopupView instance;

    TextView text;

    boolean lowDetailMode = false;

    public RenderStatPopupView(MContext c) {
        super(c);
        //setRounded(ViewConfiguration.dp(5));
        setBackgroundRoundedRect(
                Color4.rgba(0, 0, 0, 0.4f),
                ViewConfiguration.dp(5));
        //.setShadow(ViewConfiguration.dp(3),Color4.rgba(0.5f,0.5f,0.5f,0.3f),Color4.Alpha);
        LinearLayout l = new LinearLayout(c);
        l.setGravity(Gravity.Center);
        //l.setBackgroundColor(Color4.rgba(0, 0, 0, 0.4f));
        RelativeLayout.RelativeParam p = new RelativeLayout.RelativeParam();
        p.width = Param.MODE_WRAP_CONTENT;
        p.height = Param.MODE_WRAP_CONTENT;
        p.gravity = Gravity.BottomRight;
        p.marginBottom = ViewConfiguration.dp(6);
        p.marginRight = ViewConfiguration.dp(6);
        text = new TextView(c);
        text.setTextSize(ViewConfiguration.dp(17));
        text.setText("time");
        setLayoutParam(p);
        {
            RelativeLayout.RelativeParam pl = new RelativeLayout.RelativeParam();
            pl.width = Param.MODE_WRAP_CONTENT;
            pl.height = Param.MODE_WRAP_CONTENT;
            pl.gravity = Gravity.BottomCenter;
            addView(l, pl);
        }
        {
            MarginLayoutParam pl = new MarginLayoutParam();
            pl.width = Param.MODE_WRAP_CONTENT;
            pl.height = Param.MODE_WRAP_CONTENT;
            pl.postMargin(ViewConfiguration.dp(6));
            pl.marginLeft = ViewConfiguration.dp(10);
            pl.marginRight = ViewConfiguration.dp(10);
            l.addView(text, pl);
        }
        setHideWhenBackpress(false);
    }

    public boolean isLowDetailMode() {
        return lowDetailMode;
    }

    public void setLowDetailMode(boolean lowDetailMode) {
        this.lowDetailMode = lowDetailMode;
        invalidate();
    }

    public static RenderStatPopupView getInstance(MContext context) {
        if (instance == null) {
            instance = new RenderStatPopupView(context);
        }
        return instance;
    }

    @Override
    public void onFrameStart() {
        super.onFrameStart();
        text.setText(
                FrameRenderMonitor.getFPS() + "/60fps" +
                        (lowDetailMode ? "" : (
                                "\nFBO:" + FrameRenderMonitor.fboCreate + "\n"
                                        + "DrawCalls:" + FrameRenderMonitor.drawCalls + "\n"
                                        + "Total:" + (int) FrameRenderMonitor.frameRenderTime.avg + "ms\n"
                                        + "UI:" + (int) FrameRenderMonitor.drawUI.avg + "ms\n"
                                        + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000 / 1000 + "/" + Runtime.getRuntime().maxMemory() / 1000 / 1000 + "MB"
                        ))
        );

    }

    @Override
    protected void onDraw(BaseCanvas canvas) {
        super.onDraw(canvas);
        //double frameTime=getContext().getFrameDeltaTime();
    }
}
