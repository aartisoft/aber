package com.mammutgroup.taxi.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Iraj on 6/9/2016.
 */
public class TaxiInfoDialogBox extends Dialog implements
        android.view.View.OnClickListener {

    @Bind(R.id.taxi_info_dialog_txt)
    public TextView taxiInfoTxt;
    @Bind(R.id.taxi_info_dialog_btn_yes)
    public Button yes;
    @Bind(R.id.taxi_info_dialog_btn_no)
    public Button no;
    String message;

    public TaxiInfoDialogBox(Activity a, String message) {
        super(a);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.taxi_detail_for_passenger);
        ButterKnife.bind(this);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        taxiInfoTxt.setText(message);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.taxi_info_dialog_btn_yes:
                dismiss();
                break;
            case R.id.taxi_info_dialog_btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}