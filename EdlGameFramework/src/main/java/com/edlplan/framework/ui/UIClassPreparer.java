package com.edlplan.framework.ui;

import com.edlplan.framework.ui.widget.AbsoluteLayout;
import com.edlplan.framework.ui.widget.FontAwesomeTextView;
import com.edlplan.framework.ui.widget.LinearLayout;
import com.edlplan.framework.ui.widget.RelativeContainer;
import com.edlplan.framework.ui.widget.RelativeLayout;
import com.edlplan.framework.ui.widget.RoundedButton;
import com.edlplan.framework.ui.widget.ScrollLayout;
import com.edlplan.framework.ui.widget.TextView;
import com.edlplan.framework.utils.reflect.ReflectHelper;

public class UIClassPreparer {
    public static void prepare() {
        ReflectHelper.ensureClass(
                EdView.class,
                EdAbstractViewGroup.class,
                EdBufferedContainer.class
        );

        ReflectHelper.ensureClass(
                AbsoluteLayout.class,
                FontAwesomeTextView.class,
                LinearLayout.class,
                RelativeContainer.class,
                RelativeLayout.class,
                RoundedButton.class,
                ScrollLayout.class,
                TextView.class
        );
    }
}
