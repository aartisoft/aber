package com.mammutgroup.taxi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.mammutgroup.taxi.TaxiApplication;
import com.mammutgroup.taxi.exception.InvalidMobileNumberException;
import com.mammutgroup.taxi.service.remote.model.MobileNumber;
import com.mammutgroup.taxi.util.MobileNumberUtil;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MobileVerificationActivity extends AppCompatActivity {

    @Bind(R.id.input_mobile_number)
    EditText inputMobileNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);
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
        // customize toolbar
        getSupportActionBar().setTitle(getResources().getString(R.string.title_toolbar_mobile_verification));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_item_done:
                submitMobileNumber();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    void submitMobileNumber()
    {
        String mobile = inputMobileNumber.getText().toString();
        try{
            MobileNumberUtil.validate(mobile);
            MobileNumber number = new MobileNumber();
            number.setNumber(mobile);
            TaxiApplication.restClient().userService().changeMobileNumber(number, new Callback<Object>() {

                @Override
                public void success(Object o, Response response) {
                    transitToVerifyStep();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }catch (InvalidMobileNumberException ex)
        {
            //todo
        }

    }

    private void transitToVerifyStep()
    {
        Intent intent = new Intent(this,MobileVerificationCodeActivity.class);
        startActivity(intent);
        finish();
    }


}
