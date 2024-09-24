package com.example.minglemind;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minglemind.Utils.AndroidUtil;
import com.example.minglemind.Utils.FirebaseUtil;
import com.example.minglemind.adapter.ChatRecyclerAdapter;
import com.example.minglemind.adapter.SearchUser_RecylerAdapter;
import com.example.minglemind.model.ChatMessageModel;
import com.example.minglemind.model.ChatRoomModel;
import com.example.minglemind.model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    UserModel otherUser;
    EditText messegeInput;
    ImageButton sendMessegeBtn,backBtn;
    TextView otherUserName;
    RecyclerView recyclerView;
    ChatRecyclerAdapter adapter;
    ImageView profilePicImage;

    String chatroomId;
    ChatRoomModel chatRoomModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        otherUser= AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId= FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),otherUser.getUserId());

        FirebaseUtil.getOtherProfilePicStorage(otherUser.getUserId()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> t) {
                if (t.isSuccessful()){
                    Uri uri=t.getResult();
                    AndroidUtil.setProfilePic(getApplicationContext(), uri,profilePicImage);
                }
            }
        });

        profilePicImage=findViewById(R.id.profile_pic_image);
        messegeInput=findViewById(R.id.messege_et);
        backBtn=findViewById(R.id.back_btn);
        sendMessegeBtn=findViewById(R.id.messege_sent_btn);
        otherUserName=findViewById(R.id.Other_Username);
        recyclerView=findViewById(R.id.chat_recycler_view);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        otherUserName.setText(otherUser.getUserName());

        sendMessegeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                String messege=messegeInput.getText().toString().trim();
                if (!messege.isEmpty()){
                    sendMessegeToUser(messege);
                }else {
                    System.out.println(messege);
                }
            }
        });

        getOrCreateChatroomModel();
        setUpChatRecyclerView();
        

    }

    private void setUpChatRecyclerView() {
        if (adapter!=null){
            adapter.stopListening();
        }

        Query query= FirebaseUtil.getChatroomMessageReference(chatroomId).orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessageModel> options=new FirestoreRecyclerOptions.Builder<ChatMessageModel>().setQuery(query, ChatMessageModel.class).build();
        adapter=new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult().isEmpty()){
                    runOnUiThread(()-> adapter.notifyDataSetChanged());
                }
            }
        });
    }

    private void sendMessegeToUser(String messege) {
        chatRoomModel.setLastMessegeTimestamp(Timestamp.now());
        chatRoomModel.setLastMessegeSenderId(FirebaseUtil.currentUserId());
        chatRoomModel.setLastMessage(messege);
        FirebaseUtil.getChatRoomReference(chatroomId).set(chatRoomModel);


        ChatMessageModel chatMessageModel=new ChatMessageModel(messege,FirebaseUtil.currentUserId(),Timestamp.now());
        System.out.println(chatMessageModel.getMessage()+"\n"+chatMessageModel.getSenderId());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    messegeInput.setText("");
                }
            }
        });
    }

    private void getOrCreateChatroomModel() {
        FirebaseUtil.getChatRoomReference(chatroomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    chatRoomModel=task.getResult().toObject(ChatRoomModel.class);
                    if (chatRoomModel==null){
//                            first time chatting
                        chatRoomModel=new ChatRoomModel(chatroomId,"","", Arrays.asList(FirebaseUtil.currentUserId(),otherUser.getUserId()), Timestamp.now());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FirebaseUtil.getChatRoomReference(chatroomId).set(chatRoomModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ChatActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                }
            }
        });
    }
}