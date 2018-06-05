package com.example.baeminsu.nodechat.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baeminsu.nodechat.Model.TextMessage;
import com.example.baeminsu.nodechat.R;
import com.example.baeminsu.nodechat.Util.PropertyManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by baeminsu on 2018. 1. 20..
 */

public class MessageAdapter extends RecyclerView.Adapter<BasicViewHolder> {

    private final int MY_SENT_TEXT_MESSAGE = 10;
    private final int MY_SENT_PHOTO_MESSAGE = 20;
    private final int OTHER_SENT_TEXT_MESSAGE = 11;
    private final int OTHER_SENT_PHOTO_MESSAGE = 21;
    private final int INVALID_TYPE = 0;


    private RealmResults<TextMessage> messageList;
    private Context context;
    private String myNickname = PropertyManager.getInstance().getNickname();

    public MessageAdapter(RealmResults<TextMessage> messageList, Context context) {
        if (messageList != null)
            this.messageList = messageList;
        this.context = context;
    }

    @Override
    public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (viewType == MY_SENT_TEXT_MESSAGE) {
            view = inflater.inflate(R.layout.recycler_item_sent_message, parent, false);
            return new SentTextMessageViewHolder(view);
        } else if (viewType == OTHER_SENT_TEXT_MESSAGE) {
            view = inflater.inflate(R.layout.recycler_item_receive_message, parent, false);
            return new ReceiveTextMessageViewHolder(view);
        }
        return new BasicViewHolder(new View(context)) {
            @Override
            public void setView(TextMessage textMessage) {
            }
        };
    }

    @Override
    public void onBindViewHolder(BasicViewHolder holder, int position) {
        if (messageList != null && messageList.size() > 0)
            holder.setView(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        if (messageList != null && messageList.size() > 0)
            return messageList.size();
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {

        if (messageList != null && messageList.size() > 0) {
            if (myNickname.equals(messageList.get(position).getSender())) {
                return MY_SENT_TEXT_MESSAGE;
            } else {
                return OTHER_SENT_TEXT_MESSAGE;
            }
        }
        return INVALID_TYPE;

    }
}
