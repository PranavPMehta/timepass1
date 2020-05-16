package com.example.rohit.arishit_f.vault;

import android.os.Bundle;
import android.util.Log;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.login.chat.Message;
import com.example.rohit.arishit_f.login.chat.MessageAdapter;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.example.rohit.arishit_f.security.SecurityCaller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class vault_chat extends AppCompatActivity {
    IMyService iMyService;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter vAdapter;
    private RecyclerView mMessagesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault_chat);

        vAdapter=new vMessageAdapter(mMessages, vault_chat.this);
        mMessagesView = (RecyclerView) findViewById(R.id.messages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mMessagesView.setAdapter(vAdapter);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

/*
        retrofit2.Call<MessageResult> call1 = iMyService.getChats();
        call1.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                Log.d("as", String.valueOf(response.body().getMessage().toCharArray().length));
                try {
                    JSONArray jj = new JSONArray(response.body().getMessage());
                    for (int j = 0; j < jj.length(); j++) {
                        JSONObject j1 = jj.getJSONObject(j);
                        //Self Message
                        System.out.println("j1" + j1);

                        System.out.println(j1.getString("user_id"));
                        if (j1.getString("user_id").equals("abc3")) {
                            JSONObject jj1 = j1.getJSONObject("msg");
                            System.out.println(jj1);
                            if (jj1.getString("type1").equals("MESSAGE")) {
                                Log.d("jj1", jj1.getString("data"));
                                List<Object> list = new ArrayList<>();
                                list.add(jj1.get("data"));
                                list.add(jj1.get("key_1"));
                                list.add(jj1.get("key_2"));
                                Log.d("Referecnce 1", j1.getString("reference_id"));
                                if (j1.getString("isVault").equals("true"))        //msg5 mean isReceived
                                {
                                    String decryptedMessage = SecurityCaller.doDecryption(list);
                                    String time;
                                    time=j1.getString("timestamp");
                                    //time.substring(4,21);
                                    ;
                                    Log.d("Index of T", String.valueOf(time.indexOf('T')));
                                    Log.d("Index of G",String.valueOf(time.indexOf('G')));
                                    Log.d("DETE",j1.getString("user_id")+j1.getString("timestamp"));
                                    addMessage(decryptedMessage,j1.getString("user_id"),time.substring(4,21), null, false);
                                }
                            }
                        } else if (j1.getString("user_id").equals("rks1")) {
                            //Receiver's Messages

                            System.out.println("j1" + j1);
                            JSONObject jj1 = j1.getJSONObject("msg");

                            System.out.println(jj1);
                            Log.d("jj1", jj1.getString("data"));
                            if (jj1.getString("type1").equals("MESSAGE")) {
                                List<Object> list = new ArrayList<>();
                                list.add(jj1.get("data"));
                                list.add(jj1.get("key_1"));
                                list.add(jj1.get("key_2"));
                                Log.d("Referecnce", j1.getString("reference_id"));
                                if (j1.getString("isVault").equals("true"))        //msg5 mean isReceived
                                {
                                    String decryptedMessage = SecurityCaller.doDecryption(list);
                                    String time;
                                    time=j1.getString("timestamp");
                                    Log.d("Index of T", String.valueOf(time.indexOf('T')));
                                    Log.d("Index of G",String.valueOf(time.indexOf('G')));
                                    Log.d("DETE",j1.getString("user_id")+j1.getString("timestamp"));
                                    addMessage(decryptedMessage,j1.getString("user_id"),time.substring(4,21), null, true);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });
*/
    }

    private void addMessage(String message, String username,String timestamp,String loc, boolean isSend) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .message(message).location(loc).username(username).timestamp(timestamp).isSend(false).build());
        // mAdapter = new MessageAdapter(mMessages);
        //mAdapter = new MessageAdapter( mMessages);
        vAdapter = new MessageAdapter(mMessages);
        vAdapter.notifyItemInserted(0);
        scrollToBottom();
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(vAdapter.getItemCount() - 1);
    }
}
