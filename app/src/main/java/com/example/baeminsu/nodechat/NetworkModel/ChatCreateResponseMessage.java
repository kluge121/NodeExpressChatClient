package com.example.baeminsu.nodechat.NetworkModel;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by baeminsu on 2018. 5. 29..
 */

public class ChatCreateResponseMessage {

    @SerializedName("msg")
    private String msg;
    @SerializedName("insertId")
    private int insertId;
    @SerializedName("talkperson")
    private String talkperson;
    @SerializedName("date")
    private Date date;

    public String getMsg() {
        return msg;
    }

    public int getInsertId() {
        return insertId;
    }

    public String getTalkperson() {
        return talkperson;
    }

    public Date getDate() {
        return date;
    }
}
