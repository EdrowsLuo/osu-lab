package com.edlplan.framework.utils.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AdvancedTxtReader {

    BufferedReader reader;

    String line;

    int index = -1;

    public AdvancedTxtReader(InputStream in) {
        reader = new BufferedReader(new InputStreamReader(in));
    }

    public int getIndex() {
        return index;
    }

    public String getBufferedLine() {
        return line;
    }

    public String readLine() throws IOException {
        if ((line = reader.readLine()) != null) {
            index++;
            return line;
        } else {
            return null;
        }
    }


}
