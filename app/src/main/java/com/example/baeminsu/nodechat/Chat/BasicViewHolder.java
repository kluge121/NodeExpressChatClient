package com.example.baeminsu.nodechat.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.baeminsu.nodechat.Model.TextMessage;

/**
 * Created by baeminsu on 2018. 6. 5..
 */

public abstract class BasicViewHolder extends RecyclerView.ViewHolder {
    public BasicViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setView(TextMessage textMessage);
}
