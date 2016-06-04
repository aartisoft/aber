package com.mammutgroup.taxi.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mammutgroup.taxi.config.UserConfig;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Iraj on 6/3/2016.
 */
public class EditProfileActivity extends AppCompatActivity {

    @Bind(R.id.input_email_layout)
    TextInputLayout emailTil;
    @Bind(R.id.input_email)
    EditText email;
    @Bind(R.id.input_family)
    EditText lastName;
    @Bind(R.id.btn_save)
    Button saveBtn;
    @Bind(R.id.input_first_name)
    EditText firstName;
    @Bind(R.id.sex)
    Spinner sexSpinner;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    protected void setupToolbar() {
        getSupportActionBar().setTitle(R.string.title_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = UserConfig.getCurrentUser();
        setContentView(R.layout.edit_profile);
        ButterKnife.bind(this);
        emailTil.setErrorEnabled(true);
        if (user != null) {
            String email = user.getEmail();
            if (email != null)
                this.email.setText(email);
            String lastName = user.getLastName();
            if (lastName != null)
                this.lastName.setText(lastName);
            String firstName = user.getFirstName();
            if (firstName != null)
                this.firstName.setText(firstName);
            String gender = user.getGender();
            if (gender != null) {
                if (gender.equalsIgnoreCase("female"))
                    sexSpinner.setSelection(1);
            }
        }
        setupToolbar();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return true;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @OnClick(R.id.btn_save)
    void save() {
        if (!isValidEmail(email.getText())) {
            emailTil.setError(getString(R.string.invalid_email_address));
            return;
        }
        User currentUser = UserConfig.getCurrentUser();
        //TODO remove this line
        if (currentUser == null)
            currentUser = new User();
        if (email != null) {
            String emailStr = email.getText().toString();
            if (emailStr != null)
                currentUser.setEmail(emailStr);
        }
        if (firstName != null) {
            String str = firstName.getText().toString();
            if (str != null)
                currentUser.setFirstName(str);
        }
        if (lastName != null) {
            String str = lastName.getText().toString();
            if (str != null)
                currentUser.setLastName(str);
        }
        if (firstName != null && lastName != null) {
            String str1 = firstName.getText().toString();
            String str2 = lastName.getText().toString();
            StringBuilder fullName = new StringBuilder();
            fullName = fullName.append(str1);
            fullName = fullName.append(" ");
            fullName = fullName.append(str2);
            currentUser.setFullName(fullName.toString());
        }

        currentUser.setGender(String.valueOf(sexSpinner.getSelectedItem()));

        UserConfig.setCurrentUser(currentUser);
        UserConfig.saveConfig();
        Toast.makeText(getApplicationContext(), R.string.profile_successfully_saved, Toast.LENGTH_LONG).show();
        onBackPressed();
    }
}
