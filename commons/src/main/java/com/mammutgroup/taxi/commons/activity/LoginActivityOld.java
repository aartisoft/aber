/*
package com.mammutgroup.taxi.commons.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.mammutgroup.taxi.commons.exception.InvalidMobileNumberException;
import com.mammutgroup.taxi.commons.service.remote.model.ApiResponse;
import com.mammutgroup.taxi.commons.service.remote.rest.AbstractCallback;
import com.mammutgroup.taxi.commons.service.remote.rest.TaxiRestClient;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.MobileVerificationRequest;
import com.mammutgroup.taxi.commons.util.MobileNumberUtil;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.mammutgroup.taxi.commons.R;

public abstract class LoginActivity extends BaseActivity {

    public static final int VERIFY_MOBILE_REQUEST = 100;
    public static final int RESULT_LOGIN_SUCCESS = 1;
    public static final int RESULT_LOGIN_FAILED = 0;
    private final String TAG = LoginActivity.class.getSimpleName();
    private EditText inputMobileNumber;
    private Button btnLogin;
    private ProgressDialog progressDialog;


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputMobileNumber = (EditText) findViewById(R.id.input_mobile_number);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VERIFY_MOBILE_REQUEST && resultCode == MobileVerificationCodeActivity.MOBILE_ACTIVATED) {
            setResult(RESULT_LOGIN_SUCCESS);
            finish();
        }
    }

    void login() {
        if (!basicValidate())
            return;
        showProgressDialog();
        //String mobileNumber = inputMobileNumber.getText().toString();
        transitToVerification();
  */
/*
        MobileVerificationRequest verificationRequest = new MobileVerificationRequest(mobileNumber);
        TaxiRestClient.getInstance().authService().verifyMobile(verificationRequest, new AbstractCallback<ApiResponse>(btnLogin) {
            @Override
            protected void onHttpErrorStatus(RetrofitError error) {
                //TODO
            }

            @Override
            public void success(ApiResponse apiResponse, Response response) {
                hideProgressDialog();

            }

            @Override
            public void failure(RetrofitError error) {
                hideProgressDialog();
                super.failure(error);
            }
        });*//*

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

    private void transitToVerification() {
        Intent intent = new Intent(this,getVerificationActivity());
        intent.putExtra("mobile", inputMobileNumber.getText().toString());
        startActivityForResult(intent, VERIFY_MOBILE_REQUEST);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.message_progress_login));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
        progressDialog = null;
    }


    protected abstract Class<? extends MobileVerificationCodeActivity> getVerificationActivity();

}
*/
