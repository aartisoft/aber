package com.mammutgroup.taxi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mammutgroup.taxi.exception.InvalidMobileNumberException;
import com.mammutgroup.taxi.util.MobileNumberUtil;
import com.mammutgroup.taxi.widget.AppProgressDialog;

public class LoginActivity extends AppCompatActivity {

    public static final int VERIFY_MOBILE_REQUEST = 100;

    @Bind(R.id.input_mobile_number)
    EditText inputMobileNumber;
    @Bind(R.id.btn_login)
    Button btnLogin;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VERIFY_MOBILE_REQUEST && resultCode == MobileVerificationCodeActivity.MOBILE_ACTIVATED)
            finish();
    }


    @OnClick(R.id.btn_login)
    void login() {
        if (!basicValidate())
            return;
        showProgressDialog();
        String mobileNumber = inputMobileNumber.getText().toString();
        //TODO replace postdelayed with service call
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                transitToVerification();
            }
        }, 5000);


//        TaxiApplication.restClient().authService().getAccessToken(
//                mobileNumber,
//                password,
//                "password",
//                "test",
//                "test",
//                new Callback<Token>() {
//                    @Override
//                    public void success(Token token, Response response) {
//                        //todo update user config
//                        //todo check mobile verification
//                        hideProgressDialog();
//                        transitToHome();
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//
//                    }
//                }
//
//
//        );
    }


    private boolean basicValidate() {

        boolean valid = true;
        try {
            MobileNumberUtil.validate(inputMobileNumber.getText().toString());
        } catch (InvalidMobileNumberException e) {
            valid = false;
        }

        return valid;
    }

    private void transitToHome() {
        Intent intent = new Intent(this, MapsActivity2.class); // todo change to home
        startActivity(intent);
        finish();
    }

    private void transitToVerification() {
        Intent intent = new Intent(this, MobileVerificationCodeActivity.class);
        startActivityForResult(intent, VERIFY_MOBILE_REQUEST);
    }

    private void showProgressDialog() {
        progressDialog = new AppProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.message_progress_login));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
        progressDialog = null;
    }

}
