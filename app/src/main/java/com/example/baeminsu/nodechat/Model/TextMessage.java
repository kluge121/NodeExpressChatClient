package com.example.baeminsu.nodechat.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by baeminsu on 2018. 6. 4..
 */

@RealmClass
public class TextMessage implements RealmModel, Message {

    @SerializedName("id")
    private int id;
    @SerializedName("msg")
    private String msg;
    @SerializedName("unreadcount")
    private int unreadcount;
    @SerializedName("date")
    private Date date;
    @SerializedName("sender")
    private String sender;
    @SerializedName("receiver")
    private String receiver;
    @SerializedName("chatName")
    private String chatName;


    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setUnreadcount(int unreadcount) {
        this.unreadcount = unreadcount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public int getUnreadcount() {
        return unreadcount;
    }

    public Date getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    @Override
    public void convertToViewContent() {

    }
}
