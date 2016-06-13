package com.mammutgroup.taxi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.mammutgroup.taxi.commons.activity.AbstractLoginActivity;
import com.mammutgroup.taxi.commons.activity.BaseActivity;
import com.mammutgroup.taxi.commons.fragment.LoginFragment;

/**
 * @author mushtu
 * @since 6/13/16.
 */
public class LoginActivity extends AbstractLoginActivity {

   /* @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_login);
        loginFragment.setListener(new LoginFragment.LoginClickListener() {
            @Override
            public void onClick(String mobile) {
                Intent intent = new Intent(LoginActivity.this,VerificationActivity.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });
    }*/

    @Override
    public void onClick(String mobile) {
        Intent intent = new Intent(LoginActivity.this,VerificationActivity.class);
        intent.putExtra("mobile",mobile);
        startActivity(intent);
    }
}
