package com.mammutgroup.taxi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mammutgroup.taxi.TaxiApplication;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.VerificationCode;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.VerificationCodeResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MobileVerificationCodeActivity extends AppCompatActivity {

    @Bind(R.id.input_verification_code)
    EditText inputVerificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification_code);
        ButterKnife.bind(this);
        setupToolbar();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

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

        switch (item.getItemId())
        {
            case R.id.menu_item_done:
                submitVerificationCode();
                return true;
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    void submitVerificationCode()
    {

        VerificationCode code = new VerificationCode();
        code.setVerifyCode(inputVerificationCode.getText().toString());
        TaxiApplication.restClient().userService().verifyMobileNumber(code, new Callback<VerificationCodeResponse>() {
            @Override
            public void success(VerificationCodeResponse verificationCodeResponse, Response response) {
                transitToHome();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void transitToHome()
    {
        Intent intent = new Intent(this,MapsActivity2.class);
        startActivity(intent);
    }

}
