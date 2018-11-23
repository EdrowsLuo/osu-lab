package com.edlplan.testgame;

import android.app.Activity;
import android.os.Bundle;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.GLPaint;
import com.edplan.framework.graphics.opengl.GLWrapped;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.main.EdMainActivity;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.EdAbstractViewGroup;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.LinearContainer;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.TextView;

public class MainActivity extends EdMainActivity {

    @Override
    protected void createGame() {
        initialWithView(TestView.class);
    }

    public static class TestView extends LinearLayout {

        public TestView(MContext context) {
            super(context);
            setBackground(Color4.gray(0.1f));
            setOrientation(Orientation.DIRECTION_T2B);
            setGravity(Gravity.Center);
            {
                TextView textView = new TextView(context);
                textView.setBackground(Color4.rgba(0, 0, 0, 0.5f));
                textView.setText("(qwq)");
                textView.setTextSize(200);
                MarginLayoutParam marginLayoutParam = new MarginLayoutParam();
                marginLayoutParam.width = Param.MODE_MATCH_PARENT;
                marginLayoutParam.height = Param.MODE_WRAP_CONTENT;
                addView(textView, marginLayoutParam);
            }
            {
                TextView textView = new TextView(context);
                textView.setBackground(Color4.rgba(0, 0, 0, 0.5f));
                textView.setText("(qwq)");
                textView.setTextSize(200);
                MarginLayoutParam marginLayoutParam = new MarginLayoutParam();
                marginLayoutParam.width = Param.MODE_MATCH_PARENT;
                marginLayoutParam.height = Param.MODE_WRAP_CONTENT;
                addView(textView, marginLayoutParam);
            }
            {
                LinearLayout container = new LinearLayout(context);
                container.setOffsetY(200);
                container.setOrientation(Orientation.DIRECTION_T2B);
                container.setGravity(Gravity.TopLeft);
                MarginLayoutParam marginLayoutParam = new MarginLayoutParam();
                marginLayoutParam.width = Param.MODE_MATCH_PARENT;
                marginLayoutParam.height = Param.MODE_WRAP_CONTENT;
                addView(container, marginLayoutParam);
                {
                    {
                        TextView textView = new TextView(context);
                        textView.setBackground(Color4.rgba(0, 0, 0, 0.5f));
                        textView.setText("(qwq)");
                        textView.setTextSize(200);
                        MarginLayoutParam marginLayoutParam2 = new MarginLayoutParam();
                        marginLayoutParam2.width = Param.MODE_MATCH_PARENT;
                        marginLayoutParam2.height = Param.MODE_WRAP_CONTENT;
                        container.addView(textView, marginLayoutParam2);
                    }
                }
                {
                    LinearContainer container2 = new LinearContainer(context);
                    container2.setOffsetY(100);
                    container2.setOrientation(Orientation.DIRECTION_T2B);
                    container2.setGravity(Gravity.TopLeft);
                    MarginLayoutParam marginLayoutParam2 = new MarginLayoutParam();
                    marginLayoutParam2.width = Param.MODE_MATCH_PARENT;
                    marginLayoutParam2.height = Param.makeUpDP(100);
                    container.addView(container2, marginLayoutParam2);
                    {
                        {
                            TextView textView = new TextView(context);
                            textView.setBackground(Color4.rgba(0, 0, 0, 0.5f));
                            textView.setText("(qwq)");
                            textView.setTextSize(200);
                            MarginLayoutParam marginLayoutParam3 = new MarginLayoutParam();
                            marginLayoutParam3.width = Param.MODE_MATCH_PARENT;
                            marginLayoutParam3.height = Param.MODE_WRAP_CONTENT;
                            container2.addView(textView, marginLayoutParam);
                        }
                    }
                }
            }
        }

        @Override
        public void onInitialLayouted() {
            super.onInitialLayouted();
            RenderStatPopupView.getInstance(getContext()).show();
        }

        @Override
        protected void onDraw(BaseCanvas canvas) {
            super.onDraw(canvas);
            /*GLPaint paint = new GLPaint();
            for (int i = 0; i < 10; i++) {
                canvas.drawTexture(GLTexture.ErrorTexture, RectF.ltrb(0, 0, getWidth(), getHeight()), paint);
            }*/
        }
    }
}
