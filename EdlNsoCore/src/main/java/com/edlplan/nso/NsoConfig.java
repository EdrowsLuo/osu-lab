package com.edlplan.nso;

import com.edlplan.framework.config.ConfigList;
import com.edlplan.framework.config.property.ConfigBoolean;
import com.edlplan.framework.config.ConfigEntry;

public class NsoConfig extends ConfigList {
    public ConfigEntry hasUI;

    public NsoConfig() {
        hasUI = putBoolean("hasUI", true);
    }
}
