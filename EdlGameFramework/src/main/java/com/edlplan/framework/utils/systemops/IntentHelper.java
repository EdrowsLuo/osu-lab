package com.edlplan.framework.utils.systemops;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.edlplan.framework.MContext;
import com.edlplan.framework.utils.interfaces.Consumer;

public class IntentHelper {


    public static void requireTextInput(
            @NonNull MContext context,
            @Nullable String rawData,
            @NonNull Consumer<String> onInput) {
        context.postToNativeView(() -> {
            InputDialog dialog = new InputDialog(context.getNativeContext());
            if (rawData != null) {
                dialog.setText(rawData);
            }
            dialog.showForResult(onInput);
        });
    }


}
