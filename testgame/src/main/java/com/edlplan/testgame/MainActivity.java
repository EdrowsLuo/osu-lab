package com.edlplan.testgame;

import android.annotation.SuppressLint;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.main.EdMainActivity;
import com.edplan.framework.ui.EdAbstractViewGroup;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.FrameLayout;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.RoundedButton;
import com.edplan.framework.ui.widget.ScrollLayout;
import com.edplan.framework.ui.widget.TextView;

import java.util.List;

public class MainActivity extends EdMainActivity {

    @Override
    protected void createGame() {
        initialWithView(TestView.class);
    }

    public static class TestView extends RelativeLayout {

        private FrameLayout frame;

        private EdAbstractViewGroup scroll;

        private TextView testName;

        public TestView(MContext context) {
            super(context);
            setBackgroundColor(Color4.gray(0.1f));

            children(
                    new RelativeLayout(context) {{

                        layoutParam(
                                new RelativeParam() {{
                                    width = Param.MODE_MATCH_PARENT;
                                    height = Param.MODE_MATCH_PARENT;
                                }}
                        );

                        children(
                                scroll = new LinearLayout(context) {{

                                    setBackgroundColor(Color4.rgba(0, 0, 0, 0.7f));

                                    layoutParam(
                                            new RelativeParam() {{
                                                width = Param.makeUpDP(100);
                                                height = Param.MODE_MATCH_PARENT;
                                            }}
                                    );

                                    setOrientation(Orientation.DIRECTION_T2B);

                                    gravity(Gravity.Center);

                                    childoffset = ViewConfiguration.dp(10);
                                }},
                                frame = new FrameLayout(context) {{
                                    layoutParam(
                                            new RelativeParam() {{
                                                marginLeft = ViewConfiguration.dp(100);
                                                width = Param.MODE_MATCH_PARENT;
                                                height = Param.MODE_MATCH_PARENT;
                                            }}
                                    );
                                }}
                        );

                    }},
                    testName = new TextView(context) {{
                        layoutParam(
                                new RelativeParam() {{
                                    width = Param.MODE_WRAP_CONTENT;
                                    height = Param.MODE_WRAP_CONTENT;
                                    gravity = Gravity.TopCenter;
                                }}
                        );
                        textSize = ViewConfiguration.dp(40);

                        setTextColor(Color4.Green);

                        setText("unknown");

                    }}
            );

            for (final ITest test : createTests()) {
                scroll.addView(
                        new RoundedButton(context) {{

                            setPadding(ViewConfiguration.dp(10));

                            layoutParam(
                                    new MarginLayoutParam() {{

                                        width = Param.MODE_WRAP_CONTENT;
                                        height = Param.MODE_WRAP_CONTENT;
                                    }}
                            );

                            children(
                                    new TextView(context) {{
                                        layoutParam(
                                                new RelativeParam() {{
                                                    width = Param.MODE_WRAP_CONTENT;
                                                    height = Param.MODE_WRAP_CONTENT;
                                                    gravity = Gravity.Center;
                                                }}
                                        );
                                        textSize = ViewConfiguration.dp(20);

                                        setText(test.getName());

                                        setOnClickListener(view -> {
                                            frame.setFragment(test.createFragment());
                                            testName.setText(test.getName());
                                        });
                                    }}
                            );
                        }}
                );
            }



        }

        public ITest[] createTests() {
            return new ITest[]{
                    new SnakeTest()
            };
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onInitialLayouted() {
            super.onInitialLayouted();
            RenderStatPopupView.getInstance(getContext()).show();
            //getContext().toast(String.format("%f %f", getWidth(), getHeight()));
            //post(() -> System.out.println(getContext().getViewRoot().loadViewTreeStruct()), 100);
        }

        @Override
        protected void onDraw(BaseCanvas canvas) {
            super.onDraw(canvas);
        }
    }
}
