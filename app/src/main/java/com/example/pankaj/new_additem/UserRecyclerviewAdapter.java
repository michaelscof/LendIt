package com.example.pankaj.new_additem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pankaj on 24/3/18.
 */

public class UserRecyclerviewAdapter extends RecyclerView.Adapter<UserRecyclerviewAdapter.ViewHolder>{

    List<User> userList;
    Context context;

    public UserRecyclerviewAdapter(Context context,List<User> userList) {

        this.userList = userList;
        this.context=context;
    }


    @Override
    public UserRecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserRecyclerviewAdapter.ViewHolder holder, int position) {

        holder.UserName.setText(userList.get(position).getName());
        holder.Status.setText(userList.get(position).getStatus());
        CircleImageView user_pics=holder.Userpic;
        Glide.with(context).load(userList.get(position).getImage()).into(user_pics);
        final String user_id=userList.get(position).getUser_id();
        final String NameforSend=userList.get(position).getName();
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("user_id",user_id);
                intent.putExtra("user_name",NameforSend);
                context.startActivity(intent);
            }
        });
        holder.Userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,ProfileActivity.class);
                intent.putExtra("user_id",user_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        private View mview;
        private CircleImageView Userpic;
        private TextView UserName;
        private TextView Status;

        public ViewHolder(View itemView) {
            super(itemView);

            mview=itemView;

            UserName=mview.findViewById(R.id.view_username);
            Userpic=mview.findViewById(R.id.view_Userimage);
            Status=mview.findViewById(R.id.statusText);
        }
    }
}
