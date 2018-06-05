package com.example.baeminsu.nodechat.Util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by baeminsu on 2018. 5. 26..
 */

public class PropertyManager {


    private static PropertyManager instance;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    private PropertyManager() {
        mSp = PreferenceManager.getDefaultSharedPreferences(AppApplication.getContext());
    }

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String NICKNAME = "nickname";


    public String getAccessToken() {
        return mSp.getString(ACCESS_TOKEN, "null");
    }

    public void setAccessToken(String accessToken) {
        mEditor = mSp.edit();
        mEditor.putString(ACCESS_TOKEN, accessToken);
        mEditor.apply();
    }


    public String getNickname() {
        return mSp.getString(NICKNAME, "null");
    }

    public void setNickname(String nickname) {
        mEditor = mSp.edit();
        mEditor.putString(NICKNAME, nickname);
        mEditor.apply();
    }

}
