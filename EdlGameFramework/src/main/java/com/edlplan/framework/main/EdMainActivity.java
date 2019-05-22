package com.edlplan.framework.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.edlplan.framework.graphics.opengl.MainRenderer;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.UIClassPreparer;

public abstract class EdMainActivity extends Activity {

    static {
        UIClassPreparer.prepare();
    }

    private MainApplication app;

    /**
     * 在这个方法里面创建游戏，有下面几个方法来初始化。
     * <p>自定义一个类继承MainApplication然后注册的方法：
     * <pre>{@code
     * protected void createGame(){
     *          YourApplication app=new YourApplication(args);
     *          app.setUpActivity(this);
     *          register(app);
     * }
     * }</pre>
     *
     * <p>自定义一个类继承MainRenderer，并确保一个(MContext c,MainApplication app)的构造方法：
     * <pre>{@code
     * protected void createGame(){
     *      initialWithRenderer(YourRenderer.class);
     * }
     * }</pre>
     * <p>直接使用自定义的View来初始化，根据约定，所有EdView的子类应该有一个单参数(MContext c)的构造方法：
     * <pre>{@code
     * protected void createGame(){
     *      initialWithView(YourView.class);
     * }
     * }</pre>
     */
    protected abstract void createGame();

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
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
        app.getMContext().getUiLooper().post(app::onBackPressed);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
