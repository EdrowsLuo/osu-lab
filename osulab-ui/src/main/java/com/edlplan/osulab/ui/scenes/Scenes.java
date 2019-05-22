package com.edlplan.osulab.ui.scenes;

import com.edlplan.osulab.LabGame;
import com.edlplan.osulab.ScenesName;
import com.edlplan.osulab.ui.BackQuery;
import com.edlplan.osulab.ui.scenes.game.GameScene;
import com.edlplan.osulab.ui.scenes.songs.SongsScene;
import com.edlplan.framework.MContext;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.ViewConfiguration;
import com.edlplan.framework.ui.animation.ComplexAnimation;
import com.edlplan.framework.ui.animation.ComplexAnimationBuilder;
import com.edlplan.framework.ui.animation.Easing;
import com.edlplan.framework.ui.animation.FloatQueryAnimation;
import com.edlplan.framework.ui.layout.EdMeasureSpec;
import com.edlplan.framework.ui.layout.MeasureCore;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.component.Hideable;
import com.edlplan.framework.utils.interfaces.Consumer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Scenes extends RelativeLayout implements Hideable, BackQuery.BackHandler {

    public static final double SCENE_TRANSITION_DURATION = 300;

    private HashMap<String, SceneNode> scenes = new HashMap<String, SceneNode>();

    private static ArrayList<Consumer<Scenes>> registerNodes = new ArrayList<>();

    private ArrayList<BaseScene> scenesStack = new ArrayList<BaseScene>();

    private BaseScene currentScene;

    public Scenes(MContext c) {
        super(c);
        initialRegister();
    }

    public void initialRegister() {
        register(WorkingScene.class, ScenesName.Edit, true);
        register(SongsScene.class, ScenesName.SongSelect, true);
        register(GameScene.class, ScenesName.GameScene, false);
    }

    public void register(final Class<? extends BaseScene> klass, final String name, final boolean singleInstance) {
        if (scenes.containsKey(name)) {
            throw new IllegalArgumentException("scene " + name + " register twice");
        }
        SceneNode n = createNode(klass, name, singleInstance);
        scenes.put(n.name, n);
    }

    @Override
    public void onInitialLayouted() {
        super.onInitialLayouted();
        setVisiblility(VISIBILITY_GONE);
        setAlpha(0);
    }

    @Override
    public int getChildrenCount() {
        return currentScene == null ? 0 : 1;
    }

    @Override
    public EdView getChildAt(int i) {
        return i == 0 ? currentScene : null;
    }

    public BaseScene getCurrentScene() {
        return currentScene;
    }

    private void swapScene(SceneNode n) {
        if (isHidden()) show();
        final BaseScene s;
        if (n.singleInstance) {
            scenesStack.remove(n.getInstance());
            s = n.getInstance();
        } else {
            s = n.createInstance();
        }

        if (currentScene == null) {
            scenesStack.add(s);
            currentScene = s;
            s.show();
        } else {
            currentScene.hide();
            post(() -> {
                //scenesStack.remove(currentScene);
                scenesStack.add(s);
                currentScene = s;
                s.show();
            }, currentScene.getHideDuration() + 10);
        }
    }

    public void swapScene(String name) {
        final SceneNode n = scenes.get(name);
        if (n == null) {
            getContext().toast("无效的场景" + name);
            return;
        }
        swapScene(n);
    }

    @Override
    public void hide() {
        BackQuery.get().unregist(this);
        ComplexAnimationBuilder b = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 0, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        ComplexAnimation anim = b.buildAndStart();
        anim.setOnFinishListener(setVisibilityWhenFinish(VISIBILITY_GONE));
        setAnimation(anim);
        LabGame.get().getSceneSelectButtonBar().show();
    }

    @Override
    public void show() {
        BackQuery.get().regist(this);
        setVisiblility(VISIBILITY_SHOW);
        ComplexAnimationBuilder b = ComplexAnimationBuilder.start(FloatQueryAnimation.fadeTo(this, 1, ViewConfiguration.DEFAULT_TRANSITION_TIME, Easing.None));
        ComplexAnimation anim = b.buildAndStart();
        setAnimation(anim);
    }

    @Override
    public boolean isHidden() {
        return getVisiblility() == VISIBILITY_GONE;
    }

    @Override
    public boolean onBack() {
        if (scenesStack.size() > 0) {
            currentScene.hide();
            post(() -> {
                scenesStack.remove(currentScene);
                currentScene = scenesStack.size() > 0 ? scenesStack.get(scenesStack.size() - 1) : null;
                if (currentScene == null) {
                    hide();
                } else {
                    currentScene.show();
                }
            }, currentScene.getHideDuration() + 10);
            return true;
        }
        return false;
    }

    private SceneNode createNode(Class<? extends BaseScene> klass, String name, boolean singleInstance) {
        return new SceneNode(klass, name, singleInstance);
    }

    public class SceneNode {
        Class<? extends BaseScene> klass;

        boolean singleInstance;

        public String name;

        private BaseScene savedScene;

        SceneNode(Class<? extends BaseScene> klass, String name, boolean singleInstance) {
            this.klass = klass;
            this.name = name;
            this.singleInstance = singleInstance;
        }


        BaseScene createInstance() {
            try {
                BaseScene s = klass.getConstructor(new Class[]{MContext.class}).newInstance(new Object[]{getContext()});
                if (s.getSceneName() == null) {
                    Method m = klass.getMethod("setSceneName", String.class);
                    m.invoke(s, name);
                }
                long wm =
                        EdMeasureSpec.makeupMeasureSpec(
                                getWidth(),
                                EdMeasureSpec.MODE_AT_MOST);
                long hm =
                        EdMeasureSpec.makeupMeasureSpec(
                                getHeight(),
                                EdMeasureSpec.MODE_AT_MOST);
                MeasureCore.measureChild(s, 0, 0, wm, hm);
                s.layout(0, 0, getWidth(), getHeight());
                s.setParent(Scenes.this);
                return s;
            } catch (Exception e) {
                e.printStackTrace();
                getContext().toast("err create instance of " + name);
                return null;
            }
        }

        BaseScene getInstance() {
            if (savedScene == null) {
                savedScene = createInstance();
            }
            return savedScene;
        }

    }
}
