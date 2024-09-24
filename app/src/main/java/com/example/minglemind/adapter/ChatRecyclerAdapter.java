package com.example.minglemind.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minglemind.ChatActivity;
import com.example.minglemind.R;
import com.example.minglemind.Utils.AndroidUtil;
import com.example.minglemind.Utils.FirebaseUtil;
import com.example.minglemind.model.ChatMessageModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel,ChatRecyclerAdapter.ChatModelViewHolder> {
    Context context;

    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRecyclerAdapter.ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
       if (model.getSenderId().equals(FirebaseUtil.currentUserId())){
           holder.leftchatlayout.setVisibility(View.GONE);
           holder.rightchatLayout.setVisibility(View.VISIBLE);
           holder.rightChatText.setText(model.getMessage());
       }else {
           holder.rightchatLayout.setVisibility(View.GONE);
           holder.leftchatlayout.setVisibility(View.VISIBLE);
           holder.leftChatText.setText(model.getMessage());
       }
    }


    @NonNull
    @Override
    public ChatRecyclerAdapter.ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root=LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row,parent,false);

        return new ChatModelViewHolder(root);
    }

    public class ChatModelViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftchatlayout,rightchatLayout;
        TextView leftChatText,rightChatText;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);
            leftchatlayout=itemView.findViewById(R.id.left_chat_layout);
            rightchatLayout=itemView.findViewById(R.id.right_chat_layout);
            leftChatText=itemView.findViewById(R.id.left_chat_text);
            rightChatText=itemView.findViewById(R.id.right_chat_text);
           
        }
    }
}
