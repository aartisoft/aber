package com.mammutgroup.taxi.commons.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.mammutgroup.taxi.commons.R;
import com.mammutgroup.taxi.commons.service.remote.model.ApiResponse;
import com.mammutgroup.taxi.commons.service.remote.rest.AbstractCallback;
import com.mammutgroup.taxi.commons.service.remote.rest.TaxiRestClient;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.MobileVerificationRequest;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author mushtu
 * @since 6/13/16.
 */
public class MobileVerificationFragment extends Fragment {


    private final String TAG = MobileVerificationFragment.class.getSimpleName();

    private final long MAX_TIME = 60000;
    private Long startTime = null;
    private EditText inputVerificationCode;
    private TextView txtTimer;
    private Button btnVerify;
    private String mobile ;
    private VerifyClickListener listener;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = MAX_TIME - (System.currentTimeMillis() - startTime);
            int seconds = (int) (millis / 1000);
            if (seconds <= 0)
                codeExpired();
            else {
                txtTimer.setText(String.format("%02d s", seconds));
                timerHandler.postDelayed(this, 1000);
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobile_verification,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        inputVerificationCode = (EditText)view.findViewById(R.id.input_verification_code);
        txtTimer = (TextView)view.findViewById(R.id.txt_timer);
        btnVerify = (Button)view.findViewById(R.id.btn_verify_mobile);
        txtTimer.setClickable(false);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputVerificationCode.getText().length() != 6)
                    return;
                if(listener != null)
                    listener.onClick(mobile,inputVerificationCode.getText().toString());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if(getArguments() != null)
            mobile = getArguments().getString("mobile",null);
        requestCode();
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(startTime != null)
            timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onPause() {
        timerHandler.removeCallbacks(timerRunnable);
        super.onPause();
    }

    private void restartTimer() {
        startTime = System.currentTimeMillis();
        txtTimer.setClickable(false);
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void codeExpired() {

        timerHandler.removeCallbacks(timerRunnable);
        startTime = null;
        txtTimer.setText(getString(R.string.txt_label_resend_code));
        txtTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCode();
            }
        });
    }

    private void requestCode()
    {
        MobileVerificationRequest verificationRequest = new MobileVerificationRequest(mobile);
        TaxiRestClient.getInstance().authService().verifyMobile(verificationRequest, new AbstractCallback<ApiResponse>(inputVerificationCode) {
            @Override
            protected void onHttpErrorStatus(RetrofitError error) {
                //TODO
                codeExpired();
            }

            @Override
            public void success(ApiResponse apiResponse, Response response) {
                restartTimer();
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
                codeExpired();
            }
        });
    }

    public void setListener(VerifyClickListener listener) {
        this.listener = listener;
    }

    public interface VerifyClickListener {
        void onClick(String mobile,String code);
    }
}
