package com.example.baeminsu.nodechat.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * Created by baeminsu on 2018. 5. 22..
 */

@RealmClass
public class ChatRoom implements RealmModel {

    @SerializedName("chatId")
    int chatId;

    @SerializedName("chatName")
    String chatName;

    @SerializedName("nickname")
    String nickname;

    @SerializedName("lastMessage")
    String lastMessage;

    @SerializedName("unreadcount")
    int unReadCount;

    @SerializedName("lastDate")
    Date lastDate;

    public int getChatId() {
        return chatId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatName() {

        return chatName;
    }
}
