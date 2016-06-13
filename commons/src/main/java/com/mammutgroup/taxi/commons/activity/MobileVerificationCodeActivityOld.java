/*
package com.mammutgroup.taxi.commons.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.mammutgroup.taxi.commons.config.UserConfig;
import com.mammutgroup.taxi.commons.service.remote.model.ApiResponse;
import com.mammutgroup.taxi.commons.service.remote.rest.AbstractCallback;
import com.mammutgroup.taxi.commons.service.remote.rest.TaxiRestClient;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.MobileAccessToken;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.MobileVerificationRequest;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.Token;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.mammutgroup.taxi.commons.R;

public abstract class MobileVerificationCodeActivity extends BaseActivity {

    public static final int MOBILE_ACTIVATED = 200;
    private final String TAG = MobileVerificationCodeActivity.class.getName();

    private final long MAX_TIME = 60000;
    private Long startTime = null;
    private EditText inputVerificationCode;
    private TextView txtTimer;
    private String mobile ;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = MAX_TIME - (System.currentTimeMillis() - startTime);
            int seconds = (int) (millis / 1000);
            if (seconds <= 0)
                codeExpired();
            else {
                txtTimer.setText(String.format("%02d s", seconds));
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification_code);
        inputVerificationCode = (EditText)findViewById(R.id.input_verification_code);
        txtTimer = (TextView)findViewById(R.id.txt_timer);
        setupToolbar();
        txtTimer.setClickable(false);
        mobile = getIntent().getExtras().getString("mobile");
        requestCode();
        Log.d(TAG,"onCreate called.");
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(startTime != null)
            timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void setupToolbar() {
        getSupportActionBar().setTitle(R.string.title_activity_mobile_verification_code);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_done)
        {
            submitVerificationCode();
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    */
/**
     * Builds access token.
     * default is passenger access.
     * @param accessToken
     * @return
     *//*

    protected MobileAccessToken buildMobileAccessToken(MobileAccessToken accessToken)
    {
        accessToken.setScope("Passenger");
        accessToken.setClientId("72827391eb471ed2d8174d3de4b8171a");
        accessToken.setClientSecret("87d45be523522cd7425efd0ea61d2583981384f4");
        return accessToken;
    }

    private MobileAccessToken getMobileAccessToken()
    {
        MobileAccessToken accessToken = new MobileAccessToken();
        accessToken.setMobile(mobile);
        accessToken.setToken(inputVerificationCode.getText().toString());
        return buildMobileAccessToken(accessToken);
    }

    void submitVerificationCode() {

        if (inputVerificationCode.getText().length() == 0)
            return;//TODO check 6 digits
        TaxiRestClient.getInstance().authService().getAccessToken(getMobileAccessToken(), new AbstractCallback<Token>(inputVerificationCode) {
            @Override
            protected void onHttpErrorStatus(RetrofitError error) {
                Snackbar.make(inputVerificationCode,error.getResponse().getBody().toString(),Snackbar.LENGTH_LONG);
            }

            @Override
            public void success(Token token, Response response) {
                UserConfig.getInstance().setToken(token);
                //TODO call get user
                transitToDriverHome();
            }

        });


      */
/*  VerificationCode code = new VerificationCode();
        code.setVerifyCode(inputVerificationCode.getText().toString());
        TaxiApplication.restClient().userService().verifyMobileNumber(code, new Callback<VerificationCodeResponse>() {
            @Override
            public void success(VerificationCodeResponse verificationCodeResponse, Response response) {
                //TODO handle passenger profile
                Driver driver = new Driver();
                driver.setEmail("driver@gmail.com");
                driver.setFirstName("mushtu");
                driver.setMobileVerified(true);
                driver.setHasNumber(true);
                driver.setStatus(DriverStatus.OUT_OF_SERVICE);
                UserConfig.setCurrentUser(driver);
                UserConfig.saveConfig();
                transitToDriverHome();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });*//*


    }

    private void codeExpired() {

        timerHandler.removeCallbacks(timerRunnable);
        startTime = null;
        txtTimer.setText(getString(R.string.txt_label_resend_code));
        txtTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCode();
            }
        });
    }

    private void restartTimer() {
        startTime = System.currentTimeMillis();
        txtTimer.setClickable(false);
        timerHandler.postDelayed(timerRunnable, 0);
    }

    protected abstract void transitToHome();

    private void transitToDriverHome() {
      */
/*  Intent intent = new Intent(this, DriverHomeActivity.class);
        startActivity(intent);
        setResult(MOBILE_ACTIVATED);
        finish();*//*

    }

    private void requestCode()
    {
        MobileVerificationRequest verificationRequest = new MobileVerificationRequest(mobile);
        TaxiRestClient.getInstance().authService().verifyMobile(verificationRequest, new AbstractCallback<ApiResponse>(inputVerificationCode) {
            @Override
            protected void onHttpErrorStatus(RetrofitError error) {
                //TODO
                codeExpired();
            }

            @Override
            public void success(ApiResponse apiResponse, Response response) {
                restartTimer();
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
                codeExpired();
            }
        });
    }

}
*/
