package com.example.rohit.arishit_f.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rohit.arishit_f.group_create.group_create_main;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.login.chat.ChatPage;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.chat.GroupChat;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentOne extends Fragment {
    private static String sender_id = null;
    private static String sender_username = null;
    private ImageButton add_contact;
    private ImageButton create_group;
    public static String Username=null;
    private ImageButton contact_list;
    IMyService iMyService;
    private ListView listView;
    private ArrayList<Contacts> contact;
    private ArrayList<Contacts> h_contact = new ArrayList<Contacts>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Username = getArguments().getString("username");
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        contact_list = (ImageButton) view.findViewById(R.id.addmember);
        create_group = (ImageButton) view.findViewById(R.id.addGroup);
        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cg;
                cg = new Intent(getContext(), group_create_main.class);
                startActivity(cg);
            }
        });

        contact_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cl;
                cl = new Intent(getContext(), homepage_contact_list.class);
                cl.putExtra("username",Username);
                startActivity(cl);
            }
        });

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        listView = (ListView) view.findViewById(R.id.contactListView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contacts current_contact = contact.get(position);
                Intent intent;
                if(current_contact.get_is_group())
                {
                    intent = new Intent(getActivity(), GroupChat.class);
                    intent.putExtra("isGroup",String.valueOf(current_contact.get_is_group()));
                }
                else
                    intent = new Intent(getActivity(), ChatPage.class);

                intent.putExtra("id",current_contact.getmID());
                intent.putExtra("name",current_contact.getname());
                intent.putExtra("username",Username);

                //based on item add info to intent
                startActivity(intent);
            }
        });
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        fetch();
    }

    void fetch(){
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userDetails", getActivity().MODE_PRIVATE);
        String bearerToken = sharedPreferences.getString("bearer_token", "12345");
        String honey = sharedPreferences.getString("ishoney", "false");
        System.out.println("In Fragment One");
        if(honey.equals("yes")){
            System.out.println("Fragment ONE 1in Honey "+honey);
            Call<MessageResult> honeyUser = iMyService.getHoneyUsers();

            honeyUser.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                    System.out.println("Honey User Response");
                    System.out.println(response.body().getMessage());
                    try {
                        JSONArray jaHoneyUsers = new JSONArray(response.body().getMessage());
                        h_contact.clear();
                        for(int i=0;i<jaHoneyUsers.length();i++){
                            JSONObject hUser = jaHoneyUsers.getJSONObject(i);
                            System.out.println("User : "+hUser.getString("user_name"));
                            Contacts c = new Contacts(hUser.getString("user_name"),hUser.getString("user_name"), "Student","1" , false);
                            //Contacts c = new Contacts(name,Username,R.drawable.profilepic,isOnline);
                            System.out.println(c);
                            System.out.println("SDF "+c.getmUserName());
                            System.out.println("SDF "+c.getmID());
                            h_contact.add(c);
                        }
                        ContactsAdapter contactAdapter = new ContactsAdapter(getActivity(), h_contact);
                        listView.setAdapter(contactAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MessageResult> call, Throwable t) {
                    t.getStackTrace();
                    System.out.println("Honey User Failed"+t.toString());

                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), ChatPage.class);
                    Contacts current_contact = h_contact.get(position);
                    intent.putExtra("User", current_contact.getname());
                    intent.putExtra("UserName", current_contact.getmUserName());
                    System.out.println("User f_oone"+current_contact.getname());
                    System.out.println("Username f_oone"+current_contact.getmUserName());
                    //based on item add info to intent
                    startActivity(intent);
                }
            });
            //Toast.makeText(getActivity(), "We are in Honey", Toast.LENGTH_SHORT).show();
        }
        else
        {
            retrofit2.Call<MessageResult> call = iMyService.getMyDetails("Bearer " + bearerToken);
            call.enqueue(new Callback<MessageResult>() {
                @Override

                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                    JSONObject j1 = null;
                    try {
                        j1 = new JSONObject(response.body().getMessage());
                        sender_id = j1.getString("user_id");
                        sender_username = j1.getString("Username");
                        contact = new ArrayList<Contacts>();
                        Call<MessageResult> getUser = iMyService.getUserList(sender_id);
                        System.out.println("Sender_ID" + sender_id);
                        getUser.enqueue(new Callback<MessageResult>() {
                            @Override
                            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                                try {
                                    //contact.clear();
                                    JSONArray jj = new JSONArray(response.body().getMessage());
                                    System.out.println(jj);
                                    for (int j = 0; j < jj.length(); j++) {
                                        JSONObject j1 = jj.getJSONObject(j);
                                        System.out.println("j1" + j1);
//                                    String name = j1.getString("Name");
//                                    String Username = j1.getString("Username");
//                                    String Designation = j1.getString("Designation");
                                        String name = "";
                                        Log.d("Username..", sender_username);
                                        if (j1.has("Username")) {
                                            name = j1.getString("Username");
                                        } else if (j1.has("group_name")) {
                                            name = j1.getString("group_name");
                                        }
                                        String ID = "";
                                        boolean is_group_val = false;
                                        if (j1.has("user_id")) {
                                            ID = j1.getString("user_id");
                                        } else if (j1.has("group_id")) {
                                            ID = j1.getString("group_id");
                                            is_group_val = true;
                                        }

                                        String Designation = "";
                                        if (j1.has("Designation")) {
                                            Designation = j1.getString("Designation");
                                        }
                                        String url = null;
                                        if (j1.has("display_picture")) {
                                            JSONObject imageurl = new JSONObject(j1.getString("display_picture"));
                                            if (!imageurl.getString("url").equals("null")) {
                                                url = imageurl.getString("url");
                                                Log.v("url", url);
                                            }
                                        }
                                        if (!j1.has("group_name")) {
                                            if (!(j1.getString("Username").equals(sender_username)) && !j1.getString("user_id").equals(sender_id)) {
                                                Contacts c = new Contacts(name, ID, Designation, url, is_group_val);
                                                contact.add(c);
                                            }
                                        } else {
                                            Contacts c = new Contacts(name, ID, Designation, url, is_group_val);
                                            contact.add(c);
                                        }

                                    }
                                    ContactsAdapter contactAdapter = new ContactsAdapter(getActivity(), contact);
                                    listView.setAdapter(contactAdapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                            @Override
                            public void onFailure(Call<MessageResult> call, Throwable t) {
                                Toast.makeText(getContext(), "Error Chat", Toast.LENGTH_SHORT).show();
                                System.out.println(t);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MessageResult> call, Throwable t) {
                    Toast.makeText(getContext(), "Error Fetching Chat List", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    /*@Override
    public void onDestroy() {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        IMyService iMyService1 = retrofitClient.create(IMyService.class);
        System.out.println("USername in Fragment One"+Username);
        Call<MessageResult> call1 = iMyService1.loginSessionEnd(Username);
        call1.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                String respons=response.body().getResult();
                System.out.println("response ondestroy");
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                System.out.println("failure ondestroy");
                System.out.println("Throw"+t);

            }
        });
        super.onDestroy();
    }*/
}
