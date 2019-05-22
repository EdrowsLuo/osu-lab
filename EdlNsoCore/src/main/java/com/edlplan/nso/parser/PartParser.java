package com.edlplan.nso.parser;

import com.edlplan.nso.filepart.OsuFilePart;

public abstract class PartParser<T extends OsuFilePart> implements LinesParser {
    private String errMessage;

    public abstract T getPart();

    protected void setErrMessage(String msg) {
        errMessage = msg;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
