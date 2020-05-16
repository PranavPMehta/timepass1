package com.example.rohit.arishit_f.login.chat;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.rohit.arishit_f.BuildConfig;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.constants.Constants;
import com.example.rohit.arishit_f.dashboard.ThreetabSlider;
import com.example.rohit.arishit_f.faq.faq_main;
import com.example.rohit.arishit_f.group_view.group_view;
import com.example.rohit.arishit_f.login.MessageResult;
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
import java.security.acl.Group;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.On;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.rohit.arishit_f.MyApplicationContext;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class GroupChat extends BaseActivity{
    private static final int SIGNUP_REQUEST = 1;
    public static boolean isInActionMode = false;
    public static boolean check_online = false, msg_sent, msg_received, got_resp = false;
    public static String locationStateCountry = null;
    public static String sender_id = "";


    public DatabaseAdapter myDb;
    Toolbar toolbar;
    Geocoder geocoder;
    List<Address> addresses;
    String format;
    IMyService iMyService;
    String reference_id, mesage;
    int flag = -1;
    JSONObject sendText = new JSONObject();
    private String st_path = Environment.getExternalStorageDirectory().getAbsolutePath();
    private TextView userText, messageText;
    private ImageView sendButton, callButton, videoCallView;
    private CircleImageView profileimg;
    private EditText mInputMessageView;
    private RecyclerView mMessagesView;
    private List<groupMessage> mMessages = new ArrayList<groupMessage>();
    private RecyclerView.Adapter mAdapter;
    private Socket socket;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private String group_id = null;
    private ImageView profileImage;
    private TextView Groupname;
    private int mes_position = 0;
    private String admin_id, userName, group_name;
    private ProgressBar progressBar;


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
                        try {
                            System.out.println("JSON_OBJECT" + data);
                            message = data.getString("message").toString();
                            msg = new JSONObject(message);
                            System.out.println("JSONOBJECT" + msg);
                            if (msg.getString("data_type").equals("MESSAGE")) {
                                loc = msg.getString("location");
                                List<Object> list = new ArrayList<>();
                                list.add(msg.get("msg1"));
                                list.add(msg.get("msg2"));
                                list.add(msg.get("msg3"));
                                retrofit2.Call<MessageResult> call1 = iMyService.setReceived(msg.getString("id")); //idha kya likhna hey dekh le
                                call1.enqueue(new Callback<MessageResult>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                        //Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                        Toast.makeText(GroupChat.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                String timestamp = msg.getString("time");
                                timestamp = timestamp.substring(11, 19);
                                myDb.addMessage(msg.getString("msg1"), msg.getString("msg2"), msg.getString("msg3"));
                                message = SecurityCaller.doDecryption(list);
                                addMessage(message, loc, false, msg.getString("data_type"), msg.getString("sender_id"), timestamp, msg.getBoolean("is_poll"), msg.getInt("agree"), msg.getInt("disagree"), msg.getInt("neutral"), msg.getString("id"));
                            } else if (msg.getString("data_type").equals("FILES") || msg.getString("data_type").equals("VIDEO") || msg.getString("data_type").equals("IMAGE")) {
                                //byte[] encoded = msg.getString("msg1").getBytes();
                                //messagetext = (TextView) findViewById(R.id.message);
                                System.out.println("IN IMAGE|FILES Receiver");
                                retrofit2.Call<MessageResult> call1 = iMyService.setReceived(msg.getString("id"));
                                call1.enqueue(new Callback<MessageResult>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                        //Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                        Toast.makeText(GroupChat.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                    }
                                });

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
                                String timestamp = msg.getString("time");
                                timestamp = timestamp.substring(11, 19);
                                addMessage(filename, null, false, msg.getString("data_type"), timestamp, msg.getString("sender_id"), msg.getBoolean("is_poll"), msg.getInt("agree"), msg.getInt("disagree"), msg.getInt("neutral"), msg.getString("id"));

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        myDb = new DatabaseAdapter(this);
        setContentView(R.layout.activity_chat_page);
        messageText = (TextView) findViewById(R.id.message);
        userText = (TextView) findViewById(R.id.userText);
        check_online = hasActiveInternetConnection();
        profileImage = (ImageView) findViewById(R.id.call1);
        Groupname = (TextView) findViewById(R.id.userText);
        progressBar = (ProgressBar) findViewById(R.id.profile_progress);
        group_id = getIntent().getStringExtra("id");
        userName = getIntent().getStringExtra("username");
        Groupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pr = new Intent(GroupChat.this, group_view.class);
                pr.putExtra("group_id", group_id);
                startActivity(pr);

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pr = new Intent(GroupChat.this, group_view.class);
                pr.putExtra("group_id",group_id);
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
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        retrofit2.Call<MessageResult> grp_info = iMyService.getGroupInfo(group_id);
        grp_info.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                try {
                    JSONArray infoarr = new JSONArray(response.body().getMessage());
                    System.out.println(infoarr.get(0));
                    JSONObject info = (JSONObject) infoarr.get(0);
                    Log.v("name", info.getString("group_name"));
                    group_name = info.getString("group_name");
                    Groupname.setText(group_name);
                    admin_id = info.getString("admin");
                    System.out.println("Start url");
                    JSONObject url = new JSONObject(info.getString("display_picture"));
                    System.out.println("Ready url");
                    String turl = url.getString("url");

                    if (!turl.equals("null")) {
                        String link = IP_ADDRESS + ":3000" + turl;
                        Log.v("link", link);
                        Glide.with(GroupChat.this)
                                .load(link)
                                .apply(
                                        new RequestOptions()
                                                .error(R.drawable.profileimg)
                                                .centerCrop()
                                )

                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        profileImage.setImageResource(R.drawable.profileimg);
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
                        profileImage.setImageResource(R.drawable.group_img_new);
                        progressBar.setVisibility(View.GONE);

                    }
                } catch (JSONException e) {
                    profileImage.setImageResource(R.drawable.group_img_new);
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String bearerToken = sharedPreferences.getString("bearer_token", "12345");

        retrofit2.Call<MessageResult> call = iMyService.getMyDetails("Bearer " + bearerToken);
        call.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                System.out.println(response.body().getMessage());
                JSONObject j1 = null;
                try {
                    j1 = new JSONObject(response.body().getMessage());
                    sender_id = j1.getString("user_id");
                    myDb.checkLastLogin(format);
                    System.out.println("Sender ID AFter:" + sender_id);
                    //Paste here
                    //From here
                    retrofit2.Call<MessageResult> call1 = iMyService.getGroupChats(group_id);
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
                                    if (j1.getString("user_id").equals(sender_id)) {
                                        //Receiver's Messages

                                        System.out.println("j1" + j1);
                                        JSONObject jj1 = j1.getJSONObject("msg");

                                        System.out.println(jj1);
                                        Log.d("jj1", jj1.getString("data"));
                                        if (jj1.getString("data_type").equals("MESSAGE")) {
                                            List<Object> list = new ArrayList<>();
                                            list.add(jj1.get("data"));
                                            list.add(jj1.get("key_1"));
                                            list.add(jj1.get("key_2"));
                                            Log.d("Referecnce", j1.getString("id"));
                                            /*if (j1.getString("isReceived").equals("false"))        //msg5 mean isReceived
                                            {
                                                retrofit2.Call<MessageResult> call1 = iMyService.setReceived(j1.getString("id"));
                                                call1.enqueue(new Callback<MessageResult>() {
                                                    @Override
                                                    public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                                        Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                                        Toast.makeText(GroupChat.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }*/
                                            msg_received = true;
                                            myDb.addMessage(jj1.getString("data"), jj1.getString("key_1"), jj1.getString("key_2"));
                                            String decryptedMessage = SecurityCaller.doDecryption(list);
                                            System.out.println("check" + check_online);
                                            String timestamp = j1.getString("timestamp");
                                            timestamp = timestamp.substring(16, 25);

                                            String s = j1.getString("user_id");
                                            String[] split = s.split("_");
                                            String firstSubString = split[0];
                                            System.out.println("firstsubst" + firstSubString);
                                            addMessage(decryptedMessage, locationStateCountry, true, jj1.getString("data_type"), timestamp, firstSubString, j1.getBoolean("is_poll"), j1.getInt("agree"), j1.getInt("disagree"), j1.getInt("neutral"), j1.getString("id"));
                                            System.out.println("check printed" + check_online);
                                        } else if (jj1.getString("data_type").equals("FILES") || jj1.getString("data_type").equals("VIDEO") || jj1.getString("data_type").equals("IMAGE")) {
                                            String data = jj1.getString("key_1");
                                            //Log.d("FILES", data);
                                            /*if (j1.getString("isReceived").equals("false"))        //msg5 mean isReceived
                                            {
                                                //insetad of reference_id use msg_id later okk
                                                retrofit2.Call<MessageResult> call1 = iMyService.setReceived(j1.getString("id"));
                                                call1.enqueue(new Callback<MessageResult>() {
                                                    @Override
                                                    public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                                        Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                                        Toast.makeText(GroupChat.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //      below conditions to be executed and the message to be inserted in local database
                                            }*/
                                            String timestamp = j1.getString("timestamp");
                                            timestamp = timestamp.substring(16, 25);
                                            String s = j1.getString("user_id");
                                            String[] split = s.split("_");
                                            String firstSubString = split[0];
                                            System.out.println("firstsubst" + firstSubString);

                                            addMessage(data, locationStateCountry, true, jj1.getString("data_type"), timestamp, firstSubString, j1.getBoolean("is_poll"), j1.getInt("agree"), j1.getInt("disagree"), j1.getInt("neutral"), j1.getString("id"));

                                        }
                                    } else {
                                        JSONObject jj1 = j1.getJSONObject("msg");
                                        System.out.println(jj1);
                                        if (jj1.getString("data_type").equals("MESSAGE")) {
                                            Log.d("jj1", jj1.getString("data"));
                                            List<Object> list = new ArrayList<>();
                                            list.add(jj1.get("data"));
                                            list.add(jj1.get("key_1"));
                                            list.add(jj1.get("key_2"));
                                            String timestamp = j1.getString("timestamp");
                                            timestamp = timestamp.substring(16, 25);
                                            Log.d("Referecnce 1", j1.getString("id"));
                                            /*if (j1.getString("isReceived").equals("false"))        //msg5 mean isReceived
                                            {
                                                retrofit2.Call<MessageResult> call1 = iMyService.setReceived(j1.getString("id"));
                                                call1.enqueue(new Callback<MessageResult>() {
                                                    @Override
                                                    public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                                        Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                                        Toast.makeText(GroupChat.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                //      below conditions to be executed and the message to be inserted in local database
                                            }
*/
                                            msg_received = true;
                                            myDb.addMessage(jj1.getString("data"), jj1.getString("key_1"), jj1.getString("key_2"));
                                            String decryptedMessage = SecurityCaller.doDecryption(list);
                                            System.out.println("check" + check_online);
                                            String s = j1.getString("user_id");
                                            String[] split = s.split("_");
                                            String firstSubString = split[0];
                                            System.out.println("firstsubst" + firstSubString);
                                            addMessage(decryptedMessage, locationStateCountry, false, jj1.getString("data_type"), timestamp, firstSubString, j1.getBoolean("is_poll"), j1.getInt("agree"), j1.getInt("disagree"), j1.getInt("neutral"), j1.getString("id"));

                                            System.out.println("check printed" + check_online);
                                        } else if (jj1.getString("data_type").equals("FILES") || jj1.getString("data_type").equals("VIDEO") || jj1.getString("data_type").equals("IMAGE")) {
                                            String data = jj1.getString("key_1");
                                            Log.d("FILES", data);
  /*                                          if (j1.getString("isReceived").equals("false"))        //msg5 mean isReceived
                                            {
                                                //insetad of reference_id use msg_id later okk
                                                retrofit2.Call<MessageResult> call1 = iMyService.setReceived(j1.getString("id"));
                                                call1.enqueue(new Callback<MessageResult>() {
                                                    @Override
                                                    public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                                        Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                                                        Toast.makeText(GroupChat.this, "Failed setReceived", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //      below conditions to be executed and the message to be inserted in local database
                                            }
  */
                                            String timestamp = j1.getString("timestamp");
                                            timestamp = timestamp.substring(16, 25);
                                            String s = j1.getString("user_id");
                                            String[] split = s.split("_");
                                            String firstSubString = split[0];
                                            System.out.println("firstsubst" + firstSubString);
                                            addMessage(data, locationStateCountry, false, jj1.getString("data_type"), timestamp, firstSubString, j1.getBoolean("is_poll"), j1.getInt("agree"), j1.getInt("disagree"), j1.getInt("neutral"), j1.getString("id"));

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

                    if (MyApplicationContext.getGroupChatSocket(sender_id, group_id) == null) {
                        try {
                            IO.Options mOptions = new IO.Options();
                            mOptions.query = "group_id=" + group_id;
                            socket = IO.socket(Constants.Group_CHAT_SERVER_URI, mOptions);
                            //setHasOptionsMenu(true);
                            socket.connect();
                            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                                @Override
                                public void call(Object... args) {
                                }
                            });
                            JSONObject new_chat_connection = new JSONObject();
                            new_chat_connection.put("user_id", sender_id);
                            new_chat_connection.put("socket", socket);
                            new_chat_connection.put("group_id", group_id);
                            MyApplicationContext.setGroupChatSocket(new_chat_connection);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    } else {
                        socket = MyApplicationContext.getGroupChatSocket(sender_id, group_id);
                    }
                    System.out.println("SocketID:" + socket.id());
                    socket.on("message", handleIncomingMessages);
                    callButton = findViewById(R.id.call);
                    mAdapter = new GroupMessageAdapter(mMessages, GroupChat.this);
                    mInputMessageView = (EditText) findViewById(R.id.message_input);
                    profileimg = (CircleImageView) findViewById(R.id.call1);
                    mMessagesView = (RecyclerView) findViewById(R.id.messages);
                    videoCallView = (ImageView) findViewById(R.id.videocam);
                    mMessagesView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    mMessagesView.setAdapter(mAdapter);

                    callButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            callButtonClicked();
                            Toast.makeText(GroupChat.this, "Coming Up", Toast.LENGTH_SHORT).show();
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
//                            Intent videoCallIntent = new Intent(getApplicationContext(), CallingScreenActivity.class);
//                            startActivity(videoCallIntent);
                            Toast.makeText(GroupChat.this, "Coming Up", Toast.LENGTH_SHORT).show();
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
                                    geocoder = new Geocoder(GroupChat.this, Locale.getDefault());
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


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatpage_threedots, menu);
        return true;
    }

    private void displayPopupWindow(View view) {
        int[] loc_int = new int[2];
        PopupWindow popup = new PopupWindow(GroupChat.this);
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
        ImageView videobtn = (ImageView) layout.findViewById(R.id.location);
        ImageView audiobtn = (ImageView) layout.findViewById(R.id.audio);

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
                sendText.put("group_id", group_id);
                sendText.put("location", locationStateCountry);
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                sendText.put("time", currentTime);
                sendText.put("msg2", filename);
                sendText.put("msg3", " ");
                mesage = filename;
                sendText.put("msg1", encodeFileToBase64Binary);
                socket.emit("message", sendText, new Ack() {
                    @Override
                    public void call(Object... args) {
                        reference_id = String.valueOf(args[0]);
                        System.out.println("R2EGEE" + reference_id);
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                        retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                        call1.enqueue(new Callback<MessageResult>() {
                            @Override
                            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                System.out.println("mmesage" + mesage);
                                if (response.body().getResult().equals("true")) {
                                    System.out.println("IN");
                                    msg_sent = false;
                                    msg_received = true;          //message recevived by the receiver
                                    try {

                                        addMessage(mesage, null, true, sendText.getString("data_type"), currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    System.out.println("IN2354");
                                    msg_sent = true;        //message only delivered to remote database
                                    msg_received = false;
                                    try {
                                        addMessage(mesage, null, true, sendText.getString("data_type"), currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));
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
                                    addMessage(mesage, null, true, sendText.getString("data_type"), currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));
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
                    sendText.put("group_id", group_id);
                    sendText.put("location", locationStateCountry);
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    sendText.put("time", currentTime);
                    sendText.put("msg2", filename);
                    sendText.put("msg3", " ");
                    mesage = filename;
                    sendText.put("msg1", encodeFileToBase64Binary);
                    socket.emit("message", sendText, new Ack() {
                        @Override
                        public void call(Object... args) {
                            reference_id = String.valueOf(args[0]);
                            System.out.println("R2EGEE" + reference_id);
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                            call1.enqueue(new Callback<MessageResult>() {
                                @Override
                                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                    System.out.println("mmesage" + mesage);
                                    if (response.body().getResult().equals("true")) {
                                        System.out.println("IN");
                                        msg_sent = false;
                                        msg_received = true;          //message recevived by the receiver
                                        try {
                                            addMessage(mesage, null, true, sendText.getString("data_type"), currentTime, sendText.getString("sender_id"), sendText.getBoolean("is_poll"), sendText.getInt("agree"), sendText.getInt("disagree"), sendText.getInt("neutral"), sendText.getString("id"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        System.out.println("IN2354");
                                        msg_sent = true;        //message only delivered to remote database
                                        msg_received = false;
                                        try {

                                            addMessage(mesage, null, true, sendText.getString("data_type"), currentTime, sendText.getString("sender_id"), sendText.getBoolean("is_poll"), sendText.getInt("agree"), sendText.getInt("disagree"), sendText.getInt("neutral"), sendText.getString("id"));

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
                                        addMessage(mesage, null, true, sendText.getString("data_type"), currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));
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
            } else if (extension.equals(".mp4")) {
                try {
                    String encodeFileToBase64Binary = encodeFileToBase64Binary(file);
                    sendText.put("data_type", "VIDEO");
                    sendText.put("sender_id", sender_id);
                    sendText.put("group_id", group_id);
                    sendText.put("location", locationStateCountry);
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                    sendText.put("time", currentTime);
                    sendText.put("msg2", filename);
                    sendText.put("msg3", " ");
                    mesage = filename;
                    sendText.put("msg1", encodeFileToBase64Binary);
                    socket.emit("message", sendText, new Ack() {
                        @Override
                        public void call(Object... args) {
                            reference_id = String.valueOf(args[0]);
                            System.out.println("R2EGEE" + reference_id);
                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                            call1.enqueue(new Callback<MessageResult>() {
                                @Override
                                public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                                    System.out.println("mmesage" + mesage);
                                    if (response.body().getResult().equals("true")) {
                                        System.out.println("IN");
                                        msg_sent = false;
                                        msg_received = true;          //message recevived by the receiver
                                        try {

                                            addMessage(mesage, null, true, sendText.getString("data_type"), currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        System.out.println("IN2354");
                                        msg_sent = true;        //message only delivered to remote database
                                        msg_received = false;
                                        try {
                                            addMessage(mesage, null, true, sendText.getString("data_type"), currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));

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
                                        addMessage(mesage, null, true, sendText.getString("data_type"), currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));

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
            } else {
                Toast.makeText(this, "Invalid Media File", Toast.LENGTH_SHORT).show();
                flag = 2;
            }
        }
    }

    public void displayFile(String filename) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename);
        // Uri uri=Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", file);
        String mime = getContentResolver().getType(uri);

        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
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
            sendText.put("group_id", group_id);
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            sendText.put("time", currentTime);
            myDb.addMessage(String.valueOf(list.get(0)), String.valueOf(list.get(1)), String.valueOf(list.get(2)));
            sendText.put("location", locationStateCountry);
            sendText.put("isSend", true);
            sendText.put("id", 1234);
            socket.emit("message", sendText, new Ack() {
                @Override
                public void call(Object... args) {

                    reference_id = String.valueOf(args[0]);
                    System.out.println("R2EGEE" + reference_id);
                    retrofit2.Call<MessageResult> call1 = iMyService.checkReceived(reference_id);
                    call1.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                            Log.d("qwerty1", String.valueOf(response.body().getResult()));
                            if (response.body().getResult().equals("true")) {
                                System.out.println("IN");
                                msg_sent = false;
                                msg_received = true;          //message recevived by the receiver
                                try {
                                    addMessage(message, locationStateCountry, true, "MESSAGE", currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("IN2354");
                                msg_sent = true;        //message only delivered to remote database
                                msg_received = false;
                                try {
                                    addMessage(message, locationStateCountry, true, "MESSAGE", currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            sendText = new JSONObject();
                        }

                        @Override
                        public void onFailure(retrofit2.Call<MessageResult> call, Throwable
                                t) {
                            System.out.println("fghjt" + t);
                            msg_sent = false;
                            msg_received = false;
                            try {

                                addMessage(message, locationStateCountry, true, "MESSAGE", currentTime, sendText.getString("sender_id"), false, 0, 0, 0, sendText.getString("id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    public void sendImage(String path) {
        try {
            mesage = encodeImage(path);
            sendText.put("data_type", "IMAGE");
            sendText.put("sender_id", sender_id);
            sendText.put("group_id", group_id);
            sendText.put("location", locationStateCountry);
            sendText.put("msg1", mesage);
            sendText.put("msg2", "");
            sendText.put("msg3", "");
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            sendText.put("time", currentTime);
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
        mMessages.add(new groupMessage.Builder(Message.TYPE_MESSAGE)
                .image(bmp).isSend(isSend).ispolled(false).upvote(0).downvote(0).neutral(0).build());
        mAdapter = new GroupMessageAdapter(mMessages);
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

    private void addMessage(String message, String loc, boolean isSend, String
            datatype, String time, String sender_id, boolean is_poll, int agree, int disagree,
                            int neutral, String Uniqueid) {
        mMessages.add(new groupMessage.Builder(Message.TYPE_MESSAGE)
                .message(message).location(loc).datatype(datatype).timestamp(time).isSend(isSend).sender_id(sender_id).username(sender_id).ispolled(is_poll).upvote(agree).downvote(disagree).neutral(neutral).uniqueId(Uniqueid).build());
        // mAdapter = new GroupMessageAdapter(mMessages);
        //mAdapter = new GroupMessageAdapter( mMessages);
        System.out.println("Sender_ID" + sender_id);
        mAdapter = new GroupMessageAdapter(mMessages);
        mAdapter.notifyItemInserted(0);
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
        userName = getIntent().getExtras().getString("UserName");
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You may now place a call", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This application needs permission to use your microphone to function properly.", Toast
                    .LENGTH_LONG).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Added Message to Vault", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.poll) {


            String meg_id = mMessages.get(mes_position).getUniqueId();
            String sender = mMessages.get(mes_position).getUsername();

            if (sender.equals(sender_id) || sender_id.equals(admin_id)) {

                retrofit2.Call<MessageResult> call1 = iMyService.setpoll(meg_id); //idha kya likhna hey dekh le
                call1.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                        System.out.println(response);
                        Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GroupChat.this, GroupChat.class);
                        intent.putExtra("id", group_id);
                        intent.putExtra("name", group_name);
                        intent.putExtra("username", userName);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadein);

                    }

                    @Override
                    public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {
                        Toast.makeText(GroupChat.this, t.toString(), Toast.LENGTH_SHORT).show();

                    }
                });

            } else {

                Toast.makeText(this, "Unauthorized Poll Creation", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GroupChat.this, GroupChat.class);
                intent.putExtra("id", group_id);
                intent.putExtra("name", group_name);
                intent.putExtra("username", userName);
                startActivity(intent);

            }
        }

        return true;
    }

    private void clearActionMode() {
        isInActionMode = false;
    }

    public boolean onLongClickListner(View v, int i) {
        mes_position = i;
        callButton.setVisibility(View.INVISIBLE);
        videoCallView.setVisibility(View.GONE);
        userText.setVisibility(View.INVISIBLE);
        profileimg.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.group_longclick_options);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        mAdapter.notifyDataSetChanged();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        return true;
    }

    public Boolean setUpVote(String uniqueid, TextView countView) {
        String[] count = new String[1];
        retrofit2.Call<MessageResult> call = iMyService.setagree(uniqueid, sender_id);
        call.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                count[0] = response.body().getResult();
                countView.setText(count[0]);
                Log.v("count", count[0]);
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });

        return true;
    }

    public Boolean setDownVote(String uniqueid, TextView countView) {
        String[] count = new String[1];
        retrofit2.Call<MessageResult> call = iMyService.setdisagree(uniqueid, sender_id);
        call.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                count[0] = response.body().getResult();
                countView.setText(count[0]);
                Log.v("count", count[0]);
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });

        return true;
    }

    public Boolean setNeutralVote(String uniqueid, TextView countView) {
        String[] count = new String[1];
        retrofit2.Call<MessageResult> call = iMyService.setneutral(uniqueid, sender_id);
        call.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                Toast.makeText(GroupChat.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                count[0] = response.body().getResult();
                countView.setText(count[0]);
                Log.v("count", count[0]);
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });

        return true;
    }

    public boolean hasActiveInternetConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(this, ThreetabSlider.class));
//    }
}
