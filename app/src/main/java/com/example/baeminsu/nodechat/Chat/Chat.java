package com.example.baeminsu.nodechat.Chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.Model.Message;
import com.example.baeminsu.nodechat.Model.TextMessage;
import com.example.baeminsu.nodechat.R;
import com.example.baeminsu.nodechat.Util.ChatLoader;
import com.example.baeminsu.nodechat.Util.NetworkDefine;
import com.example.baeminsu.nodechat.Util.PropertyManager;
import com.example.baeminsu.nodechat.Util.SocketManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by baeminsu on 2018. 5. 22..
 */

public class Chat extends AppCompatActivity {


    private EditText inputChatEdit;
    private Button sendChatBtn;
    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;

    //현재 채팅방 정보 - realm에서 받아온 것
    private ChatRoom chatRoom;
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        realm = Realm.getDefaultInstance();
        getChatInfo(getIntent().getStringExtra("chatName"));
        setWidget();
        getMessage();
        setHomeAsUpIndicator();


    }


    private void sendChatMessage() {

        JSONObject data = new JSONObject();
        try {
            data.put("sender", PropertyManager.getInstance().getNickname());
            data.put("receiver", chatRoom.getChatName());
            data.put("msg", inputChatEdit.getText().toString());
            data.put("unreadcount", 1);
            data.put("chatName", chatRoom.getChatName());

            SocketManager.getInstance().getSocket().emit(NetworkDefine.SEND_MESSAGE, data);
            inputChatEdit.setText("");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setWidget() {
        inputChatEdit = findViewById(R.id.chat_room_edit);
        sendChatBtn = findViewById(R.id.chat_room_send);
        toolbar = findViewById(R.id.chat_room_toolbar);
        recyclerView = findViewById(R.id.chat_room_recycler);
        sendChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChatMessage();
            }
        });
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(Chat.this, LinearLayoutManager.VERTICAL, false));
        //채팅 입력창 문자 길이에 따른 sendBtn Enable
        if (inputChatEdit.getText().toString().length() == 0) {
            sendChatBtn.setEnabled(false);
        } else {
            sendChatBtn.setEnabled(true);
        }
        inputChatEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (inputChatEdit.getText().toString().trim().length() == 0) {
                    sendChatBtn.setEnabled(false);
                } else {
                    sendChatBtn.setEnabled(true);
                }

            }
        });


        int chatId = getIntent().getIntExtra("chatid", -1);

        chatRoom = realm.where(ChatRoom.class).equalTo("chatId", chatId).findFirst();
        getSupportActionBar().setTitle(chatRoom.getChatName());
    }

    private void setHomeAsUpIndicator() {
        if (toolbar == null) return;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getMessage() {

        //현재는 수신자 발신자 체크로 메세지를 가져오지만 나중에는 채팅방 이름 또는 아이디로 가져오기로 바뀌어야 함
        Realm realm = Realm.getDefaultInstance();
        ChatLoader chatLoader = new ChatLoader(realm);
        RealmResults<TextMessage> messageList = chatLoader.getChatRoomMessage(chatRoom.getChatName());
        adapter = new MessageAdapter(messageList, Chat.this);
        recyclerView.setAdapter(adapter);

        realm.addChangeListener(chatListListener);

    }

    private void getChatInfo(String chatName) {
        Realm realm = Realm.getDefaultInstance();
        chatRoom = realm.where(ChatRoom.class).equalTo("chatName", chatName).findFirst();
    }

    private RealmChangeListener chatListListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        realm.removeChangeListener(chatListListener);
    }
}
