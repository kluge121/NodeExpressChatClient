package com.example.baeminsu.nodechat.Splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.baeminsu.nodechat.Login.LoginActivity;
import com.example.baeminsu.nodechat.Main.MainActivity;
import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.Model.Message;
import com.example.baeminsu.nodechat.Model.TextMessage;
import com.example.baeminsu.nodechat.NetworkModel.LoginResponseMessage;
import com.example.baeminsu.nodechat.Model.Modify;
import com.example.baeminsu.nodechat.NetworkModel.ModifyRequest;
import com.example.baeminsu.nodechat.NetworkModel.ModifyResponse;
import com.example.baeminsu.nodechat.NetworkModel.User;
import com.example.baeminsu.nodechat.R;
import com.example.baeminsu.nodechat.Util.PropertyManager;
import com.example.baeminsu.nodechat.Util.RetrofitAPIInterface;
import com.example.baeminsu.nodechat.Util.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if ("null".equals(PropertyManager.getInstance().getAccessToken())) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {

            Log.e("스플래쉬", "초기화작업 진행");
            final RetrofitAPIInterface retrofitAPIInterface = RetrofitClient.getClient().create(RetrofitAPIInterface.class);
            Call<LoginResponseMessage> loginCall = retrofitAPIInterface.loginUser
                    (new User(PropertyManager.getInstance().getNickname(), PropertyManager.getInstance().getAccessToken()));

            loginCall.enqueue(new Callback<LoginResponseMessage>() {
                @Override
                public void onResponse(Call<LoginResponseMessage> call, Response<LoginResponseMessage> response) {

                    String msg;
                    try {
                        msg = response.body().getMsg();
                    } catch (NullPointerException e) {
                        msg = "null";
                    }

                    // 로그인 성공시 -> 서버와 로컬의 수정시간 동기화 체크 작업
                    if ("success".equals(msg) || "token reissuance".equals(msg)) {

                        final Realm realm = Realm.getDefaultInstance();

                        Modify modify = realm.where(Modify.class).findFirst();
                        Date localDate;

                        if (modify != null) {
                            localDate = modify.getModify();
                        } else {
                            localDate = new Date(0);
                        }
                        final Date serverDate = response.body().getModify();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                        Log.e("로컬", dateFormat.format(localDate));
                        Log.e("서버", dateFormat.format(serverDate));
                        //서버에 오프라인동안 전송된 데이터 요청
                        if (localDate.getTime() < serverDate.getTime()) {

                            Call<ModifyResponse> call2 = retrofitAPIInterface.requestModifyInfo(new ModifyRequest(PropertyManager.getInstance().getNickname(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(localDate)));
                            call2.enqueue(new Callback<ModifyResponse>() {
                                @Override
                                public void onResponse(Call<ModifyResponse> call, Response<ModifyResponse> response) {
                                    realm.beginTransaction();
                                    //채팅룸 정보 받아오기
                                    List<ChatRoom> chattList = response.body().getChatRoomList();
                                    if (chattList != null && chattList.size() > 0) {
                                        Log.e("체크", "채팅룸 받는중");
                                        for (ChatRoom aList : chattList) {
                                            realm.copyToRealm(aList);
                                            Log.e("체크",aList.getChatName()+" dd");

                                        }
                                    }
                                    List<TextMessage> messageList = response.body().getMessageList();
                                    if (messageList != null && messageList.size() > 0) {
                                        if (messageList.size() > 0) {
                                            Log.e("체크", "메세지 받는중");
                                            for (TextMessage aList : messageList) {
                                                Log.e("메시지받는거 뭐냐", aList.getMsg());
                                                realm.copyToRealm(aList);
                                                String chatName = aList.getChatName();
//                                                ChatRoom chatRoom = realm.where(ChatRoom.class).equalTo("chatName", chatName).findFirst();
//                                                chatRoom.setUnReadCount(chatRoom.getUnReadCount() + 1);
                                            }
                                        }
                                    }

                                    Modify tmp = realm.where(Modify.class).findFirst();
                                    tmp.setModify(serverDate);
                                    realm.commitTransaction();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                    finish();

                                }

                                @Override
                                public void onFailure(Call<ModifyResponse> call, Throwable t) {

                                }
                            });

                            //서버에 새로 요청할 데이터 없음
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }


                    }
                    // 로그인 실패시 ->
                    else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseMessage> call, Throwable t) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }); //로그인


        }


    }
}
