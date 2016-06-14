package com.mammutgroup.taxi.activity;

import android.content.Intent;
import com.mammutgroup.taxi.commons.activity.AbstractVerificationActivity;
import com.mammutgroup.taxi.commons.config.UserConfig;
import com.mammutgroup.taxi.commons.service.remote.rest.AbstractCallback;
import com.mammutgroup.taxi.commons.service.remote.rest.TaxiRestClient;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.MobileAccessToken;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.Token;
import com.mammutgroup.taxi.model.Driver;
import com.mammutgroup.taxi.model.DriverStatus;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author mushtu
 * @since 6/13/16.
 */
public class VerificationActivity extends AbstractVerificationActivity {

    /*public static final int MOBILE_ACTIVATED = 200;
    MobileVerificationFragment verificationFragment;
    ProgressDialog progressDialog = new ProgressDialog(this);*/

  /*  @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification_code);
        verificationFragment = (MobileVerificationFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_verification);
        verificationFragment.setArguments(getIntent().getExtras());
        verificationFragment.setListener(this);
    }*/

    @Override
    protected MobileAccessToken customizeAccessToken(MobileAccessToken accessToken) {
        accessToken.setScope("Driver");
        accessToken.setClientId("399daf59df7f2ddeff2721dc8432be41");
        accessToken.setClientSecret("216ef783c6b58515da1cabf496dcf5acebe35f63");
        return accessToken;
    }

    protected void initialize() {
        //TODO call api
        Driver driver = new Driver();
        driver.setEmail("driver@gmail.com");
        driver.setFirstName("mushtu");
        driver.setMobileVerified(true);
        driver.setStatus(DriverStatus.OUT_OF_SERVICE);
        UserConfig.getInstance().setCurrentUser(driver);
        transitToHome();
    }

    protected void transitToHome() {
        progressDialog.dismiss();
        Intent intent = new Intent(this, DriverHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        setResult(MOBILE_ACTIVATED);
        finish();
    }
}
