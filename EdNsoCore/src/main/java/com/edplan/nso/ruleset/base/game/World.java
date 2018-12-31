package com.edplan.nso.ruleset.base.game;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.math.RectF;
import com.edplan.framework.media.bass.BassChannel;
import com.edplan.framework.timing.FrameClock;
import com.edplan.framework.timing.Schedule;
import com.edplan.framework.ui.inputs.EdKeyEvent;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.utils.interfaces.Consumer;
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
     * 判定世界
     */
    private JudgeWorld judgeWorld = new JudgeWorld();

    private JudgeThread judgeThread = new JudgeThread();

    private Schedule topLevelSchedule = new Schedule();

    /**
     * 绘制世界
     */
    private PaintWorld paintWorld = new PaintWorld(this);

    private MContext context;

    private State gameState = State.PAUSE;

    private FrameClock paintClock = new FrameClock();
    private FrameClock judgeClock = new FrameClock();

    private WorldConfig config;

    private Consumer<BaseCanvas> onDrawStart, onDrawEnd;

    private Consumer<RectF> onConfigDrawArea;

    private Consumer<EdMotionEvent> motionEventDec;

    private BassChannel channel;

    private Score score;

    public World(MContext context) {
        this.context = context;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Score getScore() {
        return score;
    }

    public void setSong(BassChannel channel) {
        this.channel = channel;
    }

    public void setOnDrawStart(Consumer<BaseCanvas> onDrawStart) {
        this.onDrawStart = onDrawStart;
    }

    public void setOnDrawEnd(Consumer<BaseCanvas> onDrawEnd) {
        this.onDrawEnd = onDrawEnd;
    }

    public void setOnConfigDrawArea(Consumer<RectF> onConfigDrawArea) {
        this.onConfigDrawArea = onConfigDrawArea;
    }

    public void setMotionEventDec(Consumer<EdMotionEvent> motionEventDec) {
        this.motionEventDec = motionEventDec;
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

    /**
     * 设置实际绘制区域
     * @param area
     */
    public void configDrawArea(RectF area) {
        if (onConfigDrawArea != null) {
            onConfigDrawArea.consume(area);
        }
    }

    public void onDraw(BaseCanvas canvas) {
        int stat = canvas.save();
        paintClock.update();
        if (onDrawStart != null) {
            onDrawStart.consume(canvas);
        }
        paintWorld.draw(canvas);
        if (onDrawEnd != null) {
            onDrawEnd.consume(canvas);
        }
        canvas.restoreToCount(stat);
    }

    public void pause() {
        paintClock.pause();
        judgeClock.pause();
        if (channel != null) {
            channel.pause();
        }
        gameState = State.PAUSE;
    }

    public void resume() {
        paintClock.run();
        judgeClock.run();
        if (channel != null) {
            channel.play();
        }
        gameState = State.PLAYING;
    }

    public void load() {
        judgeWorld.loadObjects();
    }

    public void start() {
        judgeThread.start();
        paintClock.start();
        judgeClock.start();
        if (channel != null) {
            channel.play();
        }
        gameState = State.PLAYING;
    }

    public void stop() {
        paintClock.pause();
        judgeClock.pause();
        if (channel != null) {
            channel.stop();
        }
        gameState = State.STOP;
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
            if (motionEventDec != null) {
                motionEventDec.consume(copy[i]);
            }
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
                if (channel != null) {
                    double t = channel.currentPlayTimeMS();
                    if (Math.abs(judgeClock.getFrameTime() - t) > 5) {
                        judgeClock.offset(t - judgeClock.getFrameTime());
                    }
                }

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
