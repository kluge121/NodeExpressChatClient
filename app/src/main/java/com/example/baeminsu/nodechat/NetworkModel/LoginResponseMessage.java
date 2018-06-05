package com.example.baeminsu.nodechat.NetworkModel;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by baeminsu on 2018. 5. 26..
 */

public class LoginResponseMessage {


    @SerializedName("msg")
    private String msg;
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("modify")
    private Date modify;


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMsg() {
        return msg;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setModify(Date modify) {
        this.modify = modify;
    }

    public Date getModify() {

        return modify;
    }
}
