package com.example.baeminsu.nodechat.NetworkModel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by baeminsu on 2018. 5. 22..
 */

public class User {
    public User(String nickname, String accessToken) {
        this.nickname = nickname;
        this.accessToken = accessToken;
    }

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("accessToken")
    private String accessToken;


}
