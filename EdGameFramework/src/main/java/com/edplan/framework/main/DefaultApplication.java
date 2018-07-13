package com.edplan.framework.main;

import com.edplan.framework.graphics.opengl.MainRenderer;
import com.edplan.framework.MContext;
import com.edplan.framework.ui.EdView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DefaultApplication extends MainApplication {
    private Class<? extends MainRenderer> renderer;

    private Class<? extends EdView> contentView;

    public DefaultApplication(Class<? extends MainRenderer> renderer, Class<? extends EdView> contentView) {
        this.renderer = renderer;
        this.contentView = contentView;
    }

    @Override
    public MainRenderer createRenderer(MContext context) {

        if (renderer == null) {
            return new DefaultRenderer(context, this);
        } else {
            try {
                return renderer.getConstructor(new Class[]{MContext.class, MainApplication.class}).newInstance(new Object[]{context, this});
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class DefaultRenderer extends MainRenderer {

        public DefaultRenderer(MContext context, MainApplication app) {
            super(context, app);
        }

        @Override
        public EdView createContentView(MContext c) {

            try {
                Constructor cst = contentView.getConstructor(new Class[]{MContext.class});
                return (EdView) cst.newInstance(new Object[]{c});
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
