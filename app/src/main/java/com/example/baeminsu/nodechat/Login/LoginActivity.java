package com.example.baeminsu.nodechat.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baeminsu.nodechat.Main.MainActivity;
import com.example.baeminsu.nodechat.Model.Modify;
import com.example.baeminsu.nodechat.NetworkModel.LoginResponseMessage;
import com.example.baeminsu.nodechat.NetworkModel.User;
import com.example.baeminsu.nodechat.R;
import com.example.baeminsu.nodechat.Util.PropertyManager;
import com.example.baeminsu.nodechat.Util.RetrofitAPIInterface;
import com.example.baeminsu.nodechat.Util.RetrofitClient;

import java.util.Date;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by baeminsu on 2018. 5. 22..
 */

public class LoginActivity extends AppCompatActivity {


    private EditText inputNickname;
    private Button confirm;
    private String nickname;
    private RetrofitAPIInterface retrofitAPIInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setWidget();
        retrofitAPIInterface = RetrofitClient.getClient().create(RetrofitAPIInterface.class);

    }

    private void setWidget() {
        inputNickname = findViewById(R.id.login_nickname_et);
        confirm = findViewById(R.id.login_btn_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname = inputNickname.getText().toString();
                if (nickname.length() > 0) {
                    Call<LoginResponseMessage> loginCall = retrofitAPIInterface.loginUser(new User(nickname, PropertyManager.getInstance().getAccessToken()));
                    loginCall.enqueue(new Callback<LoginResponseMessage>() {
                        @Override
                        public void onResponse(Call<LoginResponseMessage> call, Response<LoginResponseMessage> response) {

                            String accessToken;
                            String msg;
                            try {
                                accessToken = response.body().getAccessToken();
                                msg = response.body().getMsg();
                            } catch (NullPointerException e) {
                                Toast.makeText(getApplicationContext(), "인터넷 연결상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if ("join success".equals(msg)) { // 새로 회원가입 값 다시 세팅
                                PropertyManager.getInstance().setAccessToken(accessToken);
                                PropertyManager.getInstance().setNickname(nickname);
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                Modify modify = realm.createObject(Modify.class);
                                modify.setModify(new Date());
                                realm.commitTransaction();
                                realm.close();

                            } else if ("token reissuance".equals(msg)) { // 회원가입인 되있는데 토큰이 없을때 로그인 성공
                                PropertyManager.getInstance().setAccessToken(accessToken);
                            }
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("nickname", nickname);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<LoginResponseMessage> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "인터넷 연결상태를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "닉네임을 1자이상 입력해주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}
