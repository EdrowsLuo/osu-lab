package com.edplan.nso;

import com.edplan.framework.MContext;

public class NsoCoreBased {
    private NsoCore core;

    public NsoCoreBased(NsoCore core) {
        this.core = core;
    }

    public NsoCore getCore() {
        return core;
    }

    public MContext getContext() {
        return core.getContext();
    }
}
