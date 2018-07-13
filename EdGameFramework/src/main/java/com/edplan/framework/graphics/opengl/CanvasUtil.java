package com.edplan.framework.graphics.opengl;

import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

public class CanvasUtil {
    public static final String Translate = "translate";
    public static final String Scale = "scale";

    public static void operateCanvas(BaseCanvas canvas, String op) {
        String[] spl = op.split(" ");
        for (int i = 0; i < spl.length; i++) {
            String key = spl[i];
            if (Translate.equalsIgnoreCase(key)) {
                i++;
                float tx = Float.parseFloat(spl[i]);
                i++;
                float ty = Float.parseFloat(spl[i]);
                canvas.translate(tx, ty);
            } else if (Scale.equalsIgnoreCase(Scale)) {
                i++;
                float s = Float.parseFloat(spl[i]);
                canvas.scaleContent(s);
            }
        }
    }

    public static Canvas2DOperationBuilder buildOperation() {
        return new Canvas2DOperationBuilder();
    }

    public static class Canvas2DOperationBuilder {
        List<String> data = new ArrayList<String>();

        public Canvas2DOperationBuilder translate(float x, float y) {
            data.add(Translate);
            data.add(x + "");
            data.add(y + "");
            return this;
        }

        public Canvas2DOperationBuilder scale(float s) {
            data.add(Scale);
            data.add(s + "");
            return this;
        }

        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder();
            for (String s : data) {
                sb.append(s).append(" ");
            }
            return sb.toString();
        }
    }
}
