package com.example.minglemind.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel {
    String chatroomId,lastMessegeSenderId,lastMessage;
    List<String> userIds;
    Timestamp lastMessegeTimestamp;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatroomId, String lastMessegeSenderId,String lastMessage, List<String> userIds, Timestamp lastMessegeTimestamp) {
        this.chatroomId = chatroomId;
        this.lastMessegeSenderId = lastMessegeSenderId;
        this.userIds = userIds;
        this.lastMessegeTimestamp = lastMessegeTimestamp;
        this.lastMessage=lastMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getLastMessegeSenderId() {
        return lastMessegeSenderId;
    }

    public void setLastMessegeSenderId(String lastMessegeSenderId) {
        this.lastMessegeSenderId = lastMessegeSenderId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessegeTimestamp() {
        return lastMessegeTimestamp;
    }

    public void setLastMessegeTimestamp(Timestamp lastMessegeTimestamp) {
        this.lastMessegeTimestamp = lastMessegeTimestamp;
    }
}
