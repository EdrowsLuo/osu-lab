package com.edplan.framework.main;

import android.app.Activity;
import android.os.Bundle;

import com.edplan.framework.graphics.opengl.MainRenderer;
import com.edplan.framework.ui.EdView;

public abstract class EdMainActivity extends Activity {
    private MainApplication app;

    /**
     * 在这个方法里面创建游戏，有下面几个方法来初始化
     * <p>
     * ①自定义一个类继承MainApplication然后注册的方法：
     *
     * @Override protected void createGame(){
     * YourApplication app=new YourApplication(args);
     * app.setUpActivity(this);
     * register(app);
     * }
     * -----------------------------------------------------
     * ②自定义一个类继承MainRenderer，
     * 并确保一个(MContext c,MainApplication app)的构造方法：
     * @Override protected void createGame(){
     * initialWithRenderer(YourRenderer.class);
     * }
     * -----------------------------------------------------
     * ③直接使用自定义的View来初始化，根据约定，
     * 所有EdView的子类应该有一个单参数(MContext c)的构造方法：
     * @Override protected void createGame(){
     * initialWithView(YourView.class);
     * }
     * -----------------------------------------------------
     */
    protected abstract void createGame();

    @Override
    protected final void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        createGame();
    }

    public void register(MainApplication app) {
        this.app = app;
    }

    protected void initialWithRenderer(Class<? extends MainRenderer> renderer) {
        app = new DefaultApplication(renderer, null);
        app.setUpActivity(this);
    }

    protected void initialWithView(Class<? extends EdView> view) {
        app = new DefaultApplication(null, view);
        app.setUpActivity(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
        app.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
        app.onResume();
    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();
        app.onLowMemory();
    }

    @Override
    public void onBackPressed() {

        app.getMContext().getUiLooper().post(new Runnable() {
            @Override
            public void run() {

                app.onBackPressed();
            }
        });
    }
}
