package com.example.baeminsu.nodechat.Login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.baeminsu.nodechat.NetworkHelper.NetworkFacade;
import com.example.baeminsu.nodechat.R;


/**
 * Created by baeminsu on 2018. 5. 22..
 */

public class LoginActivity extends AppCompatActivity {


    private EditText inputNickname;
    private Button confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setWidget();

    }

    private void setWidget() {
        inputNickname = findViewById(R.id.login_nickname_et);
        confirm = findViewById(R.id.login_btn_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkFacade.getInstace().requestHTTPJoin(inputNickname.getText().toString(), LoginActivity.this);
            }
        });
    }

}
