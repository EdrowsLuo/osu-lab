package com.edlplan.osulab;

import android.os.Environment;
import android.util.Log;

import com.edlplan.osulab.ui.BackQuery;
import com.edlplan.osulab.ui.popup.PopupToast;
import com.edlplan.framework.Framework;
import com.edlplan.framework.MContext;
import com.edlplan.framework.graphics.opengl.BaseCanvas;
import com.edlplan.framework.graphics.opengl.GLWrapped;
import com.edlplan.framework.graphics.opengl.MainRenderer;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuad;
import com.edlplan.framework.graphics.opengl.batch.v2.object.TextureQuadBatch;
import com.edlplan.framework.graphics.opengl.objs.AbstractTexture;
import com.edlplan.framework.graphics.opengl.objs.GLTexture;
import com.edlplan.framework.main.EdMainActivity;
import com.edlplan.framework.main.MainApplication;
import com.edlplan.framework.ui.EdBufferedContainer;
import com.edlplan.framework.ui.EdView;
import com.edlplan.framework.ui.additions.popupview.defviews.RenderStatPopupView;
import com.edlplan.framework.ui.layout.EdLayoutParam;
import com.edlplan.framework.ui.layout.Param;
import com.edlplan.framework.ui.text.font.FontAwesome;
import com.edlplan.framework.ui.text.font.bmfont.BMFont;
import com.edlplan.framework.ui.widget.AbsoluteLayout;
import com.edlplan.framework.ui.widget.RelativeContainer;
import com.edlplan.framework.utils.dataobject.DataMapObject;
import com.edlplan.framework.utils.dataobject.Struct;

import java.io.File;
import java.io.IOException;

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
            Log.e("log", "err out put log!", e);
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


    public static class TestView extends RelativeContainer {

        AbstractTexture texture;

        public TestView(MContext context) {
            super(context);
            enableDepth();
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

            GLWrapped.depthTest.save();
            GLWrapped.depthTest.set(true);
            GLWrapped.clearDepthBuffer();

            TextureQuad quad1 = new TextureQuad();
            quad1.setTextureAndSize(GLTexture.White);
            quad1.size.set(200, 200);
            quad1.position.set(300, 300);

            TextureQuad quad2 = new TextureQuad();
            quad2.setTextureAndSize(GLTexture.Red);
            quad2.alpha.value = 0.5f;
            quad2.size.set(200, 200);
            quad2.position.set(200, 200);

            TextureQuadBatch.getDefaultBatch().addAll(quad1, quad2);


            GLWrapped.depthTest.restore();

        }
    }


    public class TestObj extends DataMapObject {

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
