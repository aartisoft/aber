package com.mammutgroup.taxi.commons.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.mammutgroup.taxi.commons.R;
import com.mammutgroup.taxi.commons.exception.InvalidMobileNumberException;
import com.mammutgroup.taxi.commons.util.MobileNumberUtil;

/**
 * @author mushtu
 * @since 6/13/16.
 */
public class LoginFragment extends Fragment {

    private EditText inputMobileNumber;
    private Button btnLogin;
    private LoginClickListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        inputMobileNumber = (EditText) view.findViewById(R.id.input_mobile_number);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private boolean basicValidate() {

        boolean valid = true;
        try {
            MobileNumberUtil.validate(inputMobileNumber.getText().toString());
        } catch (InvalidMobileNumberException e) {
            valid = false;
        }

        return valid;
    }

    private void login() {
        if (!basicValidate())
            return;
        if (listener != null)
            listener.onClick(inputMobileNumber.getText().toString());
    }

    public void setListener(LoginClickListener listener) {
        this.listener = listener;
    }

    public interface LoginClickListener {
        void onClick(String mobile);
    }
}
