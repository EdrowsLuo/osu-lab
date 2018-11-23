package com.edlplan.testgame;

import android.annotation.SuppressLint;
import android.os.SystemClock;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.main.EdMainActivity;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.RoundedButton;

public class MainActivity extends EdMainActivity {

    @Override
    protected void createGame() {
        initialWithView(TestView.class);
    }

    public static class TestView extends RelativeLayout {

        SnakeView snakeView;

        public TestView(MContext context) {
            super(context);
            setBackground(Color4.gray(0.1f));
            {
                snakeView = new SnakeView(context);
                RelativeParam param = new RelativeParam();
                param.height = Param.makeupScaleOfParentParam(0.8f);
                param.width = Param.makeupScaleOfParentOtherParam(0.8f);
                param.gravity = Gravity.Center;

                addView(snakeView, param);
            }

            {
                RelativeLayout pad = new RelativeLayout(context);
                pad.setPadding(ViewConfiguration.dp(20));
                RelativeParam paramp = new RelativeParam();
                paramp.height = Param.makeUpDP(190);
                paramp.width = Param.makeUpDP(190);
                paramp.gravity = Gravity.BottomLeft;
                addView(pad, paramp);
                {
                    RoundedButton button = new RoundedButton(context);
                    button.setOnClickWhenDown(true);
                    button.setOnClickListener(v -> {
                        if (snakeView.getSnake().getDirection() != SnakeView.Snake.Direction.Down) {
                            snakeView.getSnake().setDirection(SnakeView.Snake.Direction.Up);
                        }
                    });
                    RelativeParam param = new RelativeParam();
                    param.height = Param.makeUpDP(50);
                    param.width = Param.makeUpDP(50);
                    param.gravity = Gravity.TopCenter;
                    pad.addView(button, param);
                }
                {
                    RoundedButton button = new RoundedButton(context);
                    button.setOnClickWhenDown(true);
                    button.setOnClickListener(v -> {
                        if (snakeView.getSnake().getDirection() != SnakeView.Snake.Direction.Up) {
                            snakeView.getSnake().setDirection(SnakeView.Snake.Direction.Down);
                        }
                    });
                    RelativeParam param = new RelativeParam();
                    param.height = Param.makeUpDP(50);
                    param.width = Param.makeUpDP(50);
                    param.gravity = Gravity.BottomCenter;
                    pad.addView(button, param);
                }
                {
                    RoundedButton button = new RoundedButton(context);
                    button.setOnClickWhenDown(true);
                    button.setOnClickListener(v -> {
                        if (snakeView.getSnake().getDirection() != SnakeView.Snake.Direction.Right) {
                            snakeView.getSnake().setDirection(SnakeView.Snake.Direction.Left);
                        }
                    });RelativeParam param = new RelativeParam();
                    param.height = Param.makeUpDP(50);
                    param.width = Param.makeUpDP(50);
                    param.gravity = Gravity.CenterLeft;
                    pad.addView(button, param);
                }
                {
                    RoundedButton button = new RoundedButton(context);
                    button.setOnClickWhenDown(true);
                    button.setOnClickListener(v -> {
                        if (snakeView.getSnake().getDirection() != SnakeView.Snake.Direction.Left) {
                            snakeView.getSnake().setDirection(SnakeView.Snake.Direction.Right);
                        }
                    });RelativeParam param = new RelativeParam();
                    param.height = Param.makeUpDP(50);
                    param.width = Param.makeUpDP(50);
                    param.gravity = Gravity.CenterRight;
                    pad.addView(button, param);
                }
            }
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onInitialLayouted() {
            super.onInitialLayouted();
            RenderStatPopupView.getInstance(getContext()).show();
            getContext().toast(String.format("%f %f", getWidth(), getHeight()));
            post(() -> System.out.println(getContext().getViewRoot().loadViewTreeStruct()), 100);
        }

        @Override
        protected void onDraw(BaseCanvas canvas) {
            super.onDraw(canvas);
        }
    }
}
