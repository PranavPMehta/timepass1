package com.example.rohit.arishit_f;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.prefs.AbstractPreferences;

import io.socket.client.Socket;

public class MyApplicationContext extends Application implements Application.ActivityLifecycleCallbacks{
    private Context context;
    private static JSONArray chat_sockets = new JSONArray();
    private static JSONArray group_chat_sockets = new JSONArray();

    public static void setChatSocket(JSONObject user){
        chat_sockets.put(user);
        System.out.println("Call setChatSocket:"+chat_sockets);
    }

    public static void setGroupChatSocket(JSONObject user){
        group_chat_sockets.put(user);
        System.out.println("Call setChatSocket:"+chat_sockets);
    }
    public static Socket getChatSocket(String user_id, String chat_id1, String chat_id2) throws JSONException {
        System.out.println("Call getChatSocket:"+chat_sockets);
        for(int i=0;i<chat_sockets.length();i++){
            JSONObject user = chat_sockets.getJSONObject(i);
            System.out.println("User_id:"+user.getString("user_id").equals(user_id));
            System.out.println("Chat_id1:"+user.getString("chat_id").equals(chat_id1));
            System.out.println("Chat_id2:"+user.getString("chat_id").equals(chat_id2));
            System.out.println("Second half:"+(user.getString("chat_id").equals(chat_id1) || user.getString("chat_id").equals(chat_id2)));
            System.out.println("If Result:"+((user.getString("chat_id").equals(chat_id1) || user.getString("chat_id").equals(chat_id2)) && user.getString("user_id").equals(user_id)));
            if((user.getString("chat_id").equals(chat_id1) || user.getString("chat_id").equals(chat_id2)) && user.getString("user_id").equals(user_id)) {
                System.out.println("Found:"+user.get("socket")+"Chat_id"+user.getString("chat_id"));
                return (Socket) user.get("socket");
            }
        }
        System.out.println("Not Found");
        return null;
    }
    public static Socket getGroupChatSocket(String user_id, String group_id) throws JSONException {
        System.out.println("Call getChatSocket:"+chat_sockets);
        for(int i=0;i<chat_sockets.length();i++){
            JSONObject user = group_chat_sockets.getJSONObject(i);

            if(user.getString("group_id").equals(group_id) && user.getString("user_id").equals(user_id))
                return (Socket) user.get("socket");

        }
        System.out.println("Not Found");
        return null;
    }

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //setupActivityListener();
    }

    //TODO Remove ScreenShot
    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {

            }
            @Override
            public void onActivityPaused(Activity activity) {

            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
                chat_sockets = null;
                System.out.println("My Aplication onActivityDEstroy1");
            }
        });
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        System.out.println("My Aplication onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        System.out.println("My Aplication onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        System.out.println("My Aplication onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        System.out.println("My Aplication onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        System.out.println("My Aplication onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        System.out.println("My Aplication onActivityDEstroy2");
    }
}