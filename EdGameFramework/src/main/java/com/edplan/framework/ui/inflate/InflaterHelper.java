package com.edplan.framework.ui.inflate;

import java.util.HashMap;

public class InflaterHelper {

    private static HashMap<String, IViewInflater<?>> inflaterHashMap = new HashMap<>();

    public static void registerInflater(String name, IViewInflater<?> inflater) {
        inflaterHashMap.put(name, inflater);
    }






}
