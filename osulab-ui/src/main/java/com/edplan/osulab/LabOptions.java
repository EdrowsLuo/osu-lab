package com.edplan.osulab;

import com.edplan.framework.config.ConfigList;

public class LabOptions extends ConfigList{


    public LabOptions() {
        setListName("Lab Options");
        //表现等级，越高越好的表现
        putInt("Performance level", 10);
    }

}
