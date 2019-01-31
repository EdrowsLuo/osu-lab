package com.edlplan.testgame;

import com.edplan.framework.MContext;
import com.edplan.framework.ui.ViewConfiguration;
import com.edplan.framework.ui.layout.Gravity;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.Fragment;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.RoundedButton;

public class SnakeTest implements ITest {

    @Override
    public String getName() {
        return "SnakeTest";
    }

    @Override
    public Fragment createFragment() {
        return new Fragment(){
            @Override
            protected void onCreate(MContext context) {
                setContentView(
                        new RelativeLayout(context) {{

                            layoutParam(
                                    new MarginLayoutParam() {{
                                        width = Param.MODE_MATCH_PARENT;
                                        height = Param.MODE_MATCH_PARENT;
                                    }}
                            );

                            SnakeView snakeView = new SnakeView(context);
                            RelativeParam pparam = new RelativeParam();
                            pparam.height = Param.makeupScaleOfParentParam(0.8f);
                            pparam.width = Param.makeupScaleOfParentOtherParam(0.8f);
                            pparam.gravity = Gravity.Center;

                            addView(snakeView, pparam);


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
                                    });
                                    RelativeParam param = new RelativeParam();
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
                                    });
                                    RelativeParam param = new RelativeParam();
                                    param.height = Param.makeUpDP(50);
                                    param.width = Param.makeUpDP(50);
                                    param.gravity = Gravity.CenterRight;
                                    pad.addView(button, param);
                                }
                            }
                        }}
                );
            }
        };
    }
}
