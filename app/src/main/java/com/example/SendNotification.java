package com.example;

import android.content.Context;
import android.media.MediaCodec;

public class SendNotification {
    private final String userFcmToken;
    private final String title;
    private final String body;
    private final Context context;

    private final String postUrl="https://fcm.googleapis.com/v1/projects/mingle-mind/message:send";
    public SendNotification(String userFcmToken,String title,String body,Context context){
        this.userFcmToken=userFcmToken;
        this.title=title;
        this.body=body;
        this.context=context;
    }
    public void SendNotifications(){
//        MediaCodec.QueueRequest requestQueue=
    }
}
