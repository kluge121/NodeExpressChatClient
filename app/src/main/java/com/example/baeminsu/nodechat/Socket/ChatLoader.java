package com.example.baeminsu.nodechat.Socket;

import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.Model.TextMessage;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by baeminsu on 2018. 6. 2..
 */

public class ChatLoader {


    public boolean isCheckChatRoom() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ChatRoom> results = realm.where(ChatRoom.class).findAll();
        if (results.size() == 0) {
            realm.close();
            return false;
        } else {
            realm.close();
            return true;
        }

    }

    public boolean isCheckChatRoomMessage(String chatName) {
        Realm realm = Realm.getDefaultInstance();
        TextMessage tmp = realm.where(TextMessage.class).equalTo("chatName", chatName).equalTo("chatName", "").findFirst();
        realm.close();
        return tmp != null;

    }

    public RealmResults<ChatRoom> getChatRoomList() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<ChatRoom> query = realm.where(ChatRoom.class);
        realm.close();
        return query.findAll();
    }

    public RealmResults<TextMessage> getChatRoomMessage(String chatName) {

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<TextMessage> query = realm.where(TextMessage.class).equalTo("chatName", chatName);
        RealmResults<TextMessage> list = query.or().equalTo("id", -1).findAll();
        realm.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            TextMessage tmp = list.get(i);
            if (tmp.getSender().equals(tmp.getChatName())) {
                tmp.setUnreadcount(0);
            }
        }
        realm.commitTransaction();
        realm.close();
        return list;

    }
}


