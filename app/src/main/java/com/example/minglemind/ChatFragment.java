package com.example.minglemind;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.minglemind.Utils.FirebaseUtil;
import com.example.minglemind.adapter.RecentChatRecyclerAdapter;
import com.example.minglemind.adapter.SearchUser_RecylerAdapter;
import com.example.minglemind.model.ChatRoomModel;
import com.example.minglemind.model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatFragment extends Fragment {
    RecyclerView recentChats;
    RecentChatRecyclerAdapter adapter;
    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        recentChats=view.findViewById(R.id.recycler_chats_recent);
        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {
        if (adapter!=null){
            adapter.stopListening();
        }


        Query query= FirebaseUtil.allChatroomCollectionReference()
                .whereArrayContains("userIds",FirebaseUtil.currentUserId())
                .orderBy("lastMessegeTimestamp",Query.Direction.DESCENDING);
        Log.d("SearchActivity", "Query: " + query.toString());

        FirestoreRecyclerOptions<ChatRoomModel> options=new FirestoreRecyclerOptions.Builder<ChatRoomModel>().setQuery(query, ChatRoomModel.class).build();
        adapter=new RecentChatRecyclerAdapter(options,getContext());
        recentChats.setLayoutManager(new LinearLayoutManager(getContext()));
        recentChats.setAdapter(adapter);
        adapter.startListening();

        Log.d("ChatFragment", "recentChats is null: " + (recentChats == null));
        Log.d("ChatFragment", "Adapter item count: " + adapter.getItemCount());
        Log.d("ChatFragment",FirebaseUtil.currentUserId());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Firestore", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w("Firestore", "Error getting documents.", task.getException());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.startListening();
        }
    }
}