package com.edplan.nso.storyboard;

import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.fast.FastRenderer;
import com.edplan.framework.test.performance.Tracker;
import com.edplan.framework.ui.drawable.EdDrawable;
import com.edplan.nso.storyboard.elements.IStoryboardElements;
import com.edplan.nso.storyboard.elements.drawable.ADrawableStoryboardElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PlayingStoryboardLayer_old extends EdDrawable {
    public static Tracker.TrackNode PrepareTime;
    public static Tracker.TrackNode RenderOsb;

    static {
        PrepareTime = Tracker.register("OsbPrepareTime");
        RenderOsb = Tracker.register("OsbRenderOsb");
    }

    private List<ElementNode> sprites = new ArrayList<ElementNode>();

    private List<ElementNode> applyNode = new LinkedList<ElementNode>();

    private List<ElementNode> spritesNotAdded = new LinkedList<ElementNode>();

    private List<ElementNode> spriteInField = new ArrayList<ElementNode>();

    private PlayingStoryboard storyboard;

    private boolean preApplyMode = false;

    private int newApply = 0;

    private ApplyThread applyThread;

    //private OsbRenderer renderer;

    private FastRenderer renderer;

    public PlayingStoryboardLayer_old(StoryboardLayer layer, PlayingStoryboard storyboard) {
        super(storyboard.getContext());
        this.storyboard = storyboard;
        int depth = -1;
        //int maxDepth=30000;
        for (IStoryboardElements ele : layer.getElements()) {
            if (ele.isDrawable()) {
                depth++;
                final ADrawableStoryboardElement drawable = ele.createDrawable(storyboard);
                final ElementNode node = new ElementNode(
                        sprites.size(),
                        drawable.getStartTime(),
                        drawable.getEndTime(),
                        drawable,
                        ele
                );
                sprites.add(node);
                if (preApplyMode) ele.onApply(drawable, storyboard);
				/*
				 System.out.println(((BaseDrawableSprite)drawable).getAnimations());
				 System.out.println(((StoryboardSprite)ele).rawData);
				 */
            } else {
                ele.onApply(null, storyboard);
            }
        }
        Collections.sort(sprites, new Comparator<ElementNode>() {
            @Override
            public int compare(PlayingStoryboardLayer_old.ElementNode p1, PlayingStoryboardLayer_old.ElementNode p2) {

                return (int) Math.signum(p1.startTime - p2.startTime);
            }
        });
        spritesNotAdded.addAll(sprites);
        applyNode.addAll(sprites);
        sprites.clear();

        //renderer=new OsbRenderer(storyboard.getTimeline());
        renderer = new FastRenderer();

        applyThread = new ApplyThread();
        applyThread.start();
        //System.out.println(sprites.size());
    }

    public int getNewApply() {
        return newApply;
    }

    public int objectInField() {
        return spriteInField.size();
    }

    private static Comparator<ElementNode> nodeSorter = new Comparator<ElementNode>() {
        @Override
        public int compare(PlayingStoryboardLayer_old.ElementNode p1, PlayingStoryboardLayer_old.ElementNode p2) {

            return p1.depth - p2.depth;
        }
    };

    protected void refreshObjects() {
        double time = storyboard.getTimeline().frameTime();
        applyThread.refreshCurrentTime(time);
        Iterator<ElementNode> iter = spritesNotAdded.iterator();
        ElementNode ele;
        while (iter.hasNext()) {
            ele = iter.next();
            if (ele.startTime <= time) {
                spriteInField.add(ele);
                if (!ele.hasAdded()) ele.apply();
                iter.remove();
            } else {
                break;
            }
        }
        iter = spriteInField.iterator();
        while (iter.hasNext()) {
            ele = iter.next();
            if (ele.endTime < time) {
                ele.onRemove();
                iter.remove();
            }
        }
        Collections.sort(spriteInField, nodeSorter);
    }

    int asyncThreadCount = 16;

    protected void doAsyncPrepare() {
        PrepareThread[] ts = new PrepareThread[asyncThreadCount];
        Iterator<ElementNode> nodes = spriteInField.iterator();
        int maxSize = spriteInField.size() / asyncThreadCount + 2;
        for (int i = 0; i < asyncThreadCount; i++) {
            int count = 0;
            ts[i] = new PrepareThread();
            while (count < maxSize && nodes.hasNext()) {
                ts[i].add(nodes.next());
            }
            ts[i].start();
        }
        for (PrepareThread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean asyncPrepare = false;

    @Override
    public void draw(BaseCanvas canvas) {

        newApply = 0;

        PrepareTime.watch();
        refreshObjects();
        PrepareTime.end();

        renderer.ensureSize(spriteInField.size() * 6, spriteInField.size() * 4 * 2);

        if (asyncPrepare) doAsyncPrepare();
        int c = canvas.getBlendSetting().save();
        //canvas.enablePost();
        PrepareTime.watch();
        for (ElementNode ele : spriteInField) {
            if (!asyncPrepare) ele.element.prepareForDraw();
        }
        PrepareTime.end();

        RenderOsb.watch();
        renderer.start(canvas);
        for (ElementNode ele : spriteInField) {
            //ele.element.drawOsbRenderer(renderer);
            ele.element.drawFastRenderer(renderer);
            //ele.element.draw(canvas);
        }
        renderer.end();
        RenderOsb.end();


        //canvas.disablePost();
        canvas.getBlendSetting().restoreToCount(c);
    }

    public class ElementNode {
        public final int depth;
        public final double startTime;
        public final double endTime;
        public final ADrawableStoryboardElement element;
        public final IStoryboardElements rawElement;

        boolean added = false;

        public ElementNode(int d, double st, double et, ADrawableStoryboardElement ele, IStoryboardElements rele) {
            depth = d;
            startTime = st;
            endTime = et;
            element = ele;
            rawElement = rele;
        }

        public boolean hasAdded() {
            return added;
        }

        public synchronized void apply() {
            if (added) return;
            newApply++;
            added = true;
            if (!preApplyMode) {
                //PrepareTime.watch();
                rawElement.onApply(element, storyboard);
                //PrepareTime.end();
            }
            element.onAdd();
        }

        public void onRemove() {
            element.onRemove();
        }

    }

    public class PrepareThread extends Thread {

        private List<ElementNode> nodes;

        public PrepareThread() {
            nodes = new LinkedList<ElementNode>();
        }

        public void add(ElementNode n) {
            nodes.add(n);
        }

        @Override
        public void run() {

            super.run();
            Iterator<ElementNode> iter = nodes.iterator();
            ElementNode tmp = null;
            while (iter.hasNext()) {
                tmp = iter.next();
                tmp.element.prepareForDraw();
                iter.remove();
            }
            tmp = null;
            nodes.clear();
        }

    }


    public class ApplyThread extends Thread {

        private double currentTime;

        private double preApplyTime = 2000;

        @Override
        public void run() {

            super.run();
            Iterator<ElementNode> iter = applyNode.iterator();
            ElementNode node;
            while (iter.hasNext()) {
                node = iter.next();
                while (node.startTime > currentTime + preApplyTime) {
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                    }
                }
                if (!node.hasAdded()) node.apply();
                iter.remove();
            }
        }

        public void refreshCurrentTime(double time) {
            currentTime = time;
        }

    }

}
