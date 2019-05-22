package com.edlplan.nso.storyboard.elements.drawable;

import com.edlplan.nso.storyboard.PlayingStoryboard;
import com.edlplan.framework.graphics.opengl.fast.FastRenderer;
import com.edlplan.framework.timing.PreciseTimeline;
import com.edlplan.framework.ui.drawable.EdDrawable;

public abstract class ADrawableStoryboardElement extends EdDrawable {
    private PlayingStoryboard storyboard;

    public ADrawableStoryboardElement(PlayingStoryboard storyboard) {
        super(storyboard.getContext());
        this.storyboard = storyboard;
    }

    public PreciseTimeline getTimeline() {
        return storyboard.getTimeline();
    }

    public void setStoryboard(PlayingStoryboard storyboard) {
        this.storyboard = storyboard;
    }

    public PlayingStoryboard getStoryboard() {
        return storyboard;
    }

    public abstract void drawFastRenderer(FastRenderer renderer);

    public abstract void prepareForDraw();

    public abstract void onAdd();

    public abstract void onRemove();

    public abstract double getStartTime();

    public abstract double getEndTime();

    public abstract boolean hasAdd();
}
