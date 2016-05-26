package com.mammutgroup.taxi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.mammutgroup.taxi.TaxiApplication;
import com.mammutgroup.taxi.activity.R;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.RegisterRequest;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.RegisterResponse;
import com.mammutgroup.taxi.widget.AppProgressDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignupActivity extends AppCompatActivity {

    @Bind(R.id.input_username)
    EditText inputUsername;
    @Bind(R.id.input_password)
    EditText inputPassword;
    @Bind(R.id.btn_signup)
    Button btnSignup;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_signup)
    void signup()
    {
        if(!validate())
            return;
        RegisterRequest request = new RegisterRequest();
        request.setUsername(inputUsername.getText().toString());
        request.setPassword(inputPassword.getText().toString());
        showProgressDialog();
        TaxiApplication.restClient().userService().register(request, new Callback<RegisterResponse>() {
            @Override
            public void success(RegisterResponse registerResponse, Response response) {
                hideProgressDialog();
                transitToNextStep();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private boolean validate()
    {
        if(inputUsername.getText().length() == 0 || inputPassword.getText().length() ==0)
            return false;
        return true;
    }

    private void showProgressDialog()
    {
        progressDialog = new AppProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.message_progress_signup));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void hideProgressDialog()
    {
        progressDialog.dismiss();
        progressDialog = null;
    }
    private void transitToNextStep()
    {
        Intent intent = new Intent(this,CompleteSignupActivity.class);
        startActivity(intent);
    }
}
