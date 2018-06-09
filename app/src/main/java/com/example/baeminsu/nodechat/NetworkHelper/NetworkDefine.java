package com.example.baeminsu.nodechat.NetworkHelper;

/**
 * Created by baeminsu on 2018. 5. 29..
 */

public class NetworkDefine {

    public static final String BASE_URL = "http://10.0.2.2:3000";


    public static final int EVENT_CONNECT_ERR = 0;
    public static final int EVENT_CONNECT_TIMEOUT = 1;
    public static final int EVENT_DISCONNECT = 2;

    public static final String SEND_MESSAGE = "sendMessage";
    public static final String RECEIVE_MESSAGE = "relayMessage";
    public static final String REQUEST_DATE_MODIFY = "requestUpdateModify";
    public static final String REQUEST_CREATE_CHAT_ROOM = "requestCreateChatRoom";
    public static final String RESPONSE_CREATE_CHAT_ROOM = "responseCreateChatRoom";

    public static final String UNREAD_COUNT_READ_REQUEST = "unReadCountReadRequest";
    public static final String UNREAD_COUNT_READ_RESPONSE = "unReadCountReadResponse";






}
