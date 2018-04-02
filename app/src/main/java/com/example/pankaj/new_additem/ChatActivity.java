package com.example.pankaj.new_additem;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ImageButton send;
    private DatabaseReference databaseMessage;
    private FirebaseAuth mAuth;
    private EditText input;
    private String current_userid;
    private String current_user_name;
    private String temp_name;
    private String friend_user_id;
    private FirebaseRecyclerAdapter<Message,MessageHolder> mPeopleRVAdapter;
    private RecyclerView recycleMessageList;
    private LinearLayoutManager linearLayoutManager;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        send=findViewById(R.id.chat_sendButton);

        current_userid=FirebaseAuth.getInstance().getUid();
        friend_user_id=getIntent().getStringExtra("user_id").toString();
        final String frient_user_name=getIntent().getStringExtra("user_name").toString();

        recycleMessageList = findViewById(R.id.list_message);
        recycleMessageList.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
       // linearLayoutManager.setReverseLayout(true);
       // linearLayoutManager.setStackFromEnd(true);
        recycleMessageList.setLayoutManager(linearLayoutManager);
        //linearLayoutManager.smoothScrollToPosition(recycleMessageList, null, mPeopleRVAdapter.getItemCount());


        //recycleMessageList.scrollToPosition(mPeopleRVAdapter.getItemCount()-1);

        getCurrent_user_name(current_userid);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input=findViewById(R.id.input);
                String mssg=input.getText().toString();
                if(!TextUtils.isEmpty(mssg)) {
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    current_user_name = temp_name;

                    databaseMessage = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference user_mssg_push = FirebaseDatabase.getInstance().getReference().child("messages")
                            .child(current_userid).child(friend_user_id).push();

                    Map messageMap = new HashMap();
                    messageMap.put("messageUser","you");
                    messageMap.put("messageTime", currentDateTimeString);
                    messageMap.put("message", mssg);
                    Map messageMap1 = new HashMap();
                    messageMap1.put("messageUser", current_user_name);
                    messageMap1.put("messageTime", currentDateTimeString);
                    messageMap1.put("message", mssg);
                    Map messageUserMap = new HashMap();
                    String push_id = user_mssg_push.getKey();
                    messageUserMap.put("messages/" + current_userid + "/" + friend_user_id + "/" + push_id, messageMap);
                    messageUserMap.put("messages/" + friend_user_id + "/" + current_userid + "/" + push_id, messageMap1);
                    databaseMessage.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError!=null)
                            {
                                Log.d("Chat_log",databaseError.getMessage().toString());
                            }
                        }
                    });
                    input.setText("");
                }
                else
                {
                    Toast.makeText(ChatActivity.this,"write something else",Toast.LENGTH_LONG).show();
                }
                //display_chatBox();
            }
        });

    }

    @Override
    public void onStart() {

        //if(mPeopleRVAdapter.getItemCount()>=2) {
          //  recycleMessageList.scrollToPosition(mPeopleRVAdapter.getItemCount() - 1);
        //}
        super.onStart();
        attachRecyclerViewAdapter();
        // RecyclerView listView = findViewById(R.id.list_message);

    }

    private void attachRecyclerViewAdapter() {
        final FirebaseRecyclerAdapter<Message, MessageHolder> adapter = newAdapter();

        // Scroll to bottom on new messages
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                recycleMessageList.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        recycleMessageList.setAdapter(adapter);
        adapter.startListening();
        //adapter.startListening();
        //mPeopleRVAdapter.startListening();
        //mRecyclerView.setAdapter(adapter);
    }

    void getCurrent_user_name(String user_id)
    {
        DatabaseReference databaseReferenceUser;
        databaseReferenceUser=FirebaseDatabase.getInstance().getReference().child("students").child(user_id);
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String u_name =dataSnapshot.child("name").getValue().toString();
                temp_name=u_name;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class MessageHolder extends RecyclerView.ViewHolder{

        View mview;
        public MessageHolder(View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setUserName(String name)
        {
            // System.out.println("Size");
            TextView username;
            username = (TextView) mview.findViewById(R.id.message_user);
            username.setText(name);
        }
        public void setTime(String time)
        {
            //System.out.println("Cost");
            TextView messageTime=(TextView) mview.findViewById(R.id.message_time);
            messageTime.setText(time);
        }
        public void setMessage(String message)
        {
            //System.out.println("Caution");
            TextView messageText=(TextView) mview.findViewById(R.id.message_text);
            messageText.setText(message);
        }
    }

    protected FirebaseRecyclerAdapter<Message, MessageHolder> newAdapter(){


        DatabaseReference Messagedatabase = FirebaseDatabase.getInstance().getReference().child("messages")
                .child(current_userid).child(friend_user_id);
        query = Messagedatabase.limitToLast(50);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .setLifecycleOwner(this)
                        .build();
        return new FirebaseRecyclerAdapter<Message, MessageHolder>(options) {

            @Override
            public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.look_of_mssg, parent, false);

                return new MessageHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {

                holder.setMessage(model.getMessage());
                holder.setTime(model.getMessageTime());
                holder.setUserName(model.getMessageUser());
            }

        };
       // recycleMessageList.setAdapter(mPeopleRVAdapter);
        //linearLayoutManager.smoothScrollToPosition(recycleMessageList, null, mPeopleRVAdapter.getItemCount());
        //recycleMessageList.scrollToPosition(mPeopleRVAdapter.getItemCount()-1);
        //mPeopleRVAdapter.startListening();
    }
}
