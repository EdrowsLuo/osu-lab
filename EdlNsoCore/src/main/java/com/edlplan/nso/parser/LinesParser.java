package com.edlplan.nso.parser;

import com.edlplan.nso.NsoException;

public interface LinesParser {
    public boolean parse(String l) throws NsoException;
}
