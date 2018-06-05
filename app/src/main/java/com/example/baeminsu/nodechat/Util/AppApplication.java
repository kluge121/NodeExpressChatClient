package com.example.baeminsu.nodechat.Util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import io.realm.Realm;

/**
 * Created by baeminsu on 2018. 5. 26..
 */

public class AppApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        Realm.init(this);
    }


    public static Context getContext() {
        return mContext;
    }
}
