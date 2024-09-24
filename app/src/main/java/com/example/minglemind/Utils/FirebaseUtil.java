package com.example.minglemind.Utils;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static  String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static boolean isLoggedIn(){
        if (currentUserId()!=null){
            return true;
        }
        return false;
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatRoomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatRoomReference(chatroomId).collection("chats");
    }
    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static String getChatroomId(String user1,String user2){
        if (user1.hashCode()<user2.hashCode()){
            return user1+"_"+user2;
        }else {
            return user2+"_"+user1;

        }
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds){
        if (userIds.get(0).equals(FirebaseUtil.currentUserId())){
            return allUserCollectionReference().document(userIds.get(1));
        }else {
            return allUserCollectionReference().document(userIds.get(0));

        }
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM:SS").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfilePicStorage(){
        String currentUserId = FirebaseUtil.currentUserId();
        if (currentUserId != null && !currentUserId.isEmpty()) {
            return FirebaseStorage.getInstance().getReference().child("profile_pics").child(currentUserId);
        } else {
            // Handle the case where currentUserId is null or empty (e.g., log a warning)
            return null;
        }
    }

    public static StorageReference getOtherProfilePicStorage(String otherUserId){
        if (otherUserId != null && !otherUserId.isEmpty()) {
            return FirebaseStorage.getInstance().getReference().child("profile_pics").child(otherUserId);
        } else {
            // Handle the case where currentUserId is null or empty (e.g., log a warning)
            return null;
        }
    }
}
