package com.example.baeminsu.nodechat.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baeminsu.nodechat.Chat.Chat;
import com.example.baeminsu.nodechat.NetworkModel.ChatCreateResponseMessage;
import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.R;
import com.example.baeminsu.nodechat.Util.NetworkDefine;
import com.example.baeminsu.nodechat.Util.PropertyManager;
import com.example.baeminsu.nodechat.Util.RetrofitAPIInterface;
import com.example.baeminsu.nodechat.Util.RetrofitClient;
import com.example.baeminsu.nodechat.Util.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.baeminsu.nodechat.Util.NetworkDefine.REQUEST_CREATE_CHAT_ROOM;

/**
 * Created by baeminsu on 2018. 5. 29..
 */

public class CreateChatDialog extends Dialog {

    private EditText inputNickName;
    private Button submit;
    private Button cancel;
    private String nickname;
    private Context context;
    private RetrofitAPIInterface retrofitAPIInterface;

    public CreateChatDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_create_chat);
        setWidget();
        retrofitAPIInterface = RetrofitClient.getClient().create(RetrofitAPIInterface.class);
    }


    private void setWidget() {
        inputNickName = findViewById(R.id.cd_add_nickname_et);
        submit = findViewById(R.id.cd_add_category_submit_bt);
        cancel = findViewById(R.id.cd_add_category_cancel_bt);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname = inputNickName.getText().toString();
                JSONObject data = new JSONObject();
                try {
                    data.put("nickname", nickname);
                    data.put("requestNickname", PropertyManager.getInstance().getNickname());
                    SocketManager.getInstance().getSocket().emit(REQUEST_CREATE_CHAT_ROOM, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname = inputNickName.getText().toString();
                if (nickname.length() > 0) {

                    Call<ChatCreateResponseMessage> createChatCall = retrofitAPIInterface.createChat(nickname, PropertyManager.getInstance().getNickname());


                    createChatCall.enqueue(new Callback<ChatCreateResponseMessage>() {
                        @Override
                        public void onResponse(Call<ChatCreateResponseMessage> call, Response<ChatCreateResponseMessage> response) {
                            String msg = response.body().getMsg();

                            int insetId = response.body().getInsertId();
                            String talkperson = response.body().getTalkperson();


                            Date date = response.body().getDate();
                            if ("success".equals(msg)) {

                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                ChatRoom chatRoom = realm.createObject(ChatRoom.class);
                                chatRoom.setChatId(insetId);
                                chatRoom.setChatName(nickname);
                                chatRoom.setLastDate(date);
                                chatRoom.setLastMessage("");
                                chatRoom.setUnReadCount(0);
                                chatRoom.setNickname(talkperson);
                                realm.commitTransaction();

                                Intent intent = new Intent(context, Chat.class);
                                intent.putExtra("chatid", insetId);
                                intent.putExtra("chatName", nickname);
                                //TODO 이미 있는 채팅방인지 중복처리 과정 필요
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "해당 사용자가 없습니다", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<ChatCreateResponseMessage> call, Throwable t) {
                            Toast.makeText(context, "네트워크 연결상태를 확인해주세요" + t.toString(), Toast.LENGTH_SHORT).show();
//                            Log.e("체크", "네트워크 연결상태를 확인해주세요" + t.toString());
                        }
                    });
                }

                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


}
