package com.example.baeminsu.nodechat.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baeminsu.nodechat.Model.TextMessage;
import com.example.baeminsu.nodechat.R;

import java.text.SimpleDateFormat;


/**
 * Created by baeminsu on 2018. 1. 20..
 */

public class SentTextMessageViewHolder extends BasicViewHolder {

    private TextView messageBody;
    private TextView messageTime;
    private TextView unReadCount;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");


    public SentTextMessageViewHolder(View itemView) {
        super(itemView);
        messageBody = itemView.findViewById(R.id.text_message_body);
        messageTime = itemView.findViewById(R.id.text_message_time);
        unReadCount = itemView.findViewById(R.id.text_message_unread_count);
    }

    @Override
    public void setView(TextMessage textMessage) {
        messageBody.setText(textMessage.getMsg());
        messageTime.setText(dateFormat.format(textMessage.getDate()));
        unReadCount.setText(textMessage.getUnreadcount()+"");
    }


}
