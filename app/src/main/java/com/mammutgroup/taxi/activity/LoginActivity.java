package com.mammutgroup.taxi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mammutgroup.taxi.TaxiApplication;
import com.mammutgroup.taxi.service.remote.rest.api.auth.model.Token;
import com.mammutgroup.taxi.widget.AppProgressDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends TransitionActivity {

    @Bind(R.id.input_username)
    EditText inputUsername;
    @Bind(R.id.input_password)
    EditText inputPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.link_signup)
    TextView linkSignup;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        System.out.println("VVVVVVV create....");
    }


    @OnClick(R.id.btn_login)
    void login() {

        if(!basicValidate())
            return;
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        showProgressDialog();

        TaxiApplication.restClient().authService().getAccessToken(
                username,
                password,
                "password",
                "test",
                "test",
                new Callback<Token>() {
                    @Override
                    public void success(Token token, Response response) {
                        //todo update user config
                        //todo check mobile verification
                        hideProgressDialog();
                        transitToHome();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }


        );
    }

    @OnClick(R.id.link_signup)
    void signup() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean basicValidate()
    {
        return inputUsername.getText().length()> 0 && inputPassword.length() > 0 ;
    }

    private void transitToHome()
    {
        Intent intent = new Intent(this,MapsActivity2.class); // todo change to home
        startActivity(intent);
        finish();
    }

    private void showProgressDialog()
    {
        progressDialog = new AppProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.message_progress_login));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void hideProgressDialog()
    {
        progressDialog.dismiss();
        progressDialog = null;
    }

}
