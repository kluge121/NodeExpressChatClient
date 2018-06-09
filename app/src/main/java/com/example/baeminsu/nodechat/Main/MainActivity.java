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
import com.example.baeminsu.nodechat.Model.TextMessage;
import com.example.baeminsu.nodechat.NetworkHelper.NetworkFacade;
import com.example.baeminsu.nodechat.Observer.Observer;
import com.example.baeminsu.nodechat.R;
import com.example.baeminsu.nodechat.Util.PropertyManager;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements Observer {

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
        initBasicRecylcerviewItem();
        settingWidget();
        settingChatList();
        //소켓 연결


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
        results = NetworkFacade.getInstace().requestGetChatRoomList();;
        adapter = new ChatListAdapter(this, results);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        if (adapter.getList().size() == 1)
            notChatNoti.setVisibility(View.VISIBLE);
        else
            notChatNoti.setVisibility(View.INVISIBLE);


        realm.addChangeListener(mainListListener);

        if (adapter.getItemCount() > 0) {
            adapter.getList().addChangeListener(new RealmChangeListener<RealmResults<ChatRoom>>() {
                @Override
                public void onChange(RealmResults<ChatRoom> element) {
                    adapter.notifyDataSetChanged();
                    if (adapter.getList().size() == 1)
                        notChatNoti.setVisibility(View.VISIBLE);
                    else
                        notChatNoti.setVisibility(View.INVISIBLE);
                }
            });
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


    private void initBasicRecylcerviewItem() {
        realm.beginTransaction();
        if (realm.where(TextMessage.class).equalTo("msg", "").findFirst() == null) {

            TextMessage initMessage = realm.createObject(TextMessage.class);
            initMessage.setId(-1);
            initMessage.setUnreadcount(0);
            initMessage.setChatName("");
            initMessage.setDate(new Date());
            initMessage.setReceiver("me");
            initMessage.setSender("me");
            initMessage.setReceiver("me");
            initMessage.setMsg("");
        }

        if (realm.where(ChatRoom.class).equalTo("chatId", -1).findFirst() == null) {
            ChatRoom chatRoom = realm.createObject(ChatRoom.class);
            chatRoom.setChatId(-1);
            chatRoom.setLastCheckDate(new Date());
            chatRoom.setLastDate(new Date());
            chatRoom.setUnReadCount(0);
            chatRoom.setChatName("");
            chatRoom.setNickname("");
            chatRoom.setLastMessage("");
        }
        realm.commitTransaction();

    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
        if (adapter.getList().size() == 1)
            notChatNoti.setVisibility(View.VISIBLE);
        else
            notChatNoti.setVisibility(View.INVISIBLE);
    }
}
