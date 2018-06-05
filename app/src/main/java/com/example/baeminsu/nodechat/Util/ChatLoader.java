package com.example.baeminsu.nodechat.Util;

import android.util.Log;

import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.Model.TextMessage;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by baeminsu on 2018. 6. 2..
 */

public class ChatLoader {

    private Realm realm;

    public ChatLoader(Realm realm) {
        this.realm = realm;
    }

    public boolean isCheckChatRoom() {
        ChatRoom tmp = realm.where(ChatRoom.class).findFirst();
        return tmp != null;

    }

    private boolean isCheckChatRoomMessage(String chatName) {
        TextMessage tmp = realm.where(TextMessage.class).equalTo("chatName", chatName).findFirst();
        return tmp != null;

    }

    public RealmResults<ChatRoom> getChatRoomList() {
        if (isCheckChatRoom()) {
            RealmQuery<ChatRoom> query = realm.where(ChatRoom.class);
            return query.findAll();
        } else {
            Log.e("룸리스트 가져오기", "띠요요옹");
            return null;
        }
    }

    public RealmResults<TextMessage> getChatRoomMessage(String chatName) {
        if (isCheckChatRoomMessage(chatName)) {
            RealmQuery<TextMessage> query = realm.where(TextMessage.class).equalTo("chatName", chatName);
            return query.findAll();
        } else {
            Log.e("메세지 가져오기", "띠요요옹");
            return null;
        }
    }

}
