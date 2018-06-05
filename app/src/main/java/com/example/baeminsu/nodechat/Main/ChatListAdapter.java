package com.example.baeminsu.nodechat.Main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baeminsu.nodechat.Chat.Chat;
import com.example.baeminsu.nodechat.Model.ChatRoom;
import com.example.baeminsu.nodechat.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by baeminsu on 2018. 5. 29..
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private Context context;
    private List<ChatRoom> list;


    ChatListAdapter(Context context, List<ChatRoom> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_chatroom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("chatid", list.get(position).getChatId());
                intent.putExtra("chatName", list.get(position).getChatName());
                context.startActivity(intent);

            }
        });
        holder.setView(list.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView chatTitle;
        TextView lastMessage;
        TextView unReadCount;
        TextView lastMessageDate;


        ViewHolder(View itemView) {
            super(itemView);

            chatTitle = itemView.findViewById(R.id.item_chatroom_name);
            lastMessageDate = itemView.findViewById(R.id.item_chatroom_date);
            lastMessage = itemView.findViewById(R.id.item_chatroom_last_message);
            unReadCount = itemView.findViewById(R.id.item_chatroom_unread_count);
        }

        void setView(ChatRoom chatRoom) {
            chatTitle.setText(chatRoom.getChatName());
            lastMessage.setText(chatRoom.getLastMessage());

            if (chatRoom.getUnReadCount() > 0) {
                unReadCount.setVisibility(View.VISIBLE);
                unReadCount.setText(chatRoom.getUnReadCount() + "");
            } else {
                unReadCount.setVisibility(View.INVISIBLE);
            }

            if (chatRoom.getLastDate() != null) {

                Date nowDate = new Date();
                Date lastDate = chatRoom.getLastDate();

                long diffMillSec = nowDate.getTime() - lastDate.getTime();
                long diffDays = diffMillSec / (24 * 60 * 60 * 1000);

                if (diffDays >= 365) {
                    lastMessageDate.setText(new SimpleDateFormat("yyyy년 MM월 dd일").format(lastDate));
                } else if (diffDays >= 5) {
                    lastMessageDate.setText(new SimpleDateFormat("MM월 dd일").format(lastDate));
                } else if (diffDays < 1) {
                    lastMessageDate.setText(new SimpleDateFormat("HH:mm").format(lastDate));
                } else if (diffDays == 1) {
                    lastMessageDate.setText("어제");
                } else {
                    lastMessageDate.setText(new SimpleDateFormat("MM월 dd일").format(lastDate));
                }
            }


        }
    }
}
