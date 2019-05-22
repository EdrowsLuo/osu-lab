package com.edlplan.testgame;

import com.edplan.framework.Framework;
import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.graphics.opengl.objs.GLTexture;
import com.edplan.framework.math.Vec2;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.inputs.EdMotionEvent;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.ui.text.font.drawing.TextPrinter;
import com.edplan.framework.ui.widget.Fragment;
import com.edplan.framework.utils.interfaces.Consumer;

import java.util.ArrayList;
import java.util.List;

public class SlideTest implements ITest {

    @Override
    public Fragment createFragment() {
        return new Fragment() {
            @Override
            protected void onCreate(MContext context) {
                super.onCreate(context);
                setContentView(new SlideView(context));
            }
        };
    }

    public static class SlideView extends EdView {

        SlideDetector detector = new SlideDetector();

        private ArrayList<TextureQuad> quads = new ArrayList<>();
        private int count = 0;

        private Vec2 org = new Vec2((float) (Math.random() * 700) + 200);
        private float r = 80;
        private float ar = r * 3.5f;
        private double apt = 500;
        private double hitTime = -1;
        private double cutS = -1;
        private double cutE = -1;
        private Vec2 testHitPos;

        private boolean hitFrame = false;
        private Vec2 cutPos = null;

        public SlideView(MContext context) {
            super(context);
            detector.setSlideListener(d -> {
                SlideDetector.Tick t1 = d.startTick();
                SlideDetector.Tick t2 = d.endTick();
                if (t1 == null || cutS != -1) {
                    return;
                }
                if (Vec2.length(t1.pos, org) > r && Vec2.length(t2.pos, org) < r) {
                    if (Vec2.length(t1.pos, t2.pos) > r) {
                        cutS = t1.time;
                        cutE = t2.time;
                        hitFrame = true;
                        cutPos = t2.pos;
                    }
                }
            });
        }

        @Override
        protected void onDraw(BaseCanvas canvas) {
            super.onDraw(canvas);

            if (hitTime == -1) {
                hitTime = Framework.frameworkTime() + 3000;
            }

            canvas.drawCircle(org.x, org.y, r, hitFrame ? Color4.White : Color4.Yellow, 0.5f);
            if (hitTime > Framework.frameworkTime() && hitTime - Framework.frameworkTime() < apt * 3) {
                float p = 1 - ((float) ((hitTime - Framework.frameworkTime()) / apt) % 1);
                p = Math.max(p, 0);
                float a = p > 0.5f ? 1 : p * 2;
                float rr = ar * (1 - p) + r * p;
                canvas.drawRing(org.x, org.y, rr + 10, rr, Color4.White, a);
            }

            if (testHitPos == null) {
                if (detector.endTick() != null && detector.endTick().time > hitTime) {
                    testHitPos = detector.findPos(hitTime);
                }
            }

            List<SlideDetector.Tick> ticks = detector.ticks;

            while (count < ticks.size()) {
                if (count > 0) {
                    quads.add(canvas.createLineQuad(ticks.get(count - 1).pos, ticks.get(count).pos, 3, Color4.White, 0.3f));
                }
                TextureQuad quad = new TextureQuad();
                quad.setTextureAndSize(GLTexture.White);
                quad.size.set(10);
                quad.accentColor = Color4.Red;
                quad.alpha.value = 0.5f;
                quad.syncPosition(ticks.get(count).pos);
                quads.add(quad);
                count++;
            }

            TextureQuadBatch batch = TextureQuadBatch.getDefaultBatch();
            for (TextureQuad quad : quads) {
                batch.add(quad);
            }

            if (testHitPos != null) {
                canvas.drawCircle(testHitPos.x, testHitPos.y, 7, Color4.Yellow, 1);
            }

            if (cutPos!=null) {
                canvas.drawCircle(cutPos.x, cutPos.y, 5, Color4.Green, 1);
            }

            TextPrinter textPrinter = new TextPrinter(BMFont.getDefaultFont(), 0, 100, 1, Color4.White);
            textPrinter.printString(ticks.size() + "/" + (ticks.size() * 2 - 1));
            textPrinter.toNextLine();
            textPrinter.printString((int) detector.duration() + "");
            textPrinter.toNextLine();
            textPrinter.printString((int) hitTime + "");
            textPrinter.toNextLine();
            textPrinter.printString((int) (hitTime - cutS) + "");
            textPrinter.toNextLine();
            textPrinter.printString((int) (hitTime - cutE) + "");
            textPrinter.draw(canvas);
        }

        @Override
        public boolean onMotionEvent(EdMotionEvent e) {
            hitFrame = false;
            switch (e.getEventType()) {
                case Down:
                    if (!detector.isDetecting) {
                        quads.clear();
                        count = 0;
                        detector.start(e.pointerId);
                        detector.addTick(e.eventPosition, e.time);
                    }
                    break;
                case Move:
                    if (detector.isDetecting && detector.detectingId == e.pointerId) {
                        detector.addTick(e.eventPosition, e.time);
                    }
                    break;
                case Up:
                    if (detector.isDetecting && detector.detectingId == e.pointerId) {
                        detector.end();
                    }
                    break;
            }
            return true;
        }

        public static class SlideDetector {

            private int detectingId = -1;

            private boolean isDetecting = false;

            private ArrayList<Tick> ticks = new ArrayList<>();

            private Consumer<SlideDetector> slideListener;

            public void setSlideListener(Consumer<SlideDetector> slideListener) {
                this.slideListener = slideListener;
            }

            public void start(int detectingId) {
                this.detectingId = detectingId;
                isDetecting = true;
                ticks.clear();
            }

            public void addTick(Vec2 pos, double time) {
                ticks.add(new Tick(pos, time));
                if (ticks.size() > 1) {
                    if (slideListener != null) {
                        slideListener.consume(this);
                    }
                }
            }

            public double duration() {
                return ticks.size() > 0 ? (endTick().time - startTick().time) : 0;
            }

            public Tick startTick() {
                return ticks.size() > 0 ? ticks.get(0) : null;
            }

            public Tick endTick() {
                return ticks.size() > 0 ? ticks.get(ticks.size() - 1) : null;
            }

            public Vec2 findPos(double time) {
                if (ticks.size() == 0) {
                    return new Vec2();
                } else {
                    if (time < startTick().time) {
                        return startTick().pos.copy();
                    } else {
                        if (time >= endTick().time) {
                            return endTick().pos.copy();
                        }
                        int find = -1;
                        for (int i = 1; i < ticks.size(); i++) {
                            if (ticks.get(i - 1).time <= time && ticks.get(i).time > time) {
                                find = i - 1;
                            }
                        }
                        if (find == -1) {
                            return new Vec2();
                        } else {
                            return Vec2.onLine(
                                    ticks.get(find).pos,
                                    ticks.get(find + 1).pos,
                                    (float) ((time - ticks.get(find).time) / (ticks.get(find + 1).time - ticks.get(find).time)));
                        }
                    }
                }
            }

            public ArrayList<Tick> getTicks() {
                return ticks;
            }

            public void end() {
                isDetecting = false;
            }

            public static class Tick {
                Vec2 pos;
                double time;

                public Tick(Vec2 pos, double time) {
                    this.pos = pos.copy();
                    this.time = time;
                }
            }
        }
    }
}
