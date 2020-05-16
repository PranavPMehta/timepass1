package com.example.rohit.arishit_f.group_create;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.dashboard.ThreetabSlider;
import com.example.rohit.arishit_f.group_view.group_view;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
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
import static com.example.rohit.arishit_f.group_create.group_create_main.checked_contact;
import static java.sql.DriverManager.println;

public class group_create_second_page extends AppCompatActivity {

    private IMyService iMyService;
    private MaterialEditText group_name;
    private MaterialEditText group_objective;
    private ImageView group_image;
    private Button create;
    private String group_id;
    private String ts;
    private JSONArray members;
    private String admin;
    private String sender_user_id;
    public static final int IMAGE_PICK_CODE = 1000;
    public static final int PERMISSION_CODE = 1001;
    private ProgressBar progressBar;
    private String group_name_val;
    private String templink;
    private ImageView add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_second_page);

        sender_user_id = getIntent().getStringExtra("userID");
        admin = sender_user_id;
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        group_name = (MaterialEditText)findViewById(R.id.group_create_second_name);
        group_objective = (MaterialEditText)findViewById(R.id.group_create_second_objective);
        group_image = (ImageView)findViewById(R.id.group_create_second_groupImage);
        create = (Button)findViewById(R.id.group_create_second_create_button);
        progressBar = (ProgressBar)findViewById(R.id.group_create_second_progress);
        add = (ImageView)findViewById(R.id.group_create_second_groupImage_add);

        ts = String.valueOf(System.currentTimeMillis());
        Log.v("time"," "+ts);

        members = new JSONArray();
        JSONObject adminjo = new JSONObject();

        try {
            adminjo.put("user_id",admin);
        }catch (JSONException e){
            e.printStackTrace();
        }
        members.put(adminjo);

        for(int i=0;i<checked_contact.size();i++){
            JSONObject member = new JSONObject();
            try {
                member.put("user_id",checked_contact.get(i).getContact_id());
            }catch (JSONException e){
                e.printStackTrace();
            }
            members.put(member);
        }


        group_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CODE);
                } else {
                    group_name_val = group_name.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_PICK_CODE);

                }

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(group_name.getText().toString().equals("")){
                    group_name.setError("Enter group name");
                    return;
                }else if(group_objective.getText().toString().equals("")){
                    group_objective.setError("Enter group objective");
                    return;
                }

                group_name_val = group_name.getText().toString();

                String initials ="";
                String name[] = group_name_val.split(" ");
                for(int i = 0;i<name.length;i++){
                    String temp = name[i];
                    initials = initials + temp.charAt(0);
                }
                println(initials); //PP
                group_id = initials+"_"+ts;


                Call<MessageResult> createGroup = iMyService.createGroup(group_id,group_name.getText().toString(),templink,group_objective.getText().toString(),members,admin,ts);

                createGroup.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        Toast.makeText(group_create_second_page.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(group_create_second_page.this, ThreetabSlider.class);
                        startActivity(home);
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {
                        Toast.makeText(group_create_second_page.this, t.toString(), Toast.LENGTH_SHORT).show();
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
                templink = response.body().getMessage();
                Log.v("link",templink);
                        group_image.setBackgroundResource(0);
                        Glide.with(group_create_second_page.this)
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
                                        add.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(group_image);

                Toast.makeText(group_create_second_page.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable throwable) {
                throwable.printStackTrace();
                Log.v("error",throwable.toString());
                Toast.makeText(group_create_second_page.this, "Error", Toast.LENGTH_SHORT).show();
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

}
