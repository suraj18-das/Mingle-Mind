package com.example.minglemind.adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minglemind.ChatActivity;
import com.example.minglemind.R;
import com.example.minglemind.Utils.AndroidUtil;
import com.example.minglemind.Utils.FirebaseUtil;
import com.example.minglemind.databinding.ProfilePicViewBinding;
import com.example.minglemind.databinding.RecentsChatsRecyclerRowBinding;
import com.example.minglemind.model.ChatRoomModel;
import com.example.minglemind.model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel,RecentChatRecyclerAdapter.ChatRoomModelViewHolder> {
    Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options,Context context) {
        super(options);
        this.context=context;
    }

    @NonNull
    @Override
    public RecentChatRecyclerAdapter.ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root=LayoutInflater.from(context).inflate(R.layout.recents_chats_recycler_row,parent,false);

        return new ChatRoomModelViewHolder(root);
    }
    @Override
    protected void onBindViewHolder(@NonNull RecentChatRecyclerAdapter.ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model) {
        Log.w("RecentChatAdapter", "HEllo");

        FirebaseUtil.getOtherUserFromChatroom(model.getUserIds())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            UserModel otherUserModel=task.getResult().toObject(UserModel.class);
                            if (otherUserModel!=null){
                                holder.bind(model,otherUserModel);

                                notifyDataSetChanged();
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(context, ChatActivity.class);
                                        AndroidUtil.passUserModelAsIntent(intent,otherUserModel);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                });

                            }
                            else {
                                Log.w("RecentChatAdapter", "Other user not found for chatroom");
                            }
                        }else {
                            Log.w("RecentChatAdapter", "Error fetching other user", task.getException());
                        }
                    }
                });
    }



    public static class ChatRoomModelViewHolder extends RecyclerView.ViewHolder {
        ImageView profilPic;
       private RecentsChatsRecyclerRowBinding binding;
       private ProfilePicViewBinding profilePicViewBinding;
        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
           binding=RecentsChatsRecyclerRowBinding.bind(itemView);
           profilePicViewBinding=ProfilePicViewBinding.bind(itemView);
           profilPic=itemView.findViewById(R.id.profile_pic_image);
        }
        public void bind(ChatRoomModel chatRoomModel, UserModel otherUserModel) {
            binding.recyleUsaername.setText(otherUserModel.getUserName());
            boolean lastMessageSentByMe=chatRoomModel.getLastMessegeSenderId().equals(FirebaseUtil.currentUserId());
            if (lastMessageSentByMe){
                binding.lastMessageText.setText("You: "+chatRoomModel.getLastMessage());

            }else {
                binding.lastMessageText.setText(chatRoomModel.getLastMessage());
            }
            binding.lastMessageTime.setText(FirebaseUtil.timestampToString(chatRoomModel.getLastMessegeTimestamp()));
            FirebaseUtil.getOtherProfilePicStorage(otherUserModel.getUserId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> t) {
                    if (t.isSuccessful()){
                        Uri uri=t.getResult();
                        AndroidUtil.setProfilePic(itemView.getContext(), uri,profilePicViewBinding.profilePicImage);
                    }
                }
            });

        }
    }
}
