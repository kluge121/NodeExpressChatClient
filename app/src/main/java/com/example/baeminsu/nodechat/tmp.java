package com.example.baeminsu.nodechat;

import android.widget.Toast;

import com.example.baeminsu.nodechat.Util.NetworkDefine;

import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.emitter.Emitter;

/**
 * Created by baeminsu on 2018. 6. 4..
 */

public class tmp {


//
//        try {
//        socketManager = new Manager(new URI(NetworkDefine.BASE_URL));
//    } catch (URISyntaxException e) {
//        e.printStackTrace();
//    }
//    onConnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            Toast.makeText(getApplicationContext(), "소켓 연결, 서버로 부터 응답", Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    onMessageReceived = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            JSONObject receivedData = (JSONObject) args[0];
//
//        }
//    };
//
//        try {
//        socket = IO.socket(NetworkDefine.BASE_URL);
//        socket.on("resendMessage", onMessageReceived);
//        socket.on("sendMessage", onConnect);
//        socket.connect();
//    } catch (URISyntaxException e) {
//        e.printStackTrace();
//        Toast.makeText(getApplicationContext(), "소켓 연결 실패", Toast.LENGTH_SHORT).show();
//
//    }

}
