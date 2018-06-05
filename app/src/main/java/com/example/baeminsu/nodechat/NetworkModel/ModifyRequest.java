package com.example.baeminsu.nodechat.NetworkModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by baeminsu on 2018. 6. 3..
 */

public class ModifyRequest {


    @SerializedName("nickname")
    private String nickname;

    @SerializedName("modify")
    private String date;


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNickname() {

        return nickname;
    }

    public String getDate() {
        return date;
    }

    public ModifyRequest(String nickname, String date) {

        this.nickname = nickname;
        this.date = date;
    }
}
