package com.example.baeminsu.nodechat.Splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.baeminsu.nodechat.NetworkHelper.NetworkFacade;
import com.example.baeminsu.nodechat.R;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        NetworkFacade.getInstace().requestHTTPLogin(Splash.this);
    }
}
