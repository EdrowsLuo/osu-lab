package com.edplan.framework.utils.dataframe;

import java.io.IOException;

public interface DataEntry {

    int nextInt() throws IOException;

    double nextDouble() throws IOException;

    String nextString() throws IOException;

    DataEntry nextEntry() throws IOException;

    void closeEntry() throws IOException;

}
