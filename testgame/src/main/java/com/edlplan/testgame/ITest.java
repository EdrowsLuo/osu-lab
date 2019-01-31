package com.edlplan.testgame;

import com.edplan.framework.ui.widget.Fragment;

public interface ITest {

    default String getName() {
        return getClass().getSimpleName();
    }

    Fragment createFragment();

}
