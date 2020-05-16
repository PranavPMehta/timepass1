package com.example.rohit.arishit_f.login.chat;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.constants.Constants;
import com.example.rohit.arishit_f.faq.faq_main;
import com.example.rohit.arishit_f.login.LoginScreen;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.presentation.BuildConfig;
import com.example.rohit.arishit_f.profileInfo.profile_info;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.example.rohit.arishit_f.security.SecurityCaller;
import com.example.rohit.arishit_f.videoCommunication.CallingScreenActivity;
import com.example.rohit.arishit_f.voiceCall.BaseActivity;
import com.example.rohit.arishit_f.voiceCall.CallScreenActivity;
import com.example.rohit.arishit_f.voiceCall.SinchService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.calling.Call;
/*
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.example.rohit.arishit_f.MyApplicationContext;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class ChatPage extends BaseActivity {
    private static final int SIGNUP_REQUEST = 1;
    public static boolean isInActionMode = false;
    public static boolean check_online = false, msg_sent, msg_received, got_resp = false;
    public static String locationStateCountry = null;
    public static String Username = null;
    public static String sender_id = "";
    private static boolean activityOpen=false;
    public DatabaseAdapter myDb;
    Toolbar toolbar;
    Geocoder geocoder;
    List<Address> addresses;
    String format;
    IMyService iMyService;
    String reference_id, mesage;
    int flag = -1;
    JSONObject sendText = new JSONObject();
    Menu menu;
    private String st_path = Environment.getExternalStorageDirectory().getAbsolutePath();
    private TextView userText, messageText;
    private ImageView sendButton, callButton, videoCallView;
    private CircleImageView profileimg;
    private EditText mInputMessageView;
    private RecyclerView mMessagesView;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private Socket socket;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private String receiver_id = null;
    private ImageView profileImage;
    private String userid;
    private TextView username;
    private ProgressBar progressBar;

    //Variables related to Long press message options
    private boolean isForward;
    final Context context = this;
    private int selectedMessage;
    private int selectedMessageArrayPosition;
    private View SelectedView;
    private String logged_in_user,current_chat_user;
    private PopupMenu popup;
    private Boolean isBlocked=Boolean.FALSE;
    private Boolean moveToVault =Boolean.FALSE;
    private Boolean isConfidential = Boolean.FALSE;
    private Boolean toCopy;
    private Boolean isForwardAllowed;
    SharedPreferences.Editor editor;

    //Chatdots variables
    private ImageView chatDots;
    public List<Message> mAllMessages = new ArrayList<Message>();




    private Emitter.Listener handleIncomingMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        String message, loc;
                        String imageText;
                        JSONObject msg;

                        //TODO verify ref_id again
                        String ref_id = "1234";
                        try {
                            message = data.getString("message").toString();
                            msg = new JSONObject(message);
                            System.out.println("JSONOBJECT" + msg);
                            ref_id = msg.getString("id");
                            System.out.println("SOcket ref_id : "+ref_id);
                            if (msg.getString("data_type").equals("MESSAGE")) {
                                loc = msg.getString("location");
                                List<Object> list = new ArrayList<>();
                                list.add(msg.get("msg1"));
                                list.add(msg.get("msg2"));
                                list.add(msg.get("msg3"));
                                System.out.println("Activty Open"+activityOpen);

                                if(activityOpen)
                                {
                                    retrofit2.Call<MessageResult> call1 = iMyService.setReceived(msg.getString("id")); //idha kya likhna hey dekh le
                                    call1.enqueue(new Callback<MessageResult>() {
                                        @Override
                                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                          //  Toast.makeText(ChatPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                            Toast.makeText(ChatPage.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                String timestamp=msg.getString("time");
                                timestamp=timestamp.substring(11,19);
                                myDb.addMessage(msg.getString("msg1"), msg.getString("msg2"), msg.getString("msg3"));
                                message = SecurityCaller.doDecryption(list);
                                addMessage(message, loc, false,ref_id,msg.getString("data_type"),timestamp,false);
                            } else if (msg.getString("data_type").equals("FILES") || msg.getString("data_type").equals("VIDEO") || msg.getString("data_type").equals("IMAGE")) {
                                //byte[] encoded = msg.getString("msg1").getBytes();
                                //messagetext = (TextView) findViewById(R.id.message);
                                System.out.println("Activty Open"+activityOpen);
                                if(activityOpen)
                                {
                                    retrofit2.Call<MessageResult> call1 = iMyService.setReceived(msg.getString("id")); //idha kya likhna hey dekh le
                                    call1.enqueue(new Callback<MessageResult>() {
                                        @Override
                                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                            //Toast.makeText(ChatPage.this, "Media Sent", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                            Toast.makeText(ChatPage.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                String filename = msg.getString("msg2");
                                byte[] decodedBytes = Base64.decode(msg.getString("msg1"), Base64.DEFAULT);
                                System.out.println("st_path" + st_path);
                                System.out.println("FILENA" + filename);
                                //System.out.println("MESSAGETEX" + messageText.getText());
                                FileOutputStream fos = new FileOutputStream(st_path + "/" + filename);
                                System.out.println("FILELNAME" + st_path + "/" + filename);
                                fos.write(decodedBytes);
                                fos.flush();
                                fos.close();
                                String timestamp=msg.getString("time");
                                timestamp=timestamp.substring(11,19);
                                addMessage(filename, null, false,ref_id,msg.getString("data_type"),timestamp,false);
                            }
                        } catch (JSONException e) {
                            //return;
                        }
                    } catch (Exception e) {

                    }
                }
            });
        }
    };

    private static String encodeFileToBase64Binary(File fileName) throws IOException {
        byte[] bytes = Files.readAllBytes(fileName.toPath());
        byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);
        String encodedString = new String(encoded);
        return encodedString;
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DcumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return "";
    }*/

    public static String getMimeType(String url) {
        //This Function returns the type of file being uploaded to server
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    protected void onStop() {
        activityOpen=false;
        super.onStop();
    }

    @Override
    protected void onResume() {
        activityOpen=true;
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(ChatPage.this,"In Personal Chats",Toast.LENGTH_SHORT).show();
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        Intent intent=getIntent();
        Username=intent.getStringExtra("username");
        System.out.println("Username in Chat pAge"+Username);
        iMyService = retrofitClient.create(IMyService.class);
        retrofit2.Call<MessageResult> call1 = iMyService.checkLogin(Username);
        call1.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                String res=response.body().getResult();
                System.out.println("Response"+res);
                if(res.equals("false"))
                {
                    System.out.println("Not Logged in ChatPage");
                    Intent intent=new Intent(ChatPage.this, LoginScreen.class);
                    startActivity(intent);
                }
                System.out.println("Logged in ChatPage");
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                System.out.println("failure ondestroy");
                System.out.println("Throw"+t);

            }
        });

        myDb = new DatabaseAdapter(this);
        activityOpen = true;
        setContentView(R.layout.activity_chat_page);
        messageText = (TextView) findViewById(R.id.message);
        userText = (TextView) findViewById(R.id.userText);
        check_online = hasActiveInternetConnection();
        profileImage = (ImageView) findViewById(R.id.call1);
        progressBar = (ProgressBar) findViewById(R.id.profile_progress);
        username = (TextView) findViewById(R.id.userText);

        userid = getIntent().getStringExtra("id");
        System.out.println("User idd"+userid);
        SharedPreferences sharedPreferences1 = getSharedPreferences("userDetails", MODE_PRIVATE);
        String honey = sharedPreferences1.getString("ishoney", "n12");
        System.out.println("Honey Chatpage : "+honey);
        if(honey.equals("yes")){
            String user_name = userid = getIntent().getStringExtra("User");
            System.out.println("user honey "+user_name);
            username.setText(user_name);
            retrofit2.Call<MessageResult> honey_msgs = iMyService.getHoneyMsg(user_name);

            honey_msgs.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                    System.out.println("chatpage honey respmnse"+response.body().getMessage());

                    try {
                        JSONArray arr = new JSONArray(response.body().getMessage());
                        System.out.println("msgarr"+response.body().getMessage());
                        for(int i=0;i<arr.length();i++)
                        {
                            JSONObject honeyobj1 = arr.getJSONObject(i);
                            System.out.println("HoneyRespnseMsg"+honeyobj1.getString("msg"));
                            if(honeyobj1.getString("side").equals("left")) {
                                addMessage(honeyobj1.getString("msg"), honeyobj1.getString("location"), false, "abc", "MESSAGE", "14:09:02", false);
                            }
                            else{
                                addMessage(honeyobj1.getString("msg"), honeyobj1.getString("location"), true, "abc", "MESSAGE", "14:09:02", false);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //dummy msgs are displayed but sender canrt send a msg
                @Override
                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                    System.out.println("Failed to retrieve Honey Chats");

                }
            });
            mInputMessageView = (EditText)findViewById(R.id.message_input);
            sendButton = (ImageView) findViewById(R.id.send_button);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    sendMessage();
                    String message = mInputMessageView.getText().toString().trim();
                    mInputMessageView.setText("");
                    addMessage(message, locationStateCountry, true,"ref_id","MESSAGE","14:02:09",false);
                }
            });

            mMessagesView = (RecyclerView) findViewById(R.id.messages);
//            videoCallView = (ImageView) findViewById(R.id.videocam);
            mMessagesView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mAdapter = new MessageAdapter(mMessages, ChatPage.this);
            mMessagesView.setAdapter(mAdapter);

        }
        else{

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent pr = new Intent(ChatPage.this, profile_info_chat.class);
                    System.out.println("USer ID"+userid);
                    pr.putExtra("id", userid);
                    startActivity(pr);

                }
            });

            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent pr = new Intent(ChatPage.this, profile_info_chat.class);
                    pr.putExtra("id", userid);
                    startActivity(pr);

                }
            });


            if (check_online) {
                userText.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                userText.setTextColor(Color.RED);
            }
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            format = s.format(new Date());
            format = format.substring(8, 14);
            myDb.checkLastLogin(format);


            SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
            String bearerToken = sharedPreferences.getString("bearer_token", "12345");

            receiver_id = getIntent().getExtras().getString("id");
            retrofit2.Call<MessageResult> call = iMyService.getMyDetails("Bearer " + bearerToken);
            call.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                    System.out.println(response.body().getMessage());
                    JSONObject j1 = null;
                    try {
                        j1 = new JSONObject(response.body().getMessage());
                        sender_id = j1.getString("user_id");
                        JSONObject tobj = new JSONObject(j1.getString("display_picture"));
                        String turl = tobj.getString("url");
                        Log.v("turl:",turl);
                        if(!turl.equals("null")) {
                            progressBar.setVisibility(View.VISIBLE);
                            Glide.with(ChatPage.this)
                                    .load(IP_ADDRESS + ":3000" + turl)
                                    .apply(
                                            new RequestOptions()
                                                    .error(R.drawable.profile_img_new)
                                                    .centerCrop()
                                    )
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(profileImage);
                        }else{
                            profileImage.setImageResource(R.drawable.profile_img_new);
                            progressBar.setVisibility(View.INVISIBLE);

                        }

                        myDb.checkLastLogin(format);
                        System.out.println("Sender ID AFter:" + sender_id);
                        //Paste here
                        //From here
                        logged_in_user = sender_id;
                        current_chat_user = receiver_id;

                        retrofit2.Call<MessageResult> checkBlocked = iMyService.check_block_user(logged_in_user, current_chat_user);
                        System.out.println("CHeck Blocked user : 1 : " + logged_in_user + " 2 :" + current_chat_user);
                        checkBlocked.enqueue(new Callback<MessageResult>() {
                            @Override
                            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                String r = response.body().getMessage();
                                System.out.println("Printing response Check Blocked " + r);
                                if (r.equals("yes")) {
                        /*findViewById(R.id.MessageLayoutChatPage).setVisibility(View.INVISIBLE);
                        findViewById(R.id.BlockedMessagelayout).setVisibility(View.VISIBLE);
                        popup.getMenu().findItem(R.id.block_chat).setVisible(false);
                        popup.getMenu().findItem(R.id.ttl_chat).setVisible(false);
                        popup.getMenu().findItem(R.id.ssl_chat).setVisible(false);
                        popup.getMenu().findItem(R.id.search_chat).setVisible(false);
                        popup.getMenu().findItem(R.id.unblock_chat).setVisible(true);
                        callButton.setVisibility(View.INVISIBLE);
                        videoCallView.setVisibility(View.INVISIBLE);
                         */
                                    isBlocked = Boolean.TRUE;
                                    blockedUser();
                                } else {
                        /*findViewById(R.id.MessageLayoutChatPage).setVisibility(View.VISIBLE);
                        findViewById(R.id.BlockedMessagelayout).setVisibility(View.INVISIBLE);
                        popup.getMenu().findItem(R.id.block_chat).setVisible(true);
                        popup.getMenu().findItem(R.id.unblock_chat).setVisible(false);*/
                                    isBlocked = Boolean.FALSE;
                                    unblockedUser();
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

                            }
                        });
                        retrofit2.Call<MessageResult> call1 = iMyService.getChats(sender_id, receiver_id);
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
                                        String ref_id = j1.getString("id");
                                        String imp = j1.getString("is_important");
                                        Boolean isImportant;
                                        if (imp.equals("true")) {
                                            isImportant = Boolean.TRUE;
                                        } else {
                                            isImportant = Boolean.FALSE;
                                        }
                                        System.out.println("checkImportant" + isImportant);
                                        if (j1.getString("user_id").equals(receiver_id)) {
                                            JSONObject jj1 = j1.getJSONObject("msg");
                                            System.out.println("Receiver ID"+jj1);
                                            if (jj1.getString("data_type").equals("MESSAGE")) {
                                                Log.d("jj1", jj1.getString("data"));
                                                List<Object> list = new ArrayList<>();
                                                list.add(jj1.get("data"));
                                                list.add(jj1.get("key_1"));
                                                list.add(jj1.get("key_2"));
                                                String timestamp = j1.getString("timestamp");
                                                timestamp = timestamp.substring(16, 25);
                                                Log.d("Referecnce 1", j1.getString("id"));
                                                if (j1.getString("isReceived").equals("false"))        //msg5 mean isReceived
                                                {
                                                    retrofit2.Call<MessageResult> call1 = iMyService.setReceived(j1.getString("id"));
                                                    call1.enqueue(new Callback<MessageResult>() {
                                                        @Override
                                                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                                            //Toast.makeText(ChatPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                                            Toast.makeText(ChatPage.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                    //      below conditions to be executed and the message to be inserted in local database
                                                }

                                                msg_received = true;
                                                myDb.addMessage(jj1.getString("data"), jj1.getString("key_1"), jj1.getString("key_2"));
                                                String decryptedMessage = SecurityCaller.doDecryption(list);
                                                System.out.println("check" + check_online);
                                                addMessage(decryptedMessage, locationStateCountry, false, ref_id, jj1.getString("data_type"), timestamp, isImportant);
                                                System.out.println("check printed" + check_online);
                                            } else if (jj1.getString("data_type").equals("FILES") || jj1.getString("data_type").equals("VIDEO") || jj1.getString("data_type").equals("IMAGE")) {
                                                String data = jj1.getString("key_1");
                                                if (j1.getString("isReceived").equals("false"))        //msg5 mean isReceived
                                                {
                                                    //insetad of reference_id use msg_id later okk
                                                    retrofit2.Call<MessageResult> call1 = iMyService.setReceived(j1.getString("id"));
                                                    call1.enqueue(new Callback<MessageResult>() {
                                                        @Override
                                                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                                            //Toast.makeText(ChatPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                                            Toast.makeText(ChatPage.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    //      below conditions to be executed and the message to be inserted in local database
                                                }
                                                String timestamp = j1.getString("timestamp");
                                                timestamp = timestamp.substring(16, 25);
                                                addMessage(data, locationStateCountry, false, ref_id, jj1.getString("data_type"), timestamp, isImportant);
                                            }

                                        } else if (j1.getString("user_id").equals(sender_id)) {
                                            //Receiver's Messages

                                            System.out.println("j1" + j1);
                                            JSONObject jj1 = j1.getJSONObject("msg");

                                            System.out.println("Sender Id"+jj1);
                                            Log.d("jj1", jj1.getString("data"));
                                            if (jj1.getString("data_type").equals("MESSAGE")) {
                                                List<Object> list = new ArrayList<>();
                                                list.add(jj1.get("data"));
                                                list.add(jj1.get("key_1"));
                                                list.add(jj1.get("key_2"));
                                                /*Log.d("Referecnce", j1.getString("id"));
                                                if (j1.getString("isReceived").equals("false"))        //msg5 mean isReceived
                                                {
                                                    retrofit2.Call<MessageResult> call1 = iMyService.setReceived(j1.getString("id"));
                                                    call1.enqueue(new Callback<MessageResult>() {
                                                        @Override
                                                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                                            Toast.makeText(ChatPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                                            Toast.makeText(ChatPage.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                                msg_received = true;
                                                */
                                                String decryptedMessage = SecurityCaller.doDecryption(list);
                                                System.out.println("check" + check_online);
                                                String timestamp = j1.getString("timestamp");
                                                timestamp = timestamp.substring(16, 25);
                                                System.out.println("SEnder ID"+j1.getString("isReceived").equals("false"));
                                                if(j1.getString("isReceived").equals("true"))
                                                {
                                                    msg_received=true;
                                                    msg_sent=false;
                                                    addMessage(decryptedMessage, locationStateCountry, true, ref_id, jj1.getString("data_type"), timestamp, isImportant);
                                                }
                                                else if(j1.getString("isReceived").equals("false"))
                                                {
                                                    msg_received=false;
                                                    msg_sent=true;
                                                    addMessage(decryptedMessage, locationStateCountry, true, ref_id, jj1.getString("data_type"), timestamp, isImportant);
                                                }
                                                System.out.println("1msg_received" + msg_received);
                                                System.out.println("1msg_sent" + msg_sent);
                                                myDb.addMessage(jj1.getString("data"), jj1.getString("key_1"), jj1.getString("key_2"));
                                                System.out.println("check printed" + check_online);
                                            } else if (jj1.getString("data_type").equals("FILES") || jj1.getString("data_type").equals("VIDEO") || jj1.getString("data_type").equals("IMAGE")) {
                                                String data = jj1.getString("key_1");
                                                if (j1.getString("isReceived").equals("false"))        //msg5 mean isReceived
                                                {
                                                    //insetad of reference_id use msg_id later okk
                                                    retrofit2.Call<MessageResult> call1 = iMyService.setReceived(j1.getString("id"));
                                                    call1.enqueue(new Callback<MessageResult>() {
                                                        @Override
                                                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                                            //Toast.makeText(ChatPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                                            Toast.makeText(ChatPage.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    //      below conditions to be executed and the message to be inserted in local database
                                                }
                                                String timestamp = j1.getString("timestamp");
                                                timestamp = timestamp.substring(16, 25);
                                                addMessage(data, locationStateCountry, true, ref_id, jj1.getString("data_type"), timestamp, isImportant);
                                            }
                                        }
                                    }
                                    try{
                                        String message = getIntent().getExtras().getString("message_to_be_forwarded");
                                        String data_type = getIntent().getExtras().getString("data_type");
                                        System.out.println("message_to_be_forwarded Chat Page: "+message);
                                        System.out.println("data_type_to_be_forwarded Chat Page: "+data_type);
                                        isForward = true;
                                        if (message.equals(""))
                                            Toast.makeText(getApplicationContext(),"Some Error in Forwarding Message",Toast.LENGTH_SHORT).show();
                                        else {
                                            if(data_type.equals("MESSAGE")) {
                                                sendForwardMessage(message);
                                            }else{
                                                sendForwardFile(message,data_type);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

                            }
                        });

                        if (MyApplicationContext.getChatSocket(sender_id, sender_id + "_" + receiver_id, receiver_id + "_" + sender_id) == null) {
                            try {
                                IO.Options mOptions = new IO.Options();
                                mOptions.query = "sender_id=" + sender_id + "&receiver_id=" + receiver_id;
                                socket = IO.socket(Constants.CHAT_SERVER_URI, mOptions);
                                //setHasOptionsMenu(true);
                                socket.connect();
                                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                                    @Override
                                    public void call(Object... args) {
                                        socket.emit("connected", sender_id, receiver_id, socket.id());
                                    }
                                });
                                JSONObject new_chat_connection = new JSONObject();
                                new_chat_connection.put("user_id", sender_id);
                                new_chat_connection.put("socket", socket);
                                new_chat_connection.put("chat_id", sender_id + "_" + receiver_id);
                                MyApplicationContext.setChatSocket(new_chat_connection);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        } else {
                            socket = MyApplicationContext.getChatSocket(sender_id, sender_id + "_" + receiver_id, receiver_id + "_" + sender_id);
                            socket.emit("connected", sender_id, receiver_id, socket.id());
                        }
                        System.out.println("SocketID:" + socket.id());
                        socket.on("message", handleIncomingMessages);
                        callButton = findViewById(R.id.call);
                        mAdapter = new MessageAdapter(mMessages, ChatPage.this);
                        mInputMessageView = (EditText) findViewById(R.id.message_input);
                        profileimg = (CircleImageView) findViewById(R.id.call1);
                        mMessagesView = (RecyclerView) findViewById(R.id.messages);
                        videoCallView = (ImageView) findViewById(R.id.videocam);
                        mMessagesView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        mMessagesView.setAdapter(mAdapter);

                        callButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ChatPage.this, "Upcoming Feature", Toast.LENGTH_SHORT).show();
//                                callButtonClicked();
                            }
                        });

                        ImageView attach = (ImageView) findViewById(R.id.btn_attach);
                        sendButton = (ImageView) findViewById(R.id.send_button);
                        attach.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                displayPopupWindow(v);
                            }
                        });
                        sendButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendMessage();

                            }
                        });

                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                        //setSupportActionBar(toolbar);
                        Log.d("User", getIntent().getExtras().getString("name"));
                        try {
                            userText.setText(getIntent().getExtras().getString("name").toString());
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            Log.e("User", e.toString());
                        }

                        ImageView videoCallView = (ImageView) findViewById(R.id.videocam);
                        videoCallView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ChatPage.this, "Upcoming Feature", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //      getting location
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                        SharedPreferences sharedPreferences1 = getSharedPreferences("location", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences1.edit();

                        locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationRequest.setInterval(20 * 1000);
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null) return;
                                for (Location location : locationResult.getLocations()) {
                                    if (location != null) {
                                        geocoder = new Geocoder(ChatPage.this, Locale.getDefault());
                                        try {
                                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            locationStateCountry = addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea();

                                            Log.d("location", locationStateCountry);

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }, Looper.myLooper());
                        editor.putString("locationStateCountry", locationStateCountry);
                        editor.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

                }
            });
            //Chat dots implementation
            chatDots = (ImageView) findViewById(R.id.chat_three_dots);
            popup = new PopupMenu(ChatPage.this, chatDots);
            popup.getMenuInflater().inflate(R.menu.chat_three_dots_menu, popup.getMenu());


            chatDots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(ChatPage.this, "chatDots", Toast.LENGTH_SHORT).show();

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(ChatPage.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            if (item.getItemId() == R.id.block_chat) {
                                System.out.println("Block User " + logged_in_user + " " + current_chat_user);
                                retrofit2.Call<MessageResult> blockUser = iMyService.blockUser(logged_in_user, current_chat_user);
                                blockUser.enqueue(new Callback<MessageResult>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                        Toast.makeText(ChatPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    /*findViewById(R.id.MessageLayoutChatPage).setVisibility(View.INVISIBLE);
                                    findViewById(R.id.BlockedMessagelayout).setVisibility(View.VISIBLE);
                                    popup.getMenu().findItem(R.id.block_chat).setVisible(false);
                                    popup.getMenu().findItem(R.id.unblock_chat).setVisible(true);*/
                                        blockedUser();
                                        isBlocked = Boolean.TRUE;
                                    }

                                    @Override
                                    public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                        Toast.makeText(ChatPage.this, "Unable to Block User", Toast.LENGTH_SHORT).show();
                                        System.out.println(t);
                                    }
                                });

                            } else if (item.getItemId() == R.id.unblock_chat) {
                                retrofit2.Call<MessageResult> unblockUser = iMyService.unblockUser(logged_in_user, current_chat_user);
                                unblockUser.enqueue(new Callback<MessageResult>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                    /*findViewById(R.id.MessageLayoutChatPage).setVisibility(View.VISIBLE);
                                    findViewById(R.id.BlockedMessagelayout).setVisibility(View.INVISIBLE);
                                    popup.getMenu().findItem(R.id.block_chat).setVisible(true);
                                    popup.getMenu().findItem(R.id.unblock_chat).setVisible(false);*/
                                        unblockedUser();
                                        isBlocked = Boolean.FALSE;
                                        Toast.makeText(ChatPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                        Toast.makeText(ChatPage.this, "Unable to UnBlock User", Toast.LENGTH_SHORT).show();
                                        System.out.println(t);
                                    }
                                });
                            } else if (item.getItemId() == R.id.search_chat) {
                                findViewById(R.id.searchLayoutChatPage).setVisibility(View.VISIBLE);
                                ImageView closeSearch = findViewById(R.id.close_search_button);
                                EditText searchText = findViewById(R.id.search_input_chat);
                                mAllMessages = mMessages;
                                searchText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        // TODO Auto-generated method stub
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        getFilter(s.toString());
                                        //mAdapter.notifyDataSetChanged();

                                        System.out.println("No. of Items" + mAdapter.getItemCount());
                                        System.out.println("Search " + s.toString());
                                    }
                                });

                                closeSearch.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        findViewById(R.id.searchLayoutChatPage).setVisibility(View.INVISIBLE);
                                        mMessages = mAllMessages;
                                        mAdapter = new MessageAdapter(mMessages, ChatPage.this);
                                        mMessagesView.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
/*
                        else if(item.getItemId() == R.id.ssl_chat){
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.set_security_level_dialog);
                            dialog.setTitle("Set Security Level...");

                            Button submit_ssl = (Button)dialog.findViewById(R.id.ssl_submit_popup);
                            CheckBox check_ssl_vault = (CheckBox)dialog.findViewById(R.id.ssl_vault_checkbox);

//                            check_ssl_vault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                                @Override
//                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                    if(isChecked){
//                                        moveToVault = Boolean.TRUE;
//                                    }
//                                    else{
//                                        moveToVault = Boolean.FALSE;
//                                    }
//                                    System.out.println("isChecked"+isChecked);
//                                }
//                            });

                            submit_ssl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    isConfidential = Boolean.TRUE;

                                    System.out.println("IsConfidential : "+isConfidential);

                                    int temp_ssl = 0;
                                    if(check_ssl_vault.isChecked()){
                                        System.out.println("moveToVault : True "+moveToVault);
                                        moveToVault = Boolean.TRUE;
                                    }
                                    else{
                                        System.out.println("moveToVault : False "+moveToVault+" . "+check_ssl_vault.isChecked());
                                        moveToVault = Boolean.FALSE;

                                    }
                                    if(moveToVault){
                                        temp_ssl+=1;
                                    }
                                    if(isConfidential){
                                        temp_ssl+=1;
                                    }
                                    System.out.println("TempSSL : "+temp_ssl);
                                    //Call<MessageResult> setSSLChat = iMyService.setSSLChat(logged_in_user,current_chat_user,String.valueOf(temp_ssl));
                                    //Using dummy Details for ssl chat
                                    Call<MessageResult> setSSLChat = iMyService.setSSLChat("rks","skr",String.valueOf(temp_ssl));
                                    setSSLChat.enqueue(new Callback<MessageResult>() {
                                        @Override
                                        public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                                            Toast.makeText(ChatPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<MessageResult> call, Throwable t) {
                                            Toast.makeText(ChatPage.this, "Some Error Occured", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            });

                            dialog.show();


                        }
*/

                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }

    }
    public void getFilter(String s) {
        ArrayList<Message> filteredList = new ArrayList<Message>();
        mMessages = mAllMessages;
        for(Message message : mMessages) {
            if (message.getMessage().toLowerCase().contains(s)) {
                System.out.println("Printing Filtered Results : " + message.getMessage());
                filteredList.add(message);
            }
        }
        mMessages = filteredList;
        mAdapter = new MessageAdapter(mMessages, ChatPage.this);
        mMessagesView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    public void blockedUser(){
        findViewById(R.id.MessageLayoutChatPage).setVisibility(View.INVISIBLE);
        findViewById(R.id.BlockedMessagelayout).setVisibility(View.VISIBLE);
        popup.getMenu().findItem(R.id.block_chat).setVisible(false);
        popup.getMenu().findItem(R.id.ttl_chat).setVisible(true);
//        popup.getMenu().findItem(R.id.ssl_chat).setVisible(false);
        popup.getMenu().findItem(R.id.search_chat).setVisible(true);
        popup.getMenu().findItem(R.id.unblock_chat).setVisible(true);
        callButton = findViewById(R.id.call);
        callButton.setVisibility(View.INVISIBLE);
        videoCallView.setVisibility(View.INVISIBLE);
    }

    public void unblockedUser(){
        findViewById(R.id.MessageLayoutChatPage).setVisibility(View.VISIBLE);
        findViewById(R.id.BlockedMessagelayout).setVisibility(View.INVISIBLE);
        popup.getMenu().findItem(R.id.block_chat).setVisible(true);
        popup.getMenu().findItem(R.id.ttl_chat).setVisible(true);
//        popup.getMenu().findItem(R.id.ssl_chat).setVisible(true);
        popup.getMenu().findItem(R.id.search_chat).setVisible(true);
        popup.getMenu().findItem(R.id.unblock_chat).setVisible(false);
        callButton = findViewById(R.id.call);
        callButton.setVisibility(View.VISIBLE);
        videoCallView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu=menu;
        System.out.println("Mennu "+menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatpage_threedots, menu);
        return true;
    }

    private void displayPopupWindow(View view) {
        int[] loc_int = new int[2];
        PopupWindow popup = new PopupWindow(ChatPage.this);
        View layout = getLayoutInflater().inflate(R.layout.popupwindow_attach, null);
        popup.setContentView(layout);
        popup.setBackgroundDrawable(null);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        //popup.setBackgroundDrawable(new BitmapDrawable());
        // Show anchored to button
        view.getLocationOnScreen(loc_int);
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        //location.bottom = location.top - view.getHeight();
        location.bottom = view.getBottom() + view.getHeight() + view.getTop() + 20;
        popup.showAtLocation(view, Gravity.BOTTOM, 0, location.bottom);
        ImageView imgbtn = (ImageView) layout.findViewById(R.id.camera);
        ImageView docbtn = (ImageView) layout.findViewById(R.id.document);
        ImageView videobtn=(ImageView)layout.findViewById(R.id.location);
        ImageView audiobtn=(ImageView)layout.findViewById(R.id.audio);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send;
                openGallery();
            }
        });

        videobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, 10);
            }
        });

        audiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, 10);
            }
        });

        docbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 10);
            }
        });

    }

    private void openGallery() {
        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryintent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            /*Uri selectedImage = data.getData();
            String[] filePathColum = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColum, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColum[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            System.out.println("imgDec" + imgDecodableString);
            cursor.close();
            sendImage(imgDecodableString);*/
            String PathHolder = data.getData().getPath();
            Log.d("Path", PathHolder);
            Uri uri = data.getData();
            String pp = getPath(getApplicationContext(), uri);
            File file = new File(pp);
            System.out.println("PP" + pp);
            String filename = pp.substring(pp.lastIndexOf("/") + 1);
            String extension = pp.substring(pp.lastIndexOf("."));
            try {
                String encodeFileToBase64Binary = encodeFileToBase64Binary(file);
                System.out.println("Strin" + filename + "Exte" + extension);
                sendText.put("data_type", "IMAGE");
                sendText.put("sender_id", sender_id);
                sendText.put("receiver_id", receiver_id);
                sendText.put("location", locationStateCountry);
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                sendText.put("time",currentTime);
                sendText.put("msg2", filename);
                sendText.put("msg3", " ");
                mesage = filename;
                sendText.put("msg1", encodeFileToBase64Binary);
                //TODO verify ref_id
//                String ref_id = "1234";
                Toast.makeText(context, "Media is Uploading", Toast.LENGTH_SHORT).show();
                socket.emit("message", sendText, new Ack() {
                    @Override
                    public void call(Object... args) {
                        reference_id = String.valueOf(args[0]);
                        System.out.println("R2EGEE" + reference_id);
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        String ref_id = reference_id;
                        retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                        call1.enqueue(new Callback<MessageResult>() {
                            @Override
                            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                System.out.println("mmesage" + response);
                                if (response.body().getResult().equals("true")) {
                                    System.out.println("IN");
                                    msg_sent = false;
                                    msg_received = true;          //message recevived by the receiver
                                    try {
                                        addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    System.out.println("IN2354");
                                    msg_sent = true;        //message only delivered to remote database
                                    msg_received = false;
                                    try {
                                        addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                
                                sendText = new JSONObject();
                            }

                            @Override
                            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                msg_sent = false;
                                msg_received = false;
                                try {
                                    addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                
                                sendText = new JSONObject();
                            }
                        });
                        
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 10 && resultCode == RESULT_OK && null != data) {
            String PathHolder = data.getData().getPath();
            Log.d("Path", PathHolder);
            Uri uri = data.getData();
            String pp = getPath(getApplicationContext(), uri);
            File file = new File(pp);
            System.out.println("PP" + pp);
            String filename = pp.substring(pp.lastIndexOf("/") + 1);
            String extension = pp.substring(pp.lastIndexOf("."));
            if (extension.equals(".pdf") || extension.equals(".img") || extension.equals(".docx") || extension.equals(".pptx") || extension.equals(".xlsx") || extension.equals(".mp3") || extension.equals(".ppt")) {
                try {
                    String encodeFileToBase64Binary = encodeFileToBase64Binary(file);
                    sendText.put("data_type", "FILES");
                    sendText.put("sender_id", sender_id);
                    sendText.put("receiver_id", receiver_id);
                    sendText.put("location", locationStateCountry);
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    sendText.put("time",currentTime);
                    sendText.put("msg2", filename);
                    sendText.put("msg3", " ");
                    mesage = filename;
                    sendText.put("msg1", encodeFileToBase64Binary);
                    //TODO verify ref_id
//                    String ref_id = "1234";
                    Toast.makeText(context, "Media is Uploading", Toast.LENGTH_SHORT).show();
                    socket.emit("message", sendText, new Ack() {
                        @Override
                        public void call(Object... args) {
                            reference_id = String.valueOf(args[0]);
                            System.out.println("R2EGEE" + reference_id);
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                            String ref_id = reference_id;
                            call1.enqueue(new Callback<MessageResult>() {
                                @Override
                                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                    System.out.println("mmesage" + response);
                                    if (response.body().getResult().equals("true")) {
                                        System.out.println("IN");
                                        msg_sent = false;
                                        msg_received = true;          //message recevived by the receiver
                                        try {
                                            addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        System.out.println("IN2354");
                                        msg_sent = true;        //message only delivered to remote database
                                        msg_received = false;
                                        try {
                                            addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    
                                    sendText = new JSONObject();
                                }

                                @Override
                                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                    msg_sent = false;
                                    msg_received = false;
                                    try {
                                        addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    
                                    sendText = new JSONObject();
                                }
                            });
                            
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(extension.equals(".mp4"))
            {
                try {
                    String encodeFileToBase64Binary = encodeFileToBase64Binary(file);
                    sendText.put("data_type", "VIDEO");
                    sendText.put("sender_id", sender_id);
                    sendText.put("receiver_id", receiver_id);
                    sendText.put("location", locationStateCountry);
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    sendText.put("time",currentTime);
                    sendText.put("msg2", filename);
                    sendText.put("msg3", " ");
                    mesage = filename;
                    sendText.put("msg1", encodeFileToBase64Binary);
                    //TODO verify ref_id
//                    String ref_id = "1234";
                    Toast.makeText(context, "Media is Uploading", Toast.LENGTH_SHORT).show();
                    socket.emit("message", sendText, new Ack() {
                        @Override
                        public void call(Object... args) {
                            reference_id = String.valueOf(args[0]);
                            System.out.println("R2EGEE" + reference_id);
                            String ref_id = reference_id;
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                            call1.enqueue(new Callback<MessageResult>() {
                                @Override
                                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                    System.out.println("mmesage" + response);
                                    if (response.body().getResult().equals("true")) {
                                        System.out.println("IN");
                                        msg_sent = false;
                                        msg_received = true;          //message recevived by the receiver
                                        try {
                                            addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        System.out.println("IN2354");
                                        msg_sent = true;        //message only delivered to remote database
                                        msg_received = false;
                                        try {
                                            addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    
                                    sendText = new JSONObject();
                                }

                                @Override
                                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                    msg_sent = false;
                                    msg_received = false;
                                    try {
                                        addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    
                                    sendText = new JSONObject();
                                }
                            });
                            
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(this, "Invalid Media File", Toast.LENGTH_SHORT).show();
                flag = 2;
            }
        }
    }

    public void displayFile(String filename) {
        /*
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename);
        // Uri uri=Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", file);
        String mime = getContentResolver().getType(uri);
        System.out.println("Buils AppID: "+BuildConfig.APPLICATION_ID);

        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
         */
        Toast.makeText(context, "View File in Storage", Toast.LENGTH_SHORT).show();
    }

    ///storage/emulated/0/IdeasubmissionprocessSIH2020.pdf
    private void sendMessage() {
        String message = mInputMessageView.getText().toString().trim();
        mInputMessageView.setText("");
        if (message.equals("")) return;
        //addMessage(message, locationStateCountry, true);
        try {
            got_resp = false;
            ArrayList<Object> list = SecurityCaller.doEncryption(message);
            sendText.put("data_type", "MESSAGE");
            sendText.put("msg1", list.get(0));
            sendText.put("msg2", list.get(1));
            sendText.put("msg3", list.get(2));
            sendText.put("sender_id", sender_id);
            sendText.put("receiver_id", receiver_id);
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            sendText.put("time",currentTime);
            myDb.addMessage(String.valueOf(list.get(0)), String.valueOf(list.get(1)), String.valueOf(list.get(2)));
            sendText.put("location", locationStateCountry);
            sendText.put("isSend", true);
            sendText.put("id", 1234);
            //TODO verify ID again
//            String ref_id = "1234";
            socket.emit("message", sendText, new Ack() {
                @Override
                public void call(Object... args) {
                    reference_id = String.valueOf(args[0]);
                    System.out.println("R2EGEE" + reference_id);
                    String ref_id = reference_id;
                    retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                    call1.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                            Log.d("qwerty1", String.valueOf(response.body().getResult()));
                            if (response.body().getResult().equals("true")) {
                                System.out.println("IN");
                                msg_sent = false;
                                msg_received = true;          //message recevived by the receiver
                                addMessage(message, locationStateCountry, true,ref_id,"MESSAGE",currentTime,false);
                            } else {
                                System.out.println("IN2354");
                                msg_sent = true;        //message only delivered to remote database
                                msg_received = false;
                                addMessage(message, locationStateCountry, true,ref_id,"MESSAGE",currentTime,false);
                            }
                            
                            sendText = new JSONObject();
                        }

                        @Override
                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                            System.out.println("fghjt" + t);
                            msg_sent = false;
                            msg_received = false;
                            addMessage(message, locationStateCountry, true,ref_id,"MESSAGE",currentTime,false);
                            
                            sendText = new JSONObject();
                        }
                    });
                    
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("REGEE" + reference_id);
        //addMessage(message, locationStateCountry, true);
    }

    private void sendForwardMessage(String msg) {
//        String message = mInputMessageView.getText().toString().trim();

        String message = msg;
        mInputMessageView.setText("");
        System.out.println("SendFOrwardsMessage "+message);
        if (message.equals("")) return;
        //addMessage(message, locationStateCountry, true);
        try {
            got_resp = false;
            ArrayList<Object> list = SecurityCaller.doEncryption(message);
            sendText.put("data_type", "MESSAGE");
            sendText.put("msg1", list.get(0));
            sendText.put("msg2", list.get(1));
            sendText.put("msg3", list.get(2));
            sendText.put("sender_id", sender_id);
            sendText.put("receiver_id", receiver_id);
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            sendText.put("time",currentTime);
            myDb.addMessage(String.valueOf(list.get(0)), String.valueOf(list.get(1)), String.valueOf(list.get(2)));
            sendText.put("location", locationStateCountry);
            sendText.put("isSend", true);
            sendText.put("id", 1234);

            //TODO verify ID again
            String ref_id = "1234";
            socket.emit("message", sendText, new Ack() {
                @Override
                public void call(Object... args) {
                    reference_id = String.valueOf(args[0]);
                    System.out.println("R2EGEE" + reference_id);
                    String ref_id = reference_id;
                    retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                    call1.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                            Log.d("qwerty1", String.valueOf(response.body().getResult()));
                            if (response.body().getResult().equals("true")) {
                                System.out.println("IN");
                                msg_sent = false;
                                msg_received = true;          //message recevived by the receiver
                                addMessage(message, locationStateCountry, true,ref_id,"MESSAGE",currentTime,false);
                            } else {
                                System.out.println("IN2354");
                                msg_sent = true;        //message only delivered to remote database
                                msg_received = false;
                                addMessage(message, locationStateCountry, true,ref_id,"MESSAGE",currentTime,false);
                            }
                            
                            sendText = new JSONObject();
                        }

                        @Override
                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                            System.out.println("fghjt" + t);
                            msg_sent = false;
                            msg_received = false;
                            addMessage(message, locationStateCountry, true,ref_id,"MESSAGE",currentTime,false);
                            
                            sendText = new JSONObject();
                        }
                    });
                    
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("REGEE" + reference_id);
        //addMessage(message, locationStateCountry, true);
    }

    private void sendForwardFile(String filename, String data_type) {
        if (data_type.equals("IMAGE")) {
            String pp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +filename;
            System.out.println("Forwarded Image path : "+pp);
            File file = new File(pp);
            String extension = pp.substring(pp.lastIndexOf("."));
            try {
                String encodeFileToBase64Binary = encodeFileToBase64Binary(file);
                System.out.println("Strin" + filename + "Exte" + extension);
                sendText.put("data_type", "IMAGE");
                sendText.put("sender_id", sender_id);
                sendText.put("receiver_id", receiver_id);
                sendText.put("location", locationStateCountry);
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                sendText.put("time",currentTime);
                sendText.put("msg2", filename);
                sendText.put("msg3", " ");
                mesage = filename;
                sendText.put("msg1", encodeFileToBase64Binary);
                //TODO verify ref_id
//                String ref_id = "1234";
                socket.emit("message", sendText, new Ack() {
                    @Override
                    public void call(Object... args) {
                        reference_id = String.valueOf(args[0]);
                        System.out.println("R2EGEE" + reference_id);
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        String ref_id = reference_id;
                        retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                        call1.enqueue(new Callback<MessageResult>() {
                            @Override
                            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                System.out.println("mmesage" + response);
                                if (response.body().getResult().equals("true")) {
                                    System.out.println("IN");
                                    msg_sent = false;
                                    msg_received = true;          //message recevived by the receiver
                                    try {
                                        addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    System.out.println("IN2354");
                                    msg_sent = true;        //message only delivered to remote database
                                    msg_received = false;
                                    try {
                                        addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                
                                sendText = new JSONObject();
                            }

                            @Override
                            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                msg_sent = false;
                                msg_received = false;
                                try {
                                    addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                
                                sendText = new JSONObject();
                            }
                        });
                        
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            String pp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +filename;
            System.out.println("Forwarded File path : "+pp);
            File file = new File(pp);
            String extension = pp.substring(pp.lastIndexOf("."));
            if (extension.equals(".pdf") || extension.equals(".img") || extension.equals(".docx") || extension.equals(".pptx") || extension.equals(".xlsx") || extension.equals(".mp3") || extension.equals(".ppt")) {
                try {
                    String encodeFileToBase64Binary = encodeFileToBase64Binary(file);
                    sendText.put("data_type", "FILES");
                    sendText.put("sender_id", sender_id);
                    sendText.put("receiver_id", receiver_id);
                    sendText.put("location", locationStateCountry);
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    sendText.put("time",currentTime);
                    sendText.put("msg2", filename);
                    sendText.put("msg3", " ");
                    mesage = filename;
                    sendText.put("msg1", encodeFileToBase64Binary);
                    //TODO verify ref_id
//                    String ref_id = "1234";
                    socket.emit("message", sendText, new Ack() {
                        @Override
                        public void call(Object... args) {
                            reference_id = String.valueOf(args[0]);
                            System.out.println("R2EGEE" + reference_id);
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                            String ref_id = reference_id;
                            call1.enqueue(new Callback<MessageResult>() {
                                @Override
                                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                    System.out.println("mmesage" + response);
                                    if (response.body().getResult().equals("true")) {
                                        System.out.println("IN");
                                        msg_sent = false;
                                        msg_received = true;          //message recevived by the receiver
                                        try {
                                            addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        System.out.println("IN2354");
                                        msg_sent = true;        //message only delivered to remote database
                                        msg_received = false;
                                        try {
                                            addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    
                                    sendText = new JSONObject();
                                }

                                @Override
                                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                    msg_sent = false;
                                    msg_received = false;
                                    try {
                                        addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    
                                    sendText = new JSONObject();
                                }
                            });
                            
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(extension.equals(".mp4"))
            {
                try {
                    String encodeFileToBase64Binary = encodeFileToBase64Binary(file);
                    sendText.put("data_type", "VIDEO");
                    sendText.put("sender_id", sender_id);
                    sendText.put("receiver_id", receiver_id);
                    sendText.put("location", locationStateCountry);
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    sendText.put("time",currentTime);
                    sendText.put("msg2", filename);
                    sendText.put("msg3", " ");
                    mesage = filename;
                    sendText.put("msg1", encodeFileToBase64Binary);
                    //TODO verify ref_id
//                    String ref_id = "1234";
                    socket.emit("message", sendText, new Ack() {
                        @Override
                        public void call(Object... args) {
                            reference_id = String.valueOf(args[0]);
                            System.out.println("R2EGEE" + reference_id);
                            String ref_id = reference_id;
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                            call1.enqueue(new Callback<MessageResult>() {
                                @Override
                                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                    System.out.println("mmesage" + response);
                                    if (response.body().getResult().equals("true")) {
                                        System.out.println("IN");
                                        msg_sent = false;
                                        msg_received = true;          //message recevived by the receiver
                                        try {
                                            addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        System.out.println("IN2354");
                                        msg_sent = true;        //message only delivered to remote database
                                        msg_received = false;
                                        try {
                                            addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    
                                    sendText = new JSONObject();
                                }

                                @Override
                                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                    msg_sent = false;
                                    msg_received = false;
                                    try {
                                        addMessage(mesage, null, true,ref_id,sendText.getString("data_type"),currentTime,false);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    
                                    sendText = new JSONObject();
                                }
                            });
                            
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(this, "Invalid Media File", Toast.LENGTH_SHORT).show();
                flag = 2;
            }
        }
    }

    public void sendImage(String path) {
        try {
            mesage = encodeImage(path);
            sendText.put("data_type", "IMAGE");
            sendText.put("sender_id", sender_id);
            sendText.put("receiver_id", receiver_id);
            sendText.put("location", locationStateCountry);
            sendText.put("msg1", mesage);
            sendText.put("msg2", "");
            sendText.put("msg3", "");
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            sendText.put("time",currentTime);
            sendText.put("id", 1234);
            sendText.put("isSend", true);
            Log.d("Image ", String.valueOf(sendText));


        } catch (JSONException e) {

        }
    }

    private Bitmap decodeImage(String data) {
        byte[] b = Base64.decode(data, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bmp;
    }

    private void addImage(Bitmap bmp, boolean isSend) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .image(bmp).isSend(isSend).build());
        mAdapter = new MessageAdapter(mMessages);
        mAdapter.notifyItemInserted(0);
        scrollToBottom();
    }

    private String encodeImage(String path) {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

    //addMessage(honeyobj1.getString("msg") ,honeyobj1.getString("location"), false,"abc","MESSAGE","14:02",false);
    private void addMessage(String message, String loc, boolean isSend,String reference_id,String datatype,String time,boolean imp) {
        System.out.println("Print Message : "+message + "importance : "+imp);
        Message mm = new Message.Builder(Message.TYPE_MESSAGE)
                .message(message).location(loc).datatype(datatype).timestamp(time).uniqueId(reference_id).isSend(isSend).isImportant(imp).build();
        mm.setImportant(imp);
        System.out.println("ADD message" +mm);
        try{
            mMessages.add(mm);
            mAdapter = new MessageAdapter(mMessages);
            mAdapter.notifyItemInserted(0);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        //mam shall we google it n try to find out?
        // mAdapter = new MessageAdapter(mMessages);
        //mAdapter = new MessageAdapter( mMessages);

        SharedPreferences sharedPreferences1 = getSharedPreferences("userDetails", MODE_PRIVATE);
        String honey = sharedPreferences1.getString("ishoney", "n12");
        System.out.println("Honey Chatpage : "+honey);
        /*if(honey.equals("yes")) {
        }else {*/
        scrollToBottom();

    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private void prepareSelection(int position) {
        if (!mMessages.contains(mMessages.get(position))) {
            mMessages.add(mMessages.get(position));
        } else {
            mMessages.remove(mMessages.get(position));
        }
        updateViewCounter();
    }

    private void updateViewCounter() {
        int counter = mMessages.size();
        if (counter == 1) {
            toolbar.getMenu().getItem(0).setVisible(true);
        } else {
            toolbar.getMenu().getItem(0).setVisible(false);
        }
        toolbar.setTitle(counter + "item(s) selected");
    }


    private void callButtonClicked() {
        String userName = getIntent().getExtras().getString("UserName");
        System.out.println("Calling " + userName);
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Toast.makeText(getApplicationContext(), "Username " + userName, Toast.LENGTH_SHORT).show();
            Call call = getSinchServiceInterface().callUser(userName);
            System.out.println(getSinchServiceInterface());
            Log.d("call", "callButtonClicked: call " + call);
            //Call call = getSinchServiceInterface().callUser(userName);
            if (call == null) {
                // Service failed for some reason, show a Toast and abort
                Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
                        + "placing a call.", Toast.LENGTH_LONG).show();
                return;
            }
            String callId = call.getCallId();
            Intent callScreen = new Intent(this, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            startActivity(callScreen);
        } catch (MissingPermissionException e) {
            ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now place a call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }


    private void clearActionMode() {
        isInActionMode = false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            retrofit2.Call<MessageResult> call = iMyService.deleteMessage(mMessages.get(selectedMessage).getUniqueId());
            call.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                    System.out.println("Response Deleted"+response.body().getMessage());
                    System.out.println("Array Position"+selectedMessageArrayPosition);
                    System.out.println("Array Message "+mMessages.get(selectedMessageArrayPosition).getMessage());
                    //TODO Check for adaptor why isn't it removing element
                    mMessages.remove(selectedMessageArrayPosition);
//                    mAdapter. notifyItemRemoved(selectedMessageArrayPosition);
//                    mAdapter.notifyItemRangeChanged(0, mMessages.size());
                    mAdapter = new MessageAdapter(mMessages, ChatPage.this);
                    mMessagesView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

                }
            });
            //Toast.makeText(this, "Message Deleted", Toast.LENGTH_SHORT).show();
        }

        else if(R.id.copy == item.getItemId()){
            toCopy = Boolean.FALSE;
            retrofit2.Call<MessageResult> toCpy = iMyService.getSSLmsg(mMessages.get(selectedMessage).getUniqueId());
            toCpy.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                    String r = response.body().getMessage();
                    System.out.println("Copy" + r);
                    if(r.equals("false")){
                        toCopy = Boolean.FALSE;
                    }
                    if(r.equals("true")){
                        toCopy = Boolean.TRUE;
                    }
                    System.out.println("toCopy" + toCopy);
                    if(toCopy){
                        Toast.makeText(context, "Cannot Copy because of Confidential Level", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ChatPage.this, "Text Copied", Toast.LENGTH_SHORT).show();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Arishti", mMessages.get(selectedMessage).getmMessage());
                        clipboard.setPrimaryClip(clip);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                    Toast.makeText(context, "Failed to Copy", Toast.LENGTH_SHORT).show();
                }
            });

        }

        else if(R.id.important == item.getItemId()){
            retrofit2.Call<MessageResult> call = iMyService.setImportant(mMessages.get(selectedMessage).getUniqueId());
            call.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    mMessages.get(selectedMessage).setImportant(true);
                    mMessages.get(selectedMessageArrayPosition).setImportant(true);
//                    mAdapter.notifyItemChanged(selectedMessageArrayPosition);
                    mAdapter = new MessageAdapter(mMessages, ChatPage.this);
                    mMessagesView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

                }
            });
        }

        else if(R.id.hidemessage == item.getItemId()){
            retrofit2.Call<MessageResult> call = iMyService.hideMessage(mMessages.get(selectedMessage).getUniqueId());
            call.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    mMessages.remove(selectedMessageArrayPosition);
//                    mAdapter. notifyItemRemoved(selectedMessageArrayPosition);
//                    mAdapter.notifyItemRangeChanged(0, mMessages.size());
                    mAdapter = new MessageAdapter(mMessages, ChatPage.this);
                    mMessagesView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

                }
            });
        }

        else if(R.id.info == item.getItemId()){
            Intent i = new Intent(getApplicationContext(),MessageInfo.class);
            i.putExtra("reference_id",mMessages.get(selectedMessage).getUniqueId());
            i.putExtra("timestamp",mMessages.get(selectedMessage).getTimestamp());
            i.putExtra("location",mMessages.get(selectedMessage).getLocation());
            startActivity(i);
        }

        else if(R.id.forward == item.getItemId())
        {
            isForwardAllowed = Boolean.FALSE;
            retrofit2.Call<MessageResult> fwdAllowed = iMyService.getSSLmsg(mMessages.get(selectedMessage).getUniqueId());
            fwdAllowed.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                    String r = response.body().getMessage();
                    if(r.equals("false")){
                        isForwardAllowed = Boolean.FALSE;
                    }
                    if(r.equals("true")){
                        isForwardAllowed = Boolean.TRUE;
                    }
                    if(isForwardAllowed){
                        Toast.makeText(context, "Cannot Forward because of Confidential Level", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent i = new Intent(getApplicationContext(),message_forward_list.class);
                        i.putExtra("message_to_be_forwarded",mMessages.get(selectedMessage).getmMessage());
                        i.putExtra("data_type",mMessages.get(selectedMessage).getDatatype());
                        System.out.println("Forward data_Type : "+mMessages.get(selectedMessage).getDatatype());
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                    System.out.println("Error"+ t.toString());
                }
            });

        }


/*
        else if(R.id.sl_low == item.getItemId()){
            Call<MessageResult> call = iMyService.set_security_level(mMessages.get(selectedMessage).getUniqueId(),"Low");
            call.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<MessageResult> call, Throwable t) {

                }
            });
            //Toast.makeText(this, "Low Security Level", Toast.LENGTH_SHORT).show();
        }

        else if(R.id.sl_medium == item.getItemId()){
            Call<MessageResult> call = iMyService.set_security_level(mMessages.get(selectedMessage).getUniqueId(),"Medium");
            call.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<MessageResult> call, Throwable t) {

                }
            });
            //Toast.makeText(this, "Medium Security Level", Toast.LENGTH_SHORT).show();
        }

        else if(R.id.sl_high == item.getItemId()){
            Call<MessageResult> call = iMyService.set_security_level(mMessages.get(selectedMessage).getUniqueId(),"High");
            call.enqueue(new Callback<MessageResult>() {
                @Override
                public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<MessageResult> call, Throwable t) {

                }
            });
            //Toast.makeText(this, "High Security Level", Toast.LENGTH_SHORT).show();
        }
*/
        else if(item.getItemId() == R.id.setsl){
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.set_security_level_dialog);
            dialog.setTitle("Set Security Level...");

            Button submit_ssl = (Button)dialog.findViewById(R.id.ssl_submit_popup);
            CheckBox check_ssl_vault = (CheckBox)dialog.findViewById(R.id.ssl_vault_checkbox);

//                            check_ssl_vault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                                @Override
//                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                    if(isChecked){
//                                        moveToVault = Boolean.TRUE;
//                                    }
//                                    else{
//                                        moveToVault = Boolean.FALSE;
//                                    }
//                                    System.out.println("isChecked"+isChecked);
//                                }
//                            });

            submit_ssl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    isConfidential = Boolean.TRUE;

                    System.out.println("IsConfidential : "+isConfidential);

                    int temp_ssl = 0;
                    if(check_ssl_vault.isChecked()){
                        System.out.println("moveToVault : True "+moveToVault);
                        moveToVault = Boolean.TRUE;
                    }
                    else{
                        System.out.println("moveToVault : False "+moveToVault+" . "+check_ssl_vault.isChecked());
                        moveToVault = Boolean.FALSE;

                    }
                    if(moveToVault){
                        temp_ssl+=1;
                    }
                    if(isConfidential){
                        temp_ssl+=1;
                    }
                    System.out.println("TempSSL : "+temp_ssl);
                    //Call<MessageResult> setSSLChat = iMyService.setSSLChat(logged_in_user,current_chat_user,String.valueOf(temp_ssl));
                    //Using dummy Details for ssl chat
                    retrofit2.Call<MessageResult> setSSLChat = iMyService.set_security_level(mMessages.get(selectedMessage).getUniqueId(),String.valueOf(temp_ssl));
                    setSSLChat.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                            Toast.makeText(ChatPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                            Toast.makeText(ChatPage.this, "Some Error Occured", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });

            dialog.show();


        }


        SelectedView.setBackgroundColor(getResources().getColor(R.color.message_deselected));
//        selectedMessageArrayPosition = -1;
//        selectedMessage = -1;
        callButton.setVisibility(View.VISIBLE);
        videoCallView.setVisibility(View.VISIBLE);
        userText.setVisibility(View.VISIBLE);
        profileimg.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        chatDots = (ImageView)findViewById(R.id.chat_three_dots);
        chatDots.setVisibility(View.VISIBLE);

        return true;
    }

    public boolean onLongClick(View v,int i,String datatype) {
//        Toast.makeText(this, "Innnnnn", Toast.LENGTH_SHORT).show();
        callButton.setVisibility(View.INVISIBLE);
        videoCallView.setVisibility(View.GONE);
        userText.setVisibility(View.INVISIBLE);
        profileimg.setVisibility(View.GONE);
        SelectedView = v;
        selectedMessageArrayPosition = i;
        selectedMessage = i;

        if(datatype.equals("IMAGE")||datatype.equals("FILES")||datatype.equals("VIDEO"))
        {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.files_message_longclick_options);
            toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        }
        else
        {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.message_longclick_options);
            toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        }
        mAdapter.notifyDataSetChanged();
        System.out.println("DATA "+datatype);
        chatDots = (ImageView)findViewById(R.id.chat_three_dots);
        chatDots.setVisibility(View.INVISIBLE);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }


    public boolean hasActiveInternetConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onBackPressed() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        super.onBackPressed();
    }
}
