package com.example.baeminsu.nodechat.Main;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.Model.TextMessage;

/**
 * Created by baeminsu on 2018. 6. 6..
 */

public abstract class BasicViewHolder extends RecyclerView.ViewHolder {
    public BasicViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setView(ChatRoom chatRoom);
}