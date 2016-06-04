package com.mammutgroup.taxi.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.mammutgroup.taxi.TaxiApplication;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.User;

/**
 * @author mushtu
 * @since 5/25/16.
 */
public class UserConfig {

    private static User currentUser;

    public static boolean userLoggedIn = false;
    public static boolean waitingForMobileVerify = false;

    private final static Object sync = new Object();

    public static boolean isUserRegistered()
    {
        synchronized (sync)
        {
            return currentUser != null;
        }
    }

    public static boolean mobileVerified()
    {
        synchronized (sync)
        {
            if(currentUser != null)
                return currentUser.isMobileVerified();
            else
                return false;
        }
    }

    public static void loadConfig()
    {
        synchronized (sync) {

            SharedPreferences preferences = TaxiApplication.context.getSharedPreferences("userconfing", Context.MODE_PRIVATE);
            String user = preferences.getString("user",null);
            if(user != null)
            {
                Gson gson = new Gson();
                currentUser = gson.fromJson(user,User.class);
            }
            waitingForMobileVerify = preferences.getBoolean("waitingForMobileVerify",false);
            waitingForMobileVerify = preferences.getBoolean("userLoggedIn",false);

        }
    }

    public static void saveConfig()
    {
        synchronized (sync) {
            SharedPreferences preferences = TaxiApplication.context.getSharedPreferences("userconfing", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if (currentUser != null) {
                Gson gson = new Gson();
                String user = gson.toJson(currentUser);
                editor.putString("user",user);
            }else
                editor.remove("user");
            editor.putBoolean("waitingForMobileVerify",waitingForMobileVerify);
            editor.putBoolean("userLoggedIn",userLoggedIn);
            editor.commit();
        }
    }


    public static void setCurrentUser(User user)
    {
        synchronized (sync)
        {
            currentUser = user;
        }
    }

    public static User getCurrentUser()
    {
        synchronized (sync)
        {
            return currentUser;
        }
    }


}
