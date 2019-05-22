package com.edlplan.framework.utils;

import java.io.File;

public class FileUtils {
    public static void checkExistDir(File dir) {
        if (!dir.exists()) dir.mkdirs();
    }
}
