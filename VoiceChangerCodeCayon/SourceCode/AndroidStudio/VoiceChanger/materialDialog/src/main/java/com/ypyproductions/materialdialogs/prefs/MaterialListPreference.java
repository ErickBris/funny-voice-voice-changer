package com.ypyproductions.materialdialogs.prefs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;

import com.ypyproductions.materialdialogs.MaterialDialog;

/**
 * @author Marc Holder Kluver (marchold), Aidan Follestad (afollestad)
 */
public class MaterialListPreference extends ListPreference {

    private Context context;
    private MaterialDialog mDialog;

    public MaterialListPreference(Context context) {
        super(context);
        init(context);
    }

    public MaterialListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1)
            setWidgetLayoutResource(0);
    }

    @Override
    public void setEntries(CharSequence[] entries) {
        super.setEntries(entries);
        if (mDialog != null)
            mDialog.setItems(entries);
    }

    @Override
    protected void showDialog(Bundle state) {
        if (getEntries() == null || getEntryValues() == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.");
        }

        int preselect = findIndexOfValue(getValue());
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(getDialogTitle())
                .content(getDialogMessage())
                .icon(getDialogIcon())
                .negativeText(getNegativeButtonText())
                .items(getEntries())
                .itemsCallbackSingleChoice(preselect, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        onClick(null, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                        if (which >= 0 && getEntryValues() != null) {
                            String value = getEntryValues()[which].toString();
                            if (callChangeListener(value) && isPersistent())
                                setValue(value);
                        }
                    }
                });

        final View contentView = onCreateDialogView();
        if (contentView != null) {
            onBindDialogView(contentView);
            builder.customView(contentView, false);
        } else {
            builder.content(getDialogMessage());
        }

        mDialog = builder.show();
    }
}
