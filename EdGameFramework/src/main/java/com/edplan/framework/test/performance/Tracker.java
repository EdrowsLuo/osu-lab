package com.edplan.framework.test.performance;

import com.edplan.framework.Framework;

import java.util.ArrayList;
import java.util.HashMap;

public class Tracker {
    private static boolean enable = true;

    public static final String DRAW_ARRAY = "DRAW_ARRAY";
    public static final String PREPARE_VERTEX_DATA = "PREPARE_VERTEX_DATA";
    public static final String INJECT_DATA = "INJECT_DATA";
    public static final String MAIN_LOOPER = "MAIN_LOOPER";

    public static final String DRAW_UI = "DRAW_UI";
    public static final String INVALIDATE_MEASURE_AND_LAYOUT = "INVALIDATE_MEASURE";

    public static final String TOTAL_FRAME_TIME = "TOTAL_FRAME_TIME";

    public static final TrackNode DrawArray;
    public static final TrackNode PrepareVertexData;
    public static final TrackNode InjectData;
    public static final TrackNode MainLooper;
    public static final TrackNode DrawUI;
    public static final TrackNode TotalFrameTime;

    public static final TrackNode InvalidateMeasureAndLayout;

    private static ArrayList<TrackNode> nodes;
    private static HashMap<String, TrackNode> namemap;


    static {
        nodes = new ArrayList<TrackNode>();
        namemap = new HashMap<String, TrackNode>();

        DrawArray = register(DRAW_ARRAY);
        PrepareVertexData = register(PREPARE_VERTEX_DATA);
        InjectData = register(INJECT_DATA);
        MainLooper = register(MAIN_LOOPER);

        InvalidateMeasureAndLayout = register(INVALIDATE_MEASURE_AND_LAYOUT);
        DrawUI = register(DRAW_UI);
        TotalFrameTime = register(TOTAL_FRAME_TIME);
    }

    public static TrackNode register(String name) {
        TrackNode node = new TrackNode(nodes.size(), name);
        nodes.add(node);
        namemap.put(name, node);
        return node;
    }

    public static void reset() {
        for (TrackNode n : nodes) {
            n.totalTimeMS = 0;
            n.trackedTimes = 0;
            n.latestRecordTime = 0;
            n.stack = 0;
        }
    }

    public static class TrackNode {
        public double totalTimeMS;
        public long trackedTimes;
        public double latestRecordTime;
        public int id;
        public String name;

        private int stack = 0;

        public TrackNode(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public void watch() {
            if (stack == 0) {
                latestRecordTime = Framework.relativePreciseTimeMillion();
            } else {
                double time = Framework.relativePreciseTimeMillion();
                totalTimeMS += time - latestRecordTime;
                latestRecordTime = time;
            }
            stack++;
        }

        public void end() {
            trackedTimes++;
            stack--;
            if (stack == 0) {
                totalTimeMS += Framework.relativePreciseTimeMillion() - latestRecordTime;
            } else {
                double time = Framework.relativePreciseTimeMillion();
                totalTimeMS += time - latestRecordTime;
                latestRecordTime = time;
            }
        }

        @Override
        public String toString() {

            StringBuilder sb = new StringBuilder();
            sb.append("------------------------------------\n");
            sb.append("name         : " + name + " (" + id + ")\n");
            sb.append("totalTime    : " + totalTimeMS + "ms\n");
            sb.append("trackedTimes : " + trackedTimes + "\n");
            sb.append("------------------------------------");
            return sb.toString();
        }
    }
}
