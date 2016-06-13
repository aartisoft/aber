package com.mammutgroup.taxi.commons.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import com.mammutgroup.taxi.commons.config.UserConfig;
import com.mammutgroup.taxi.commons.fragment.MobileVerificationFragment;
import com.mammutgroup.taxi.commons.service.remote.rest.AbstractCallback;
import com.mammutgroup.taxi.commons.service.remote.rest.TaxiRestClient;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.MobileAccessToken;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.Token;
import com.mammutgroup.taxi.commons.R;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author mushtu
 * @since 6/13/16.
 */
public abstract class AbstractVerificationActivity extends BaseActivity implements MobileVerificationFragment.VerifyClickListener {

    public static final int MOBILE_ACTIVATED = 200;
    MobileVerificationFragment verificationFragment;
    protected ProgressDialog progressDialog ;

    @Override
    final protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_abstract);
        verificationFragment = new MobileVerificationFragment();
        verificationFragment.setArguments(getIntent().getExtras());
        verificationFragment.setListener(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.placeholder, verificationFragment);
        ft.commit();
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }


    @Override
    public void onClick(String mobile, String code) {
        progressDialog.show();
        MobileAccessToken accessToken = new MobileAccessToken();
        accessToken.setMobile(mobile);
        accessToken.setToken(code);
        customizeAccessToken(accessToken);
        getAccessToken(accessToken);
    }

    protected MobileAccessToken customizeAccessToken(MobileAccessToken accessToken)
    {
        accessToken.setScope("Passenger");
        accessToken.setClientId("72827391eb471ed2d8174d3de4b8171a");
        accessToken.setClientSecret("87d45be523522cd7425efd0ea61d2583981384f4");
        return accessToken;
    }

    protected void getAccessToken(MobileAccessToken accessToken)
    {
        TaxiRestClient.getInstance().authService().getAccessToken(accessToken,
                new AbstractCallback<Token>(findViewById(R.id.verification_root_view)) {
                    @Override
                    protected void onHttpErrorStatus(RetrofitError error) {
                        //TODO
                        progressDialog.dismiss();
                    }

                    @Override
                    public void success(Token token, Response response) {
                        UserConfig.getInstance().setToken(token);
                        initialize();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                        super.failure(error);

                    }
                });
    }

    /**
     * initialization of required info for showing home( like getting user info and profile from service)
     */
    protected abstract void initialize();

    protected abstract void transitToHome() ;
}
