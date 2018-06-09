package com.example.baeminsu.nodechat.NetworkHelper;

import android.content.Context;

import com.example.baeminsu.nodechat.HTTP.HttpManager;
import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.Model.TextMessage;
import com.example.baeminsu.nodechat.Socket.ChatLoader;
import com.example.baeminsu.nodechat.Socket.SocketManager;

import org.json.JSONObject;

import io.realm.RealmResults;

import static com.example.baeminsu.nodechat.NetworkHelper.NetworkDefine.REQUEST_CREATE_CHAT_ROOM;
import static com.example.baeminsu.nodechat.NetworkHelper.NetworkDefine.UNREAD_COUNT_READ_REQUEST;

/**
 * Created by baeminsu on 2018. 6. 9..
 */

public class NetworkFacade {


    private static NetworkFacade instance = new NetworkFacade();
    private HttpManager httpManager;
    private SocketManager socketManager;
    private ChatLoader chatLoader;

    private NetworkFacade() {
        httpManager = new HttpManager();
        socketManager = SocketManager.getInstance();
        chatLoader = new ChatLoader();

        connectionSockect();
    }


    public static NetworkFacade getInstace() {
        return instance;
    }

    //HTTP - 로그인 + 오프라인동안 수신한 메세지 로딩 요청
    public void requestHTTPLogin(Context context) {
        httpManager.requestSplashLogin(context);
    }

    //HTTP - 회원가입 요청
    public void requestHTTPJoin(String nickname, Context context) {
        httpManager.reqeustJoin(nickname, context);
    }

    //소켓 연결
    public void connectionSockect() {
        socketManager.connectSocket();
    }

    //소켓 해제
    public void disconnectionSocket() {
        socketManager.disconnectSockect();
    }

    //채팅방 입장 소켓 등록
    public void chatRoomEnterSocketOpen() {
        socketManager.registerChatRoomEnterRelativeSocket();
    }

    //채팅방 퇴장 소켓 제거
    public void charRoomLeaveSocketOff() {
        socketManager.unnregisterChatRoomEnterRelativeSocket();
    }


    //채팅방 목록 요청
    public RealmResults<ChatRoom> requestGetChatRoomList() {
        if (chatLoader.isCheckChatRoom()) {
            return chatLoader.getChatRoomList();
        }
        return null;

    }

    //메세지 목록 요청
    public RealmResults<TextMessage> requestGetChatMessageList(String chatName) {
        if (chatLoader.isCheckChatRoomMessage(chatName)) {
            return chatLoader.getChatRoomMessage(chatName);
        }
        return null;
    }

    //메세지 읽음처리 요청 (소켓)
    public void requestReadCountMessage(JSONObject data) {
        SocketManager.getInstance().getSocket().emit(UNREAD_COUNT_READ_REQUEST, data);
    }

    //채팅방 생성 요청 (소켓)
    public void requestCreateChatRoom(JSONObject data) {
        SocketManager.getInstance().getSocket().emit(REQUEST_CREATE_CHAT_ROOM, data);
    }


}
