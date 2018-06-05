package com.example.baeminsu.nodechat.Util;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by baeminsu on 2018. 6. 3..
 */

public class TmpSocketManager {

    //추후 수신 소켓 따로 구현

    private static TmpSocketManager instance;
    private Socket sendSocket;
    private Socket receiveSocket;
    private int RECONNECTION_ATTEMPT = 10;
    private long CONNECTION_TIMEOUT = 30000;
    private static NetworkInterface mNetworkInterface;


    public static TmpSocketManager getInstance() {
        if (instance == null) {
            instance = new TmpSocketManager();
        }
        return instance;
    }

    public void createSendSocket() {
        try {
            Manager manager = new Manager(new URI(NetworkDefine.BASE_URL));
            manager.timeout(CONNECTION_TIMEOUT);
            manager.reconnection(true);
            manager.reconnectionAttempts(RECONNECTION_ATTEMPT);
            manager.reconnectionDelay(1000);
            sendSocket = manager.socket("/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public void createReceiveSocket() {
        try {
            Manager manager = new Manager(new URI(NetworkDefine.BASE_URL));
            manager.timeout(CONNECTION_TIMEOUT);
            manager.reconnection(true);
            manager.reconnectionAttempts(RECONNECTION_ATTEMPT);
            manager.reconnectionDelay(1000);
            receiveSocket = manager.socket("/" + PropertyManager.getInstance().getNickname());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }


    public Socket getSendSocket() {
        return sendSocket;
    }

    public Socket getReceiveSocket() {
        return receiveSocket;
    }

    public void connectSocket() {
        try {
            createSendSocket();
            createReceiveSocket();
            makeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectSockect() {
        unregisterConnectionAttributes();
        sendSocket.disconnect();
        sendSocket = null;
        receiveSocket.disconnect();
        receiveSocket = null;
        instance = null;

    }

    private void makeConnection() throws URISyntaxException {
        if (receiveSocket != null && sendSocket != null) {
            registerConnectionAttributes();
            sendSocket.connect();
            receiveSocket.connect();
        }
    }

    private void registerConnectionAttributes() {
        try {
            receiveSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
            receiveSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut);
            receiveSocket.on(Socket.EVENT_DISCONNECT, onServerDisconnect);
            receiveSocket.on(NetworkDefine.RECEIVE_MESSAGE, onReceiveMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterConnectionAttributes() {
        try {
            receiveSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectionError);
            receiveSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut);
            receiveSocket.off(Socket.EVENT_DISCONNECT, onServerDisconnect);
            receiveSocket.off(NetworkDefine.RECEIVE_MESSAGE, onReceiveMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Toast.makeText(AppApplication.getContext(), "소켓 연결 에러", Toast.LENGTH_SHORT).show();
        }
    };
    private Emitter.Listener onConnectionTimeOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Toast.makeText(AppApplication.getContext(), "소켓 연결 타임아웃", Toast.LENGTH_SHORT).show();
        }
    };
    private Emitter.Listener onServerDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Toast.makeText(AppApplication.getContext(), "소켓 서버 연결 끊김", Toast.LENGTH_SHORT).show();
        }
    };

    private Emitter.Listener onReceiveMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                Log.e("리시브메세지 콜백확인 ", "콜백진입");
                JSONObject jsonObject = (JSONObject) args[0];
                jsonObject.get("msg");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    public interface NetworkInterface {
        public void networkCallReceive(int responseType);
    }


}
