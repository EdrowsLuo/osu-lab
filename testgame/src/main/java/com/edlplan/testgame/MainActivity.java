package com.edlplan.testgame;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.edlplan.framework.MContext;
import com.edlplan.framework.database.v2.SimpleDBHelper;
import com.edlplan.framework.database.v2.SimpleSQLException;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.main.EdMainActivity;
import com.edlplan.framework.ui.EdAbstractViewGroup;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edlplan.framework.ui.layout.Gravity;
import com.edlplan.framework.ui.layout.MarginLayoutParam;
import com.edlplan.framework.ui.layout.Orientation;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.widget.FrameLayout;
import com.edlplan.framework.ui.widget.LinearLayout;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.RoundedButton;
import com.edlplan.framework.ui.widget.ScrollLayout;
import com.edlplan.framework.ui.widget.TextView;
import com.edlplan.framework.utils.json.JsonStream;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends EdMainActivity {

    @Override
    protected void createGame() {
        initialWithView(TestView.class);
        SimpleDBHelper dbHelper = new SimpleDBHelper(
                new File(Environment.getExternalStorageDirectory(), "osu!lab/test.db").getAbsolutePath(), 2) {

        };
        try {
            dbHelper.open();
            System.out.println("db version : " + dbHelper.queryDatabaseVersion());
        } catch (SimpleSQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonStream stream = new JsonStream("{test: 1.23123,\n\"hh\" : [ ]}");
        try {
            System.out.println(stream.dumpJSONObject().toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                    new SnakeTest(),
                    new CanvasDrawTest(),
                    new JavaScriptTest(),
                    new SlideTest()
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
