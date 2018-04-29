package com.example.pankaj.new_additem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pankaj on 16/4/18.
 */

class MessageNotificationRecyclerviewAdapter extends RecyclerView.Adapter<MessageNotificationRecyclerviewAdapter.ViewHolder> {

    List<NotificationMessage> notificationMessageList;
    Context context;
    public MessageNotificationRecyclerviewAdapter(Context applicationContext, List<NotificationMessage> messagelist) {

        this.context=applicationContext;
        this.notificationMessageList=messagelist;
    }

    @NonNull
    @Override
    public MessageNotificationRecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_view,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.fromName.setText("From "+notificationMessageList.get(position).getFromName());
        holder.Message.setText(notificationMessageList.get(position).getMessage());
    }


    @Override
    public int getItemCount() {
        return notificationMessageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mview;
        public TextView fromName,Message;
        public ViewHolder(View itemView) {
            super(itemView);

            mview = itemView;
            fromName=itemView.findViewById(R.id.view_fromuser);
            Message=itemView.findViewById(R.id.message_text);
        }
    }
}
