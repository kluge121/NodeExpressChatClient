package com.example.baeminsu.nodechat.HTTP;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.baeminsu.nodechat.Login.LoginActivity;
import com.example.baeminsu.nodechat.Main.MainActivity;
import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.Model.Modify;
import com.example.baeminsu.nodechat.Model.TextMessage;
import com.example.baeminsu.nodechat.HTTP.RetrofitAPIInterface;
import com.example.baeminsu.nodechat.HTTP.RetrofitClient;
import com.example.baeminsu.nodechat.NetworkModel.LoginResponseMessage;
import com.example.baeminsu.nodechat.NetworkModel.ModifyRequest;
import com.example.baeminsu.nodechat.NetworkModel.ModifyResponse;
import com.example.baeminsu.nodechat.NetworkModel.User;
import com.example.baeminsu.nodechat.Util.PropertyManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by baeminsu on 2018. 6. 9..
 */

public class HttpManager {

    private RetrofitAPIInterface retrofitAPIInterface;

    public HttpManager() {
        retrofitAPIInterface = RetrofitClient.getClient().create(RetrofitAPIInterface.class);
    }

    public void requestSplashLogin(Context context) {

        final Context splashContext = context;

        if ("null".equals(PropertyManager.getInstance().getAccessToken())) {
            Intent intent = new Intent(splashContext, LoginActivity.class);
            splashContext.startActivity(intent);
            ((AppCompatActivity) splashContext).finish();
        } else {

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
                                        for (ChatRoom aList : chattList) {
                                            realm.copyToRealm(aList);
                                        }
                                    }
                                    List<TextMessage> messageList = response.body().getMessageList();
                                    if (messageList != null && messageList.size() > 0) {
                                        if (messageList.size() > 0) {
                                            for (TextMessage aList : messageList) {
                                                realm.copyToRealm(aList);
                                                String chatName = aList.getChatName();
                                                ChatRoom chatRoom = realm.where(ChatRoom.class).equalTo("nickname", aList.getSender()).equalTo("chatName", aList.getReceiver())
                                                        .or().equalTo("nickname", aList.getReceiver()).equalTo("chatName", aList.getSender()).findFirst();

                                                chatRoom.setUnReadCount(chatRoom.getUnReadCount() + 1);
                                            }
                                        }
                                    }

                                    Modify tmp = realm.where(Modify.class).findFirst();
                                    tmp.setModify(serverDate);
                                    realm.commitTransaction();

                                    Intent intent = new Intent(splashContext, MainActivity.class);
                                    splashContext.startActivity(intent);
                                    ((AppCompatActivity) splashContext).finish();

                                }

                                @Override
                                public void onFailure(Call<ModifyResponse> call, Throwable t) {

                                }
                            });

                            //서버에 새로 요청할 데이터 없음
                        } else {
                            Intent intent = new Intent(splashContext, MainActivity.class);
                            splashContext.startActivity(intent);
                            ((AppCompatActivity) splashContext).finish();
                        }

                    }
                    // 로그인 실패시 ->
                    else {
                        Intent intent = new Intent(splashContext, LoginActivity.class);
                        splashContext.startActivity(intent);
                        ((AppCompatActivity) splashContext).finish();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseMessage> call, Throwable t) {
                    Intent intent = new Intent(splashContext, LoginActivity.class);
                    splashContext.startActivity(intent);
                    ((AppCompatActivity) splashContext).finish();
                }
            }); //로그인


        }

    }

    public void reqeustJoin(final String nickname, Context context) {

        final Context loginContext = context;

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
                        Toast.makeText(loginContext, "인터넷 연결상태를 확인해주세요1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ("join success".equals(msg)) { // 새로 회원가입 값 다시 세팅
                        PropertyManager.getInstance().setAccessToken(accessToken);
                        PropertyManager.getInstance().setNickname(nickname);
                        Date date = response.body().getModify();
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        Modify modify = realm.createObject(Modify.class);
                        modify.setModify(new Date(0));
                        realm.commitTransaction();

                    } else if ("token reissuance".equals(msg)) { // 회원가입은 되있는데 토큰이 없을때 로그인 성공
                        PropertyManager.getInstance().setAccessToken(accessToken);
                    }
                    Intent intent = new Intent(loginContext, MainActivity.class);
                    intent.putExtra("nickname", nickname);
                    loginContext.startActivity(intent);
                    ((AppCompatActivity) loginContext).finish();
                }

                @Override
                public void onFailure(Call<LoginResponseMessage> call, Throwable t) {
                    Toast.makeText(loginContext, "인터넷 연결상태를 확인해주세요2", Toast.LENGTH_SHORT).show();
                    Log.e("cpcp", t.toString());
                }
            });

        } else {
            Toast.makeText(loginContext, "닉네임을 1자이상 입력해주세요.", Toast.LENGTH_LONG).show();
        }


    }


}
