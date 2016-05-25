package com.mammutgroup.taxi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mammutgroup.taxi.exception.InvalidMobileNumberException;
import com.mammutgroup.taxi.util.MobileNumberUtil;


public class CompleteSignupActivity extends AppCompatActivity {

    @Bind(R.id.btn_init_profile)
    Button btnInitProfile;
    @Bind(R.id.input_user_profile_name)
    EditText inputUserProfileName;
    @Bind(R.id.input_mobile_number)
    EditText inputMobileNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_signup);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complete_signup, menu);
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

    @OnClick(R.id.btn_init_profile)
    void saveProfile()
    {
        String profileName = inputUserProfileName.getText().toString();
        String mobile = inputMobileNumber.getText().toString();
        try{
            MobileNumberUtil.validate(mobile);
            //todo validate profileName

        }catch (InvalidMobileNumberException ex)
        {
            //todo
        }

    }


}
