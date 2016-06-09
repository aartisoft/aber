package com.mammutgroup.taxi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mammutgroup.taxi.TaxiApplication;
import com.mammutgroup.taxi.config.UserConfig;
import com.mammutgroup.taxi.model.Driver;
import com.mammutgroup.taxi.model.DriverStatus;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.VerificationCode;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.VerificationCodeResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MobileVerificationCodeActivity extends AppCompatActivity {

    public static final int MOBILE_ACTIVATED = 200;

    private int timerValue = 60;


    @Bind(R.id.input_verification_code)
    EditText inputVerificationCode;
    @Bind(R.id.txt_timer)
    TextView txtTimer;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            txtTimer.setText(String.format("%02d s", timerValue));
            if (timerValue == 0)
                codeExpired();
            else {
                timerValue--;
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification_code);
        ButterKnife.bind(this);
        setupToolbar();
        restartTimer();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void setupToolbar() {
        getSupportActionBar().setTitle(R.string.title_toolbar_mobile_verification_code);
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

        switch (item.getItemId()) {
            case R.id.menu_item_done:
                submitVerificationCode();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    void submitVerificationCode() {
        if (inputVerificationCode.getText().length() == 0)
            return;//TODO check 6 digits
        VerificationCode code = new VerificationCode();
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
        });

    }

    private void codeExpired() {

        txtTimer.setText(getString(R.string.txt_label_resend_code));
        txtTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO send request to service
                restartTimer();
            }
        });
    }

    private void restartTimer() {
        timerValue = 60;
        txtTimer.setClickable(false);
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void transitToDriverHome() {
        Intent intent = new Intent(this, DriverHomeActivity.class);
        startActivity(intent);
        setResult(MOBILE_ACTIVATED);
        finish();
    }

}
