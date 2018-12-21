package com.edplan.nso.ruleset.base.game;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.math.RectF;
import com.edplan.framework.timing.FrameClock;
import com.edplan.framework.ui.inputs.EdKeyEvent;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.nso.ruleset.base.game.judge.CursorData;
import com.edplan.nso.ruleset.base.game.judge.JudgeWorld;
import com.edplan.nso.ruleset.base.game.judge.RawInputHandler;
import com.edplan.nso.ruleset.base.game.paint.PaintWorld;

import java.util.ArrayList;

/**
 * onLoad对世界进行基础的设置
 * start，pause，resume处理世界的开始，暂停和继续
 * 在onDraw里面处理绘制事件
 */
public class World implements RawInputHandler {

    /**
     * 世界窗口位置
     */
    private RectF worldRect;

    /**
     * 判定世界
     */
    private JudgeWorld judgeWorld = new JudgeWorld();

    private JudgeThread judgeThread = new JudgeThread();

    /**
     * 绘制世界
     */
    private PaintWorld paintWorld = new PaintWorld(this);

    private MContext context;

    private State gameState = State.PAUSE;

    private FrameClock paintClock = new FrameClock();
    private FrameClock judgeClock = new FrameClock();

    private WorldConfig config;

    public World(MContext context) {
        this.context = context;
    }

    public JudgeWorld getJudgeWorld() {
        return judgeWorld;
    }

    public PaintWorld getPaintWorld() {
        return paintWorld;
    }

    public MContext getContext() {
        return context;
    }

    public FrameClock getJudgeClock() {
        return judgeClock;
    }

    public FrameClock getPaintClock() {
        return paintClock;
    }

    public double getPaintFrameTime() {
        return paintClock.getFrameTime();
    }

    public double getJudgeFrameTime() {
        return judgeClock.getFrameTime();
    }

    public void onDraw(BaseCanvas canvas) {
        paintClock.update();
        paintWorld.draw(canvas);
    }

    public void pause() {
        paintClock.pause();
        judgeClock.pause();
        gameState = State.PAUSE;
    }

    public void resume() {
        paintClock.run();
        judgeClock.run();
        gameState = State.PLAYING;
    }

    public void load() {
        judgeWorld.loadObjects();
    }

    public void start() {
        judgeThread.start();
        paintClock.start();
        judgeClock.start();
        gameState = State.PLAYING;
    }

    public void onLoadConfig(WorldConfig config) {
        this.config = config;
        judgeWorld.preloadJudgeDataType(config.judgeTypes.toArray(new Class[config.judgeTypes.size()]));
    }

    @Override
    public boolean onMotionEvent(EdMotionEvent... event) {
        if (!judgeClock.isRunninng()) {
            return false;
        }
        EdMotionEvent[] copy = new EdMotionEvent[event.length];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = event[i].copy();
            copy[i].time = judgeClock.toClockTime(copy[i].time);
        }
        return judgeWorld.onMotionEvent(copy);
    }

    @Override
    public boolean onKeyEvent(EdKeyEvent event) {
        if (!judgeClock.isRunninng()) {
            return false;
        }
        return judgeWorld.onKeyEvent(event);
    }

    public enum State {
        PAUSE,
        PLAYING,
        STOP,
    }

    private class JudgeThread extends Thread {
        @Override
        public void run() {
            super.run();
            l:
            while (true) {
                switch (gameState) {
                    case PAUSE:
                        try {
                            sleep(config.judgeThreadInterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case STOP:
                        break l;
                    case PLAYING:
                    default:
                        runJudge();
                        try {
                            sleep(config.judgeThreadInterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }

        private void runJudge() {
            if (judgeClock.isRunninng()) {
                judgeClock.update();
                judgeWorld.update(World.this);
            }
        }
    }


    public static class WorldConfig {
        //判定线程的休眠时间
        public int judgeThreadInterval = 1;

        //申请的判定数据类型
        public ArrayList<Class> judgeTypes = new ArrayList<>();
    }
}
