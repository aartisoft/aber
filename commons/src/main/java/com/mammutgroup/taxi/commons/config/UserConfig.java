package com.mammutgroup.taxi.commons.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.mammutgroup.taxi.commons.service.remote.rest.api.auth.model.Token;
import com.mammutgroup.taxi.commons.service.remote.rest.api.user.model.User;

/**
 * @author mushtu
 * @since 5/25/16.
 */
public class UserConfig {

    private volatile static UserConfig instance;

    private User currentUser;
    private Token token;
    private String locale = "en" ;

    private UserConfig() {

    }

    public static UserConfig getInstance() {
        if (instance == null) {
            synchronized (UserConfig.class) {
                if (instance == null) {
                    instance = new UserConfig();
                }
            }
        }
        return instance;
    }

    public synchronized User getCurrentUser() {
        return currentUser;
    }

    public synchronized void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public synchronized Token getToken() {
        return token;
    }

    public synchronized void setToken(Token token) {
        this.token = token;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    private void clearLocal()
    {
        currentUser = null;
        token = null;
    }


    public synchronized void loadConfig(Context context, Class<? extends User> userType) {
        clearLocal();
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences("userconfing", Context.MODE_PRIVATE);
        String user = preferences.getString("user", null);
        if (user != null)
            currentUser = gson.fromJson(user, userType);
        String token = preferences.getString("token",null);
        if(token != null)
            this.token = gson.fromJson(token,Token.class);

        locale = preferences.getString("locale","en");
    }

    public synchronized void saveConfig(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("userconfing", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        if (currentUser != null) {
            String user = gson.toJson(currentUser);
            editor.putString("user", user);
        } else
            editor.remove("user");
        if(token != null)
            editor.putString("token",gson.toJson(token));
        else
            editor.remove("token");
        if(locale != null)
            editor.putString("locale", locale);
        else editor.remove("locale");


        //TODO more

        editor.commit();

    }


}
