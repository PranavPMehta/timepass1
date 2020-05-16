package com.example.rohit.arishit_f.facialRecognition;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rohit.arishit_f.constants.Constants;
import com.example.rohit.arishit_f.login.CheckAnimation;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.registration.RegisterPage;
import com.example.rohit.arishit_f.registration.otp.SendOtp;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.example.rohit.arishit_f.screenSharing.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.rohit.arishit_f.retrofit.IMyService.IP;

public class FacialImageUpload extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int CHATPAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 123;
    Bitmap bitmap;
    Button mCaptureBtn,nextbtn;
    ImageView mImageView;
    Uri image_uri;
    private String mImageFileLocation;
    IMyService iMyService;
    String username;
    int i;
    ProgressDialog pd;
    ArrayList<Uri> fileUris = new ArrayList<Uri>();
    java.util.List<java.io.File> files = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facial_image_upload);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        mImageView = findViewById(R.id.profile_img_fiu);
        mCaptureBtn = findViewById(R.id.capture_btn_fiu);
        nextbtn = findViewById(R.id.finish_button_fiu);
        username = RegisterPage.reg_username;
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(FacialImageUpload.this);
                pd.setMessage("loading");
                pd.show();
                Toast.makeText(getApplicationContext(),"Thank You",Toast.LENGTH_SHORT).show();
                imgpicker(0);
                nextbtn.setVisibility(View.VISIBLE);

            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(files.get(0),files.get(1), files.get(2));
                uploadval(files.get(3));
                Intent chat = new Intent(FacialImageUpload.this, SendOtp.class);
                startActivity(chat);
            }
        });

    }

    public void imgpicker(int a){


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        for (int i = 0; i < 4; i++) {
            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT, "720000");
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 2);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int count = 1;
        if(requestCode==1 && resultCode == RESULT_OK && data != null){
            ClipData clipData = data.getClipData();
            for(int i=0; i< clipData.getItemCount(); i++){
                ClipData.Item item = clipData.getItemAt(i);
                Uri uri = item.getUri();
                fileUris.add(uri);

            }


        }

        else if(requestCode==2 && resultCode == RESULT_OK && data != null){

            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp"+ UUID.randomUUID().toString() + ".png");
            Log.d("count", "onActivityResult: "+UUID.randomUUID().toString());
            try {
                FileOutputStream fout = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, fout);
                files.add(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            count++;

        }
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString){
        return RequestBody.create(
                MultipartBody.FORM, descriptionString
        );
    }

    @NonNull
    private  MultipartBody.Part prepareFilePart(String partName, File fileUri){
        //File file = FileUtils.getFile(this, fileUri);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),fileUri);

        return MultipartBody.Part.createFormData(partName, fileUri.getName(), requestFile);
    }

    public void upload(File file1, File file2, File file3){

        //SharedPreferences sharedPreferences = getSharedPreferences()

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.IP_ADDRESS+":5000")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        IMyService client = retrofit.create(IMyService.class);

        //
        Call<ResponseBody> uploadTrainImage = client.uploadTrainImage(
                createPartFromString(username),
                prepareFilePart("userPhoto",file1),
                prepareFilePart("userPhoto", file2),
                prepareFilePart("userPhoto", file3)
        );

        uploadTrainImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Uploaded Successfully ", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(FacialImageUpload.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void uploadval(File file4){
        //SharedPreferences sharedPreferences = getSharedPreferences()

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.IP_ADDRESS+":5001")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        IMyService client = retrofit.create(IMyService.class);

        Call<ResponseBody> uploadValImage = client.uploadValImage(
                createPartFromString(username),
                prepareFilePart("userPhoto",file4)
        );
        uploadValImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                }

                Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(FacialImageUpload.this, "Error", Toast.LENGTH_SHORT).show();
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