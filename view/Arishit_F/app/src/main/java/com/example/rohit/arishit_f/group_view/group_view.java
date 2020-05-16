package com.example.rohit.arishit_f.group_view;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.group_view.group_add_member;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.login.chat.GroupChat;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class group_view extends AppCompatActivity {

    private IMyService iMyService;
    private TextView group_name;
    private TextView group_objective;
    private String filepath = " ";
    private ImageView group_image;
    private ImageView group_image_edit;
    private ImageView group_name_edit;
    private ImageView group_objective_edit;
    private ImageView group_member_edit;
    public static final int IMAGE_PICK_CODE = 1000;
    public static final int PERMISSION_CODE = 1001;
    private String group_id;
    private String group_name_val;
    private String link;
    private ProgressBar progressBar;
    String admin;
    String sender_user_id;
    public static ArrayList<group_contacts> contact;
    ListView ls;
    String name;
    String designation;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        ls = (ListView) findViewById(R.id.group_listview);
        group_name = (TextView) findViewById(R.id.Group_name);
        group_objective = (TextView) findViewById(R.id.Group_Objective);
        group_image = (ImageView) findViewById(R.id.group_image);
        group_image_edit = (ImageView) findViewById(R.id.group_image_edit);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        group_name_edit = (ImageView) findViewById(R.id.group_name_edit);
        group_member_edit = (ImageView) findViewById(R.id.group_member_edit);


        group_image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CODE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_PICK_CODE);

                }

            }
        });

        group_name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(group_view.this,group_info_edit.class);
                i.putExtra("group_id",group_id);
                startActivity(i);
            }
        });

        group_member_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alret = new AlertDialog.Builder(group_view.this);
                View mview = getLayoutInflater().inflate(R.layout.activity_group_member_edit_popup,null);
                Button addmem = (Button)mview.findViewById(R.id.group_edit_add_member);
                Button removemem = (Button)mview.findViewById(R.id.group_edit_remove_member);

                removemem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(group_view.this,remove_member_group.class);
                        i.putExtra("group_id",group_id);
                        startActivity(i);
                    }
                });
                addmem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(group_view.this, group_add_member.class);
                        i.putExtra("group_id",group_id);
                        startActivity(i);
                    }
                });

                alret.setView(mview);
                AlertDialog dialog = alret.create();
                dialog.show();
            }

        });

        fetch();


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    void fetch(){

        group_id = getIntent().getStringExtra("group_id");
        Log.v("group_id",group_id);


        contact = new ArrayList<group_contacts>();

        Call<MessageResult> getgrpinfo = iMyService.getGroupInfo(group_id);//write group_id here

        getgrpinfo.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                Log.d("info", response.body().getMessage());
                try {
                    JSONArray infoarr = new JSONArray(response.body().getMessage());
                    System.out.println(infoarr.get(0));
                    JSONObject info = (JSONObject) infoarr.get(0);
                    Log.v("name", info.getString("group_name"));
                    group_name.setText(info.getString("group_name"));
                    group_name_val = info.getString("group_name");
                    group_objective.setText(info.getString("objective"));
                    admin = info.getString("admin");
                    JSONObject url = new JSONObject(info.getString("display_picture"));
                    String turl = url.getString("url");
                    if(!turl.equals("null")) {
                        link  = IP_ADDRESS+":3000"+turl;
                        Log.v("link",link);
                        Glide.with(group_view.this)
                                .load(link)
                                .apply(
                                        new RequestOptions()
                                                .error(R.drawable.group_img_new)
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
                                .into(group_image);
                    }else{
                        group_image.setImageResource(R.drawable.group_img_new);
                    }

                    JSONArray member = (JSONArray) infoarr.get(1);
                    System.out.println(member);

                    for (int i = 0; i < member.length(); i++) {
                        JSONObject mem = member.getJSONObject(i);
                        JSONObject display_pic = new JSONObject(mem.getString("display_picture"));
                        contact.add(new group_contacts(mem.getString("Username"), mem.getString("Designation"),display_pic.getString("url"),mem.getString("user_id")));
                    }

                    group_contactsAdapter contactAdapter = new group_contactsAdapter(group_view.this, contact);
                    ls.setAdapter(contactAdapter);

                    SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
                    String bearerToken = sharedPreferences.getString("bearer_token", "12345");
                    Call<MessageResult> call1 = iMyService.getMyDetails("Bearer " + bearerToken);
                    call1.enqueue(new Callback<MessageResult>() {
                        @Override

                        public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                            JSONObject j1 = null;
                            try {
                                j1 = new JSONObject(response.body().getMessage());
                                sender_user_id = j1.getString("user_id");
                                Log.v("admin",admin);
                                Log.v("sender id",sender_user_id);

                                if(!admin.equals(sender_user_id)){
                                    group_image_edit.setVisibility(View.GONE);
                                    group_member_edit.setVisibility(View.GONE);
                                    group_name_edit.setVisibility(View.GONE);
                                }else{
                                    group_image_edit.setVisibility(View.VISIBLE);
                                    group_member_edit.setVisibility(View.VISIBLE);
                                    group_name_edit.setVisibility(View.VISIBLE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResult> call, Throwable t) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressBar.setVisibility(View.VISIBLE);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            File file = new File(getRealPathFromURI(this,data.getData()));

            uploadImage(file);
            Log.v("file",file.toString());

        }

    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString
        );
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, File fileUri) {
        //File file = FileUtils.getFile(this, fileUri);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), fileUri);

        return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestFile);
    }


    private void uploadImage(File file1) {

        //SharedPreferences sharedPreferences = getSharedPreferences()

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(IP_ADDRESS + ":3000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        IMyService client = retrofit.create(IMyService.class);


        Call<MessageResult> uploadImage = client.uploadImage(
                createPartFromString(group_name_val.replaceAll("\\s+", "_")),
                prepareFilePart("userPhoto", file1)
        );

        uploadImage.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {

                Log.v("response",response.body().getMessage());
                String templink = response.body().getMessage();
                Log.v("link",templink);
                Call<MessageResult> modify = iMyService.modifyGroup(group_id,group_name.getText().toString(),templink);

                modify.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {

                        Glide.with(group_view.this)
                                .load(IP_ADDRESS+":3000"+templink)
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
                                .into(group_image);

                        Toast.makeText(group_view.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        Toast.makeText(group_view.this, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable throwable) {
                throwable.printStackTrace();
                Log.v("error",throwable.toString());
                Toast.makeText(group_view.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String getRealPathFromURI(final Context context, final Uri uri) {


        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                // This is for checking Main Memory
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else {
                        return Environment.getExternalStorageDirectory() + "/";
                    }
                    // This is for checking SD Card
                } else {
                    return "storage" + "/" + docId.replace(":", "/");
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                String fileName = getFilePath(context, uri);
                if (fileName != null) {
                    return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                }

                String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    id = id.replaceFirst("raw:", "");
                    File file = new File(id);
                    if (file.exists())
                        return id;
                }

                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
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

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

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
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static String getFilePath(Context context, Uri uri) {

        Cursor cursor = null;
        final String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i =new Intent(group_view.this, GroupChat.class);
        i.putExtra("id",group_id);
        i.putExtra("name",group_name_val);
        startActivity(i);
    }
}