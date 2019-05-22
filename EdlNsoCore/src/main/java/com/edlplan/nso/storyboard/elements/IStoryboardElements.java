package com.edlplan.nso.storyboard.elements;

import com.edlplan.nso.storyboard.PlayingStoryboard;
import com.edlplan.nso.storyboard.elements.drawable.ADrawableStoryboardElement;
import com.edlplan.nso.storyboard.elements.drawable.BaseDrawableSprite;

public interface IStoryboardElements {
    public boolean isDrawable();

    public String getPath();

    public String[] getTexturePaths();

    public BaseDrawableSprite createDrawable(PlayingStoryboard storyboard);

    public void onApply(ADrawableStoryboardElement sprite, PlayingStoryboard storyboard);
}
