package com.example.baeminsu.nodechat.HTTP;

import com.example.baeminsu.nodechat.NetworkModel.ChatCreateResponseMessage;
import com.example.baeminsu.nodechat.NetworkModel.LoginResponseMessage;
import com.example.baeminsu.nodechat.NetworkModel.ModifyRequest;
import com.example.baeminsu.nodechat.NetworkModel.ModifyResponse;
import com.example.baeminsu.nodechat.NetworkModel.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by baeminsu on 2018. 5. 24..
 */

public interface RetrofitAPIInterface {

    @POST("/login")
    Call<LoginResponseMessage> loginUser(@Body User user);

    @POST("/login/modify")
    Call<ModifyResponse> requestModifyInfo(@Body ModifyRequest modifyRequest);


    @GET("/logout")
    Call<User> logout();

    @GET("/chat/create")
    Call<ChatCreateResponseMessage> createChat(@Query("nickname") String nickname, @Query("reqnickname") String reqnickname);


}
