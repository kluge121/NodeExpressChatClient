package com.example.baeminsu.nodechat.NetworkModel;

import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.Model.Message;
import com.example.baeminsu.nodechat.Model.TextMessage;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by baeminsu on 2018. 6. 3..
 */

public class ModifyResponse {

    @SerializedName("chatroomlist")
    private List<ChatRoom> chatRoomList;

    @SerializedName("messagelist")
    private List<TextMessage> messageList;


    public void setChatRoomList(List<ChatRoom> chatRoomList) {

        this.chatRoomList = chatRoomList;
    }

    public List<ChatRoom> getChatRoomList() {
        return chatRoomList;
    }

    public List<TextMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<TextMessage> messageList) {
        this.messageList = messageList;
    }
}
