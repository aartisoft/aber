package com.mammutgroup.taxi.widget;

import android.app.ProgressDialog;
import android.content.Context;
import com.mammutgroup.taxi.activity.R;

/**
 * @author mushtu
 * @since 5/26/16.
 */
public class AppProgressDialog extends ProgressDialog {

    public AppProgressDialog(Context context) {
        super(context, R.style.AppTheme_Dark_Dialog);
    }
}
