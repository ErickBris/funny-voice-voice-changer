package com.ypyproductions.materialdialogs.prefs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ypyproductions.materialdialogs.MaterialDialog;
import com.ypyproductions.materialdialogs.R;
import com.ypyproductions.materialdialogs.MaterialDialog.Builder;
import com.ypyproductions.materialdialogs.MaterialDialog.ButtonCallback;
import com.ypyproductions.materialdialogs.util.DialogUtils;

/**
 * @author Aidan Follestad (afollestad)
 */
public class MaterialEditTextPreference extends EditTextPreference {

    private int mColor = 0;

    public MaterialEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP)
            mColor = DialogUtils.resolveColor(context, R.attr.colorAccent);
    }

    public MaterialEditTextPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onAddEditTextToDialogView(@NonNull View dialogView, @NonNull EditText editText) {
        if (editText.getParent() != null)
            ((ViewGroup) getEditText().getParent()).removeView(editText);
        ((ViewGroup) dialogView).addView(editText, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        getEditText().setText("");
        getEditText().append(getText());
        ViewParent oldParent = getEditText().getParent();
        if (oldParent != view) {
            if (oldParent != null)
                ((ViewGroup) oldParent).removeView(getEditText());
            onAddEditTextToDialogView(view, getEditText());
        }
    }

    @Override
    protected void showDialog(Bundle state) {
        Builder mBuilder = new MaterialDialog.Builder(getContext())
                .title(getDialogTitle())
                .icon(getDialogIcon())
                .positiveText(getPositiveButtonText())
                .negativeText(getNegativeButtonText())
                .callback(callback)
                .content(getDialogMessage());

        View layout = LayoutInflater.from(getContext()).inflate(R.layout.md_input_dialog_stub, null);
        onBindDialogView(layout);

        if (VERSION.SDK_INT < VERSION_CODES.LOLLIPOP)
            getEditText().getBackground().setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);

        TextView message = (TextView) layout.findViewById(android.R.id.message);
        if (getDialogMessage() != null && getDialogMessage().toString().length() > 0) {
            message.setVisibility(View.VISIBLE);
            message.setText(getDialogMessage());
        } else {
            message.setVisibility(View.GONE);
        }
        mBuilder.customView(layout, false);

        MaterialDialog mDialog = mBuilder.build();
        if (state != null)
            mDialog.onRestoreInstanceState(state);
        requestInputMethod(mDialog);

        mDialog.setOnDismissListener(this);
        mDialog.show();
    }

    /**
     * Callback listener for the MaterialDialog. Positive button checks with
     * OnPreferenceChangeListener before committing user entered text
     */
    private final ButtonCallback callback = new ButtonCallback() {
        @Override
        public void onPositive(MaterialDialog dialog) {
            String value = getEditText().getText().toString();
            if (callChangeListener(value) && isPersistent())
                setText(value);
        }
    };

    /**
     * Copied from DialogPreference.java
     */
    private void requestInputMethod(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}