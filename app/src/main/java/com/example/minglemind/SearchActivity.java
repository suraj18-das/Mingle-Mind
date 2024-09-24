package com.example.minglemind;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minglemind.Utils.FirebaseUtil;
import com.example.minglemind.adapter.SearchUser_RecylerAdapter;
import com.example.minglemind.model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchActivity extends AppCompatActivity {
    EditText searchInput;
    ImageButton searchBtn,backBtn;
    RecyclerView recyclerView;

    SearchUser_RecylerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchInput=findViewById(R.id.searchUser_name_input);
        searchBtn=findViewById(R.id.searcg_btn_user);
        backBtn=findViewById(R.id.back_btn);
        recyclerView=findViewById(R.id.search_user_recyclerView);

        searchInput.requestFocus();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm=searchInput.getText().toString();
                if (searchTerm.isEmpty() || searchTerm.length()<3){
                    searchInput.setError("Invalid Username");
                }
                else {
                    setUpSearchRecyclerView(searchTerm);
                }
            }
        });


    }

    private void setUpSearchRecyclerView(String searchTerm) {
        if (adapter!=null){
            adapter.stopListening();
        }

        Query query= FirebaseUtil.allUserCollectionReference().whereGreaterThanOrEqualTo("userName",searchTerm);
        Log.d("SearchActivity", "Search term: " + searchTerm);
        Log.d("SearchActivity", "Query: " + query.toString());
        FirestoreRecyclerOptions <UserModel> options=new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();
        adapter=new SearchUser_RecylerAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult().isEmpty()){
                    runOnUiThread(()-> adapter.notifyDataSetChanged());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.stopListening();
        }
    }
}