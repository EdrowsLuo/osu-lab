package com.edplan.framework.ui;

import com.edplan.framework.ui.widget.AbsoluteLayout;
import com.edplan.framework.ui.widget.FontAwesomeTextView;
import com.edplan.framework.ui.widget.LinearLayout;
import com.edplan.framework.ui.widget.RelativeContainer;
import com.edplan.framework.ui.widget.RelativeLayout;
import com.edplan.framework.ui.widget.RoundedButton;
import com.edplan.framework.ui.widget.ScrollLayout;
import com.edplan.framework.ui.widget.TextView;
import com.edplan.framework.utils.reflect.ReflectHelper;

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
