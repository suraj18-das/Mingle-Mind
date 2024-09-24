package com.example.minglemind.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.minglemind.model.UserModel;

public class AndroidUtil {

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username",model.getUserName());
        intent.putExtra("phone",model.getPhone());
        intent.putExtra("userId",model.getUserId());
    }
    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel model=new UserModel();
        model.setUserName(intent.getStringExtra("username"));
        model.setPhone(intent.getStringExtra("phone"));
        model.setUserId(intent.getStringExtra("userId"));

        return model;
    }

    public static void setProfilePic(Context context, Uri imageuri, ImageView imageView){
        Glide.with(context).load(imageuri).apply(RequestOptions.circleCropTransform()).into(imageView);

    }

}
