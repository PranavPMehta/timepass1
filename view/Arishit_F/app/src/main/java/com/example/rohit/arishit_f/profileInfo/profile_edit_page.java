package com.example.rohit.arishit_f.profileInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.rohit.arishit_f.group_create.group_create_second_page;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class profile_edit_page extends AppCompatActivity {

    TextView cpass;
    private IMyService iMyService;
    private EditText userName;
    private EditText userid;
    private EditText useremail;
    private EditText userdesignation;
    private EditText usermobile;
    private ImageView change_image;
    private ImageView user_image;
    private Button save;
    private String user_name_val;
    private ProgressBar progressBar;
    public static final int IMAGE_PICK_CODE = 1000;
    public static final int PERMISSION_CODE = 1001;
    String uid;
    private String templink;
    private int flag_image_change=0;
    private String turl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_page);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        cpass =(TextView)findViewById(R.id.changepasswordButton);
        userdesignation = (EditText) findViewById(R.id.user_designation);
        useremail = (EditText) findViewById(R.id.emailEdittext);
        userid = (EditText) findViewById(R.id.user_id);
        usermobile = (EditText) findViewById(R.id.mobileEdittext);
        userName = (EditText) findViewById(R.id.fullnameEdittext);
        save = (Button) findViewById(R.id.savebutton);
        uid= getIntent().getStringExtra("uid"); //write user id
        change_image = (ImageView)findViewById(R.id.image_edit_botton);
        user_image = (ImageView)findViewById(R.id.circular_image);
        progressBar = (ProgressBar)findViewById(R.id.edit_progress);
        cpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cp = new Intent(profile_edit_page.this,change_password.class);
                cp.putExtra("uid",uid);
                startActivity(cp);
                finish();
            }
        });

        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
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

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


        Log.d("uid",uid);
        Call<MessageResult> getinfo = iMyService.getInfo(uid);

        getinfo.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                Log.d("info",response.body().getMessage());
                try {
                    JSONObject info =new JSONObject(response.body().getMessage());
                    System.out.println(info);
                    userName.setText(info.getString("Name"));
                    userid.setText(info.getString("user_org_id"));
                    useremail.setText(info.getString("Email"));
                    userdesignation.setText(info.getString("Designation"));
                    usermobile.setText(info.getString("Mobile"));
                    user_name_val = userName.getText().toString();
                    JSONObject tobj = new JSONObject(info.getString("display_picture"));
                    turl = tobj.getString("url");

                    Log.v("turl:",turl);

                    if(!turl.equals("null")) {
                                progressBar.setVisibility(View.VISIBLE);
                                Glide.with(profile_edit_page.this)
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
                                        .into(user_image);
                    }else{
                        user_image.setImageResource(R.drawable.profile_img_new);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String final_url;
                if(flag_image_change == 1){
                    final_url = templink;
                    Log.v("flag","flag_image: 1 " );
                    Log.v("final_url",final_url);
                }else{
                    final_url = turl;
                    Log.v("flag","flag_image: 0 " );
                    Log.v("final_url",final_url);
                }

                Call<MessageResult> modifyinfo = iMyService.modifyInfo(uid,userName.getText().toString(),userid.getText().toString(),userdesignation.getText().toString(),useremail.getText().toString(),usermobile.getText().toString(),final_url);
                modifyinfo.enqueue(new Callback<MessageResult>() {

                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        Log.d("info",response.body().getMessage());
                        Toast.makeText(profile_edit_page.this,response.body().getMessage() , Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(profile_edit_page.this,profile_info.class);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        Toast.makeText(profile_edit_page.this,"Some Error Occured" , Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });




    }

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            progressBar.setVisibility(View.VISIBLE);

            File file = new File(getRealPathFromURI(this,data.getData()));

            uploadImage(file);
            Log.v("file",file.toString());
            flag_image_change = 1;


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
                createPartFromString(uid),
                prepareFilePart("userPhoto", file1)
        );

        uploadImage.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {

                Log.v("response",response.body().getMessage());
                templink = response.body().getMessage();
                Log.v("link",templink);

                Glide.with(profile_edit_page.this)
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
                        .into(user_image);

                Toast.makeText(profile_edit_page.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable throwable) {
                throwable.printStackTrace();
                Log.v("error",throwable.toString());
                Toast.makeText(profile_edit_page.this, "Error", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(this, profile_info.class));
    }

}


