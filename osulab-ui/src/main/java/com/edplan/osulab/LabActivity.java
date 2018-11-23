package com.edplan.osulab;

import android.os.Environment;
import android.util.Log;

import com.edplan.framework.MContext;
import com.edplan.framework.graphics.opengl.BaseCanvas;
import com.edplan.framework.graphics.opengl.MainRenderer;
import com.edplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edplan.framework.graphics.opengl.objs.Color4;
import com.edplan.framework.main.EdMainActivity;
import com.edplan.framework.main.MainApplication;
import com.edplan.framework.math.RectF;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edplan.framework.ui.layout.EdLayoutParam;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.text.font.FontAwesome;
import com.edplan.framework.ui.text.font.bmfont.BMFont;
import com.edplan.framework.ui.widget.AbsoluteContainer;
import com.edplan.framework.ui.widget.AbsoluteLayout;
import com.edplan.framework.utils.HashDataMap;
import com.edplan.framework.utils.Setter;
import com.edplan.framework.utils.advance.BaseDataMap;
import com.edplan.framework.utils.dataobject.DataObject;
import com.edplan.framework.utils.dataobject.Struct;
import com.edplan.osulab.ui.BackQuery;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.edplan.framework.Framework;
import com.edplan.osulab.ui.popup.PopupToast;

public class LabActivity extends EdMainActivity {
    private MainApplication app;

    @Override
    protected void createGame() {

        try {
            File f = new File(Environment.getExternalStorageDirectory(), "osu!lab/logs/log.txt");
            if (f.exists()) {
                f.delete();
            }
            File dir = f.getParentFile();
            if (!dir.exists()) dir.mkdirs();
            Runtime.getRuntime().exec("logcat -f " + f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("log out!", "err out put log!", e);
        }


        //initialWithView(TestView.class);

        app = new LabApplication();
        app.setUpActivity(this);

        //DatabaseTable table=new DatabaseTable();
        //table.initial(TestDBLine.class);

        /*HashDataMap dataMap = new HashDataMap();
        dataMap.data.put("v1", "1.2222");
        TestObj obj = new TestObj();

        long time = System.currentTimeMillis();
        int times = 10000000;

        for (int i = 0; i < times; i++) {
            ((Setter<Float>)obj.getStruct().getItems().get(0).setter).set(1.22f);
        }

        Log.i("test-time", String.format("run %dtimes, cost %dms", times, System.currentTimeMillis() - time));*/

    }

    public static class TestRootView extends AbsoluteLayout {

        public TestRootView(MContext con) {
            super(con);
            EdLayoutParam param = new EdLayoutParam();
            param.width = Param.MODE_MATCH_PARENT;
            param.height = Param.MODE_MATCH_PARENT;
            addView(new TestView(con), param);
        }

        @Override
        public void onInitialLayouted() {
            super.onInitialLayouted();
        }
    }


    public static class TestView extends EdView {

        AbstractTexture texture;

        public TestView(MContext context) {
            super(context);
        }

        @Override
        public void onInitialLayouted() {
            super.onInitialLayouted();
            RenderStatPopupView.getInstance(getContext()).show();
            PopupToast.toast(getContext(), "create").show();
            Log.i("test", "create");
        }

        @Override
        protected void onDraw(BaseCanvas canvas) {
            super.onDraw(canvas);
            if (texture == null) {
                try {
                    texture = getContext().getAssetResource().loadTexture("osu/ui/menu-background-1.jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            for(int i = 0;i<10;i++) {
                canvas.drawTexture(
                        texture,
                        RectF.xywh(0, 0, canvas.getWidth(), canvas.getHeight()),
                        Color4.ONE,
                        1);
            }

        }
    }


    public class TestObj extends DataObject {

        private float v1;

        public float getV1() {
            return v1;
        }

        public void setV1(float v1) {
            this.v1 = v1;
        }

        @Override
        protected void onLoadStruct(Struct struct) {
            struct.add("v1", Float.class, this::getV1, this::setV1);
        }
    }

    public class LabApplication extends MainApplication {
        @Override
        public MainRenderer createRenderer(MContext context) {

            return new LabMainRenderer(context, this);
        }

        @Override
        public boolean onBackPressNotHandled() {

            return BackQuery.get().back();
        }

        @Override
        public void onExit() {

            LabGame.get().exit();
        }

        @Override
        public void onGLCreate() {

            double s = Framework.relativePreciseTimeMillion();
            super.onGLCreate();
            try {
                {
                    BMFont f = BMFont.getFont(BMFont.FontAwesome);
                    f.addFont(
                            mContext.getAssetResource().subResource("font"),
                            "osuFont.fnt");
                }
            } catch (Exception e) {
                e.printStackTrace();
                mContext.toast("读取字体osuFont失败：" + e.getMessage());
            }
            System.out.println("load font cost " + (int) (Framework.relativePreciseTimeMillion() - s) + "ms");
        }
    }
}
