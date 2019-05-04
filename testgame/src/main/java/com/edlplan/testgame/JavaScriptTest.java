package com.edlplan.testgame;

import com.edplan.framework.MContext;
import com.edplan.framework.test.performance.Tracker;
import com.edplan.framework.ui.EdView;
import com.edplan.framework.ui.layout.MarginLayoutParam;
import com.edplan.framework.ui.layout.Orientation;
import com.edplan.framework.ui.layout.Param;
import com.edplan.framework.ui.widget.Fragment;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.RoundedButton;
import com.edplan.framework.ui.widget.TextView;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JavaScriptTest implements ITest {

    private TextView textView;

    private JsEngine engine;

    @Override
    public Fragment createFragment() {
        return new Fragment(){
            @Override
            protected void onCreate(MContext context) {
                super.onCreate(context);
                engine = new JsEngine();
                setContentView(new LinearLayout(context) {{
                    orientation = Orientation.DIRECTION_VERTICAL;
                    children(
                            textView = new TextView(context) {{
                                layoutParam(new MarginLayoutParam() {{
                                    width = Param.MODE_WRAP_CONTENT;
                                    height = Param.MODE_WRAP_CONTENT;
                                }});
                                setText("test");
                            }},
                            new RoundedButton(context) {{
                                layoutParam(new MarginLayoutParam() {{
                                    width = Param.makeUpDP(100);
                                    height = Param.makeUpDP(50);
                                }});
                                setOnClickListener(view -> {
                                    Tracker.createTmpNode("jsTest")
                                            .wrap(engine::request)
                                            .then(System.out::println);
                                });
                            }}
                    );
                }});
            }
        };
    }

    public class JsEngine{
        private Class clazz;
        private org.mozilla.javascript.Context rhino;
        private Scriptable scope;

        private String jsCode = "";
        private String testCode =
                "var method_Api_rhino_test = ScriptAPI.getMethod(\"rhino_test\",[java.lang.String])\n" +
                        "function rhino_test() {\n" +
                        "    var str = \"jzy666\";\n" +
                        "    method_Api_rhino_test.invoke(javaContext,str);\n" +
                        "}" +
                        "rhino_test()";
        public JsEngine(){
            this.clazz = JsEngine.class;
            initJsEngine();
        }

        private void initJsEngine(){
            jsCode = "print(\"hello\")";//"var ScriptAPI = java.lang.Class.forName(\"" + JsEngine.class.getName() + "\", true, javaLoader);\n"
                    //+ testCode;
        }

        public void request(){
            rhino = org.mozilla.javascript.Context.enter();
            rhino.setOptimizationLevel(1);
            try{
                scope = rhino.initStandardObjects();
                // 这两句是设置当前的类做为上下文以及获取当前的类加载器，以便于 rhino 通过反射获取档期类
                //ScriptableObject.putProperty(scope,"javaContext", org.mozilla.javascript.Context.javaToJS(this,scope));
                //ScriptableObject.putProperty(scope,"javaLoader", org.mozilla.javascript.Context.javaToJS(clazz.getClassLoader(),scope));
                //执行 js 代码
                Object x = rhino.evaluateString(scope, jsCode, clazz.getSimpleName(), 1, null);
            }finally {
                //退出
                org.mozilla.javascript.Context.exit();
            }
        }

        // 对应类中需要需要被调用的方法，可以做为 JS 代码执行时的回调
        public void rhino_test(String str) {
            textView.setText("rhino_test: " + str);
        }
    }
}
