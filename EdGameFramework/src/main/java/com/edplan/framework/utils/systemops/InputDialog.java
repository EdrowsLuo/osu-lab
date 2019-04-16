package com.edplan.framework.utils.systemops;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.edplan.framework.R;
import com.edplan.framework.utils.interfaces.Consumer;

public class InputDialog extends Dialog {

    private boolean singleLine = true;

    public InputDialog(@NonNull Context context) {
        super(context, R.style.Theme_MaterialComponents_BottomSheetDialog);
        setContentView(R.layout.dialog_for_input);
    }

    public boolean isSingleLine() {
        return singleLine;
    }

    public void setText(String text) {
        ((EditText) findViewById(R.id.editText)).setText(text);
    }

    public void showForResult(Consumer<String> onResult) {
        findViewById(R.id.button3).setOnClickListener(view->{
            onResult.consume(((EditText)findViewById(R.id.editText)).getText().toString());
            dismiss();
        });
        if (isSingleLine()) {
            ((EditText)findViewById(R.id.editText)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        onResult.consume(v.getText().toString());
                        dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }
        show();
    }
}
