package com.mammutgroup.taxi.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mammutgroup.taxi.TaxiApplication;
import com.mammutgroup.taxi.config.UserConfig;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaxiApplication.initApplication();
        if(!UserConfig.userLoggedIn)
        {
            //show login page
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else  if(UserConfig.userLoggedIn && UserConfig.mobileVerified())
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();

        }else
        {
            Intent intent = new Intent(this,MobileVerificationActivity.class);
            startActivity(intent);
            finish();
        }

        //setContentView(R.layout.activity_launch);
    }


}
