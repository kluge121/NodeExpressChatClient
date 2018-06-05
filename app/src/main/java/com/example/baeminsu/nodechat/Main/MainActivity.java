package com.example.baeminsu.nodechat.Main;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.example.baeminsu.nodechat.Dialog.CreateChatDialog;
import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.R;
import com.example.baeminsu.nodechat.Util.ChatLoader;
import com.example.baeminsu.nodechat.Util.PropertyManager;
import com.example.baeminsu.nodechat.Util.SocketManager;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private Toolbar toolbar;
    private String nickname;
    private FloatingActionButton fab;
    private TextView notChatNoti;
    private Realm realm = Realm.getDefaultInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingWidget();
        settingChatList();
        //소켓 연결
        SocketManager.getInstance().connectSocket();


    }

    private void settingWidget() {
        recyclerView = findViewById(R.id.main_recycler);
        toolbar = findViewById(R.id.main_toolbar);
        fab = findViewById(R.id.main_fab);
        notChatNoti = findViewById(R.id.main_not_chat_noti);
        toolbar.setTitle(PropertyManager.getInstance().getNickname());
        fab.setImageResource(R.drawable.ic_add_icon);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateChatDialog dialog = new CreateChatDialog(MainActivity.this);
                dialog.show();

            }
        });

    }

    private void settingChatList() {

        RealmResults<ChatRoom> results = null;


        ChatLoader chatLoader = new ChatLoader(realm);
        if (chatLoader.isCheckChatRoom()) {
            results = chatLoader.getChatRoomList();
            adapter = new ChatListAdapter(this, results);
            recyclerView.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);

            realm.addChangeListener(mainListListener);
        } else {
            notChatNoti.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private RealmChangeListener mainListListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            adapter.notifyDataSetChanged();
        }
    };

}