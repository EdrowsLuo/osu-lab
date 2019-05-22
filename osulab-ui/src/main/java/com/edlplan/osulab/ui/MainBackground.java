package com.edlplan.osulab.ui;

import com.edlplan.osulab.ui.popup.PopupToast;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.text.font.FontAwesome;
import com.edlplan.framework.ui.widget.RelativeContainer;
import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.drawable.ColorDrawable;
import com.edlplan.framework.graphics.opengl.objs.Color4;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;

import java.io.IOException;

import com.edlplan.framework.ui.drawable.sprite.TextureSprite;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.math.RectF;
import com.edlplan.framework.ui.widget.RelativeLayout;

public class MainBackground extends RelativeLayout {
    private AbstractTexture testTexture;

    private TextureSprite textureSprite;

    public MainBackground(MContext c) {
        super(c);
        ColorDrawable cd = new ColorDrawable(c);
        float t = 0.1f, b = 0.1f;
        cd.setColor(Color4.gray(t), Color4.gray(t),
                Color4.gray(b), Color4.gray(b));
        //setBackgroundColor(cd);

        textureSprite = new TextureSprite(c);

        try {
            testTexture = getContext().getAssetResource().subResource("osu/ui").loadTexture(
                    "menu-background-1.jpg"
                    //"logo.png"
            );
        } catch (IOException e) {
            e.printStackTrace();
            PopupToast.toast(getContext(), "err " + e.getMessage()).show();
        }
    }

    @Override
    public void onInitialLayouted() {
        super.onInitialLayouted();
    }

    @Override
    protected void onDraw(BaseCanvas canvas) {
        super.onDraw(canvas);
        if (testTexture != null) {
            canvas.getBlendSetting().save();
            canvas.getBlendSetting().setEnable(false);
            textureSprite.setAlpha(0.4f);
            textureSprite.setTexture(testTexture);
            textureSprite.setAreaFillTexture(RectF.xywh(0, 0, canvas.getWidth(), canvas.getHeight()));
            textureSprite.draw(canvas);
            canvas.getBlendSetting().restore();
        }
    }

}
