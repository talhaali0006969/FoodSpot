package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

public class SessionHelper {

    public static void setCurrentUser(Context context, com.example.myapplication.User user)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_user",new Gson().toJson(user)).apply();
        editor.apply();

    }
    @Nullable
    public static com.example.myapplication.User getCurrentUser(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
       String json = prefs.getString( "current_user","" );
        if(json.isEmpty()){
            return null;
        }
        else{
             com.example.myapplication.User currentUser = new Gson().fromJson(json, com.example.myapplication.User.class);
            return currentUser;
        }
    }
    public static  void  Logout(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().remove("current_user").apply();
    }
}
