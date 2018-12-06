package com.edplan.framework.utils.systemops;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.edplan.framework.R;
import com.edplan.framework.utils.interfaces.Consumer;

public class InputDialog extends Dialog {

    public InputDialog(@NonNull Context context) {
        super(context, R.style.Theme_Design_BottomSheetDialog);
        setContentView(R.layout.dialog_for_input);
    }

    public void setText(String text) {
        ((EditText) findViewById(R.id.editText)).setText(text);
    }

    public void showForResult(Consumer<String> onResult) {
        findViewById(R.id.button3).setOnClickListener(view->{
            onResult.consume(((EditText)findViewById(R.id.editText)).getText().toString());
            dismiss();
        });
        show();
    }
}
