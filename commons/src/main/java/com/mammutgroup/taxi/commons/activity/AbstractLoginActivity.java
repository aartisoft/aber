package com.mammutgroup.taxi.commons.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.mammutgroup.taxi.commons.fragment.LoginFragment;
import com.mammutgroup.taxi.commons.R;

/**
 * @author mushtu
 * @since 6/13/16.
 */
public abstract class AbstractLoginActivity extends BaseActivity implements LoginFragment.LoginClickListener {

    public static final int VERIFY_MOBILE_REQUEST = 100;

    @Override
    final protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_abstract);
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_login);
        loginFragment.setListener(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(com.mammutgroup.taxi.commons.R.anim.pull_in_right,
                com.mammutgroup.taxi.commons.R.anim.push_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VERIFY_MOBILE_REQUEST && resultCode == AbstractVerificationActivity.MOBILE_ACTIVATED)
            finish();
    }

}
