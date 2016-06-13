package com.mammutgroup.taxi.activity;

import android.content.Intent;
import android.os.Bundle;
import com.mammutgroup.taxi.commons.activity.BaseActivity;
import com.mammutgroup.taxi.commons.config.UserConfig;

public class LaunchActivity extends BaseActivity {

    private final int LOGIN_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(UserConfig.getInstance().getToken() == null)
        {
            //show login page
            startLogin();

        }else
            startHome();

    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUEST && requestCode == AbstractLoginActivity.RESULT_LOGIN_SUCCESS)
        {
            startHome();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }*/

    private void startLogin()
    {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void startHome()
    {
        Intent intent = new Intent(this,DriverHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
