package com.mammutgroup.taxi.commons.service.remote.rest;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import com.mammutgroup.taxi.commons.service.remote.model.ApiResponse;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * @author mushtu
 * @since 6/12/16.
 */
public abstract class AbstractCallback<R> implements Callback<R> {

    private final int STATUS_OK = 200;
    private final int STATUS_CREATED = 201;
    private final int STATUS_ACCEPTED = 202;


    private View view ;

    public AbstractCallback(View view)
    {
        this.view = view;
    }

    @Override
    public void failure(RetrofitError error) {
            switch (error.getKind())
            {
                case CONVERSION:
                    //TODO handle
                    break;
                case NETWORK:
                    Snackbar.make(view, "Network error!", Snackbar.LENGTH_LONG).show(); //TODO give more detailed message
                    break;
                case HTTP:
                    int status = error.getResponse().getStatus();
                    if(status == STATUS_ACCEPTED || status == STATUS_CREATED)
                    {
                        //ignore
                    }else
                        onHttpErrorStatus(error);
                    break;
                default:
                    break;
            }
    }

    protected abstract void onHttpErrorStatus(RetrofitError error);
}
