package com.example.minglemind.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minglemind.ChatActivity;
import com.example.minglemind.R;
import com.example.minglemind.Utils.AndroidUtil;
import com.example.minglemind.Utils.FirebaseUtil;
import com.example.minglemind.model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SearchUser_RecylerAdapter extends FirestoreRecyclerAdapter<UserModel,SearchUser_RecylerAdapter.UsereModelViewholder> {
Context context;

    public SearchUser_RecylerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchUser_RecylerAdapter.UsereModelViewholder holder, int position, @NonNull UserModel model) {
        holder.userNameText.setText(model.getUserName());
        holder.phoneText.setText(model.getPhone());
        if (model.getUserId().equals(FirebaseUtil.currentUserId())){
            holder.userNameText.setText(model.getUserName()+" (Me)");

        }
        FirebaseUtil.getOtherProfilePicStorage(model.getUserId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> t) {
                if (t.isSuccessful()){
                    Uri uri=t.getResult();
                    AndroidUtil.setProfilePic(context, uri,holder.profilePic);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                AndroidUtil.passUserModelAsIntent(intent,model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @NonNull
    @Override
    public SearchUser_RecylerAdapter.UsereModelViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root=LayoutInflater.from(context).inflate(R.layout.search_user_item,parent,false);

        return new UsereModelViewholder(root);
    }

    public class UsereModelViewholder extends RecyclerView.ViewHolder {
        TextView userNameText,phoneText;
        ImageView profilePic;
        public UsereModelViewholder(@NonNull View itemView) {
            super(itemView);
            userNameText=itemView.findViewById(R.id.recyle_usaername);
            phoneText=itemView.findViewById(R.id.recycle_userPhone);
            profilePic=itemView.findViewById(R.id.profile_pic_image);
        }
    }
}
