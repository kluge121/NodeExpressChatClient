package com.example.baeminsu.nodechat.Socket;

import android.util.Log;

import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.Model.Modify;
import com.example.baeminsu.nodechat.Model.TextMessage;
import com.example.baeminsu.nodechat.NetworkHelper.NetworkDefine;
import com.example.baeminsu.nodechat.Observer.Observable;
import com.example.baeminsu.nodechat.Observer.Observer;
import com.example.baeminsu.nodechat.Util.PropertyManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.baeminsu.nodechat.NetworkHelper.NetworkDefine.REQUEST_DATE_MODIFY;
import static com.example.baeminsu.nodechat.NetworkHelper.NetworkDefine.UNREAD_COUNT_READ_RESPONSE;

/**
 * Created by baeminsu on 2018. 6. 3..
 */

public class SocketManager implements Observable {

    private static SocketManager instance;
    private Socket socket;
    private int RECONNECTION_ATTEMPT = 10;
    private long CONNECTION_TIMEOUT = 30000;
    private Observer chatListObserver;


    public static SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    private SocketManager() {
        createSendSocket();
    }

    private void createSendSocket() {
        try {
            Manager manager = new Manager(new URI(NetworkDefine.BASE_URL));
            manager.timeout(CONNECTION_TIMEOUT);
            manager.reconnection(true);
            manager.reconnectionAttempts(RECONNECTION_ATTEMPT);
            manager.reconnectionDelay(1000);
            socket = manager.socket("/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public Socket getSocket() {
        return socket;
    }


    public void connectSocket() {
        try {
//            createSendSocket();
            makeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectSockect() {
        unregisterAllConnectionAttributes();
        socket.disconnect();
        socket = null;

        instance = null;

    }

    private void makeConnection() throws URISyntaxException {
        if (socket != null) {
            registerAllConnectionAttributes();
            socket.connect();

        }
    }

    private void registerAllConnectionAttributes() {
        try {
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectionError);
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut);
            socket.on(Socket.EVENT_DISCONNECT, onServerDisconnect);
            socket.on(NetworkDefine.RECEIVE_MESSAGE, onReceiveMessage);
            socket.on(NetworkDefine.RESPONSE_CREATE_CHAT_ROOM, onReceiveCreateChatRoomMessage);
//            socket.on(UNREAD_COUNT_READ_RESPONSE, onUnReadCountReceiveMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterAllConnectionAttributes() {
        try {
            socket.off(Socket.EVENT_CONNECT_ERROR, onConnectionError);
            socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut);
            socket.off(Socket.EVENT_DISCONNECT, onServerDisconnect);
            socket.off(NetworkDefine.RECEIVE_MESSAGE, onReceiveMessage);
            socket.off(NetworkDefine.RESPONSE_CREATE_CHAT_ROOM, onReceiveCreateChatRoomMessage);
            socket.off(UNREAD_COUNT_READ_RESPONSE, onUnReadCountReceiveMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerChatRoomEnterRelativeSocket() {
        socket.on(UNREAD_COUNT_READ_RESPONSE, onUnReadCountReceiveMessage);
    }

    public void unnregisterChatRoomEnterRelativeSocket() {
        socket.off(UNREAD_COUNT_READ_RESPONSE, onUnReadCountReceiveMessage);
    }


    private Emitter.Listener onConnectionError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        }
    };
    private Emitter.Listener onConnectionTimeOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        }
    };
    private Emitter.Listener onServerDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
        }
    };
    private Emitter.Listener onReceiveCreateChatRoomMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            try {
                JSONObject jsonObject = (JSONObject) args[0];
                int insertId = jsonObject.getInt("id");
                String lastMessage = jsonObject.getString("lastMessage");
                int unReadCount = jsonObject.getInt("unreadcount");
                /// 날짜 처리
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                String strLastCheckDate = jsonObject.getString("lastCheckDate");
                String strLastMessageDate = jsonObject.getString("lastDate");
                Date lastCheckDate = dateFormat.parse(strLastCheckDate);
                Date lastMessageDate = dateFormat.parse(strLastMessageDate);
                ///

                String sender = jsonObject.getString("sender");
                String receiver = jsonObject.getString("receiver");

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                // 로컬 시간 변경
                Modify modify = realm.where(Modify.class).findFirst();
                modify.setModify(lastMessageDate);

                //내부DB에 채팅방 저장
                ChatRoom chatRoom = realm.createObject(ChatRoom.class);
                chatRoom.setChatId(insertId);
                chatRoom.setUnReadCount(unReadCount);
                chatRoom.setLastDate(lastMessageDate);
                chatRoom.setLastMessage(lastMessage);
                chatRoom.setLastCheckDate(lastCheckDate);

                if (sender.equals(PropertyManager.getInstance().getNickname())) {
                    chatRoom.setChatName(receiver);
                    chatRoom.setNickname(sender);
                } else {
                    chatRoom.setChatName(sender);
                    chatRoom.setNickname(receiver);
                }

                realm.commitTransaction();
                realm.close();

                //서버 시간 변경
                JSONObject modifyDate = new JSONObject();
                modifyDate.put("modify", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(lastMessageDate));
                modifyDate.put("nickname", PropertyManager.getInstance().getNickname());
                socket.emit(REQUEST_DATE_MODIFY, modifyDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private Emitter.Listener onUnReadCountReceiveMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            JSONArray jsonArray = (JSONArray) args[0];
            Realm realm = Realm.getDefaultInstance();
            Log.e("체크", "언리드 카운팅 : " + jsonArray.length() + "개");
            realm.beginTransaction();
            String myNickname = PropertyManager.getInstance().getNickname();
            try {
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject tmp = (JSONObject) jsonArray.get(i);
                    int tmpId = tmp.getInt("id");
                    String sender = tmp.getString("sender");
                    int tmpUnreadCount = tmp.getInt("unreadcount");
                    TextMessage tmpTextMessage = realm.where(TextMessage.class).equalTo("id", tmpId).findFirst();
                    tmpTextMessage.setUnreadcount(tmpUnreadCount);


                }
                realm.commitTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
            realm.close();


        }
    };
    private Emitter.Listener onReceiveMessage = new Emitter.Listener() {
        @Override
        public synchronized void call(Object... args) {
            try {

                JSONObject jsonObject = (JSONObject) args[0];
                String sender = jsonObject.getString("sender");
                String receiver = jsonObject.getString("receiver");
                String myNickname = PropertyManager.getInstance().getNickname();

                if (myNickname.equals(sender) || myNickname.equals(receiver)) {


                    int messageId = jsonObject.getInt("id");
                    int unreadCount = jsonObject.getInt("unreadcount");
                    String msg = jsonObject.getString("msg");
                    String strDate = jsonObject.getString("date");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date date = dateFormat.parse(strDate);

                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    // 로컬 시간 변경
                    Modify modify = realm.where(Modify.class).findFirst();
                    modify.setModify(date);

                    //내부DB에 메세지 저장
                    TextMessage textMessage = realm.createObject(TextMessage.class);
                    textMessage.setId(messageId);
                    textMessage.setDate(date);
                    textMessage.setMsg(msg);
                    textMessage.setReceiver(receiver);
                    textMessage.setSender(sender);
                    textMessage.setUnreadcount(unreadCount);


                    ChatRoom chatRoom;
                    if (myNickname.equals(sender)) {
                        textMessage.setChatName(receiver);
                        chatRoom = realm.where(ChatRoom.class).equalTo("chatName", receiver).findFirst();
                    } else {
                        textMessage.setChatName(sender);
                        chatRoom = realm.where(ChatRoom.class).equalTo("chatName", sender).findFirst();
                    }

                    if (!sender.equals(PropertyManager.getInstance().getNickname()))
                        chatRoom.setUnReadCount(chatRoom.getUnReadCount() + 1);
                    chatRoom.setLastMessage(msg);
                    chatRoom.setLastDate(date);

                    realm.commitTransaction();


                    // 서버 시간 변경
                    JSONObject modifyDate = new JSONObject();
                    modifyDate.put("modify", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(date));
                    modifyDate.put("nickname", PropertyManager.getInstance().getNickname());
                    socket.emit(REQUEST_DATE_MODIFY, modifyDate);
                    Log.e("메세지 리시브", "리시브");
                    realm.close();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void attach(Observer o) {
        chatListObserver = o;

    }

    @Override
    public void detach(Observer o) {
        chatListObserver = null;

    }

    @Override
    public void notifyObservers() {
        chatListObserver.update();

    }


    public interface NetworkInterface {
        public void networkCallReceive(int responseType);
    }


}
