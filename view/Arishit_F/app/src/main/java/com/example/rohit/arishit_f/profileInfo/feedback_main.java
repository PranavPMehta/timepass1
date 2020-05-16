package com.example.rohit.arishit_f.profileInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class feedback_main extends AppCompatActivity implements View.OnClickListener {

    int IMAGE_GALLERY_REQUEST;

    String user_id;
    Button submit;
    EditText feedbackEditText;

    String like_application;
    String like_features;
    String suggestions;

    ImageView completely_happy1;
    ImageView happy1;
    ImageView neutral1;
    ImageView completely_dissatisfied1;
    ImageView completely_happy2;
    ImageView happy2;
    ImageView neutral2;
    ImageView completely_dissatisfied2;

    ImageView screenshot;
    ImageView crossMark;

    Drawable highlight;

    File pictureDirectory;
    String pictureDirectoryPath;
    Uri data;
    Uri imageUri;
    File filePath;
    InputStream inputStream;
    Bitmap image;
    String feedbackImage;

    // retrofit2 classes
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_main);

        submit = (Button) findViewById(R.id.button);
        feedbackEditText = (EditText) findViewById(R.id.feedbackEditText);

        completely_happy1=(ImageView) findViewById(R.id.completely_happy1);
        happy1 = (ImageView) findViewById(R.id.happy1);
        neutral1 = (ImageView) findViewById(R.id.neutral1);
        completely_dissatisfied1 = (ImageView) findViewById(R.id.completely_dissatisfied1);

        completely_happy1.setOnClickListener(this);
        happy1.setOnClickListener(this);
        neutral1.setOnClickListener(this);
        completely_dissatisfied1.setOnClickListener(this);


        completely_happy2=(ImageView) findViewById(R.id.completely_happy2);
        happy2 = (ImageView) findViewById(R.id.happy2);
        neutral2 = (ImageView) findViewById(R.id.neutral2);
        completely_dissatisfied2 = (ImageView) findViewById(R.id.completely_dissatisfied2);

        completely_happy2.setOnClickListener(this);
        happy2.setOnClickListener(this);
        neutral2.setOnClickListener(this);
        completely_dissatisfied2.setOnClickListener(this);

        highlight = getResources().getDrawable(R.drawable.highlight_when_clicked);

        IMAGE_GALLERY_REQUEST=20;

        // screenshot image
        screenshot = (ImageView) findViewById(R.id.screenshotImage);

        // creating object of another class
        screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // invoke the image gallery using an implicit intent
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                // where do we want to find the data?
                pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                pictureDirectoryPath =pictureDirectory.getPath();

                // finally get URI representation
                data = Uri.parse(pictureDirectory.toString());

                // set the data and type
                photoPickerIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
            }
        });

        // crossMark beside screenshot image
        crossMark = (ImageView) findViewById(R.id.crossMark);
        crossMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackImage = null;
                crossMark.setVisibility(View.INVISIBLE);
                screenshot.setImageResource(R.drawable.screenshot_image);
            }
        });

        //Init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String bearerToken = sharedPreferences.getString("bearer_token","12345");

        retrofit2.Call<MessageResult> call = iMyService.getMyDetails("Bearer "+bearerToken);
        call.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(retrofit2.Call<MessageResult> call, Response<MessageResult> response) {
                System.out.println(response.body().getMessage());
                JSONObject info = null;
                try {
                    info = new JSONObject(response.body().getMessage());
                    user_id = info.getString("user_id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });



        // click submit button to save data
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestions = feedbackEditText.getText().toString();
                saveDataToDatabase(like_application, like_features, suggestions, feedbackImage, user_id);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            // if we are here everything processed successfully
            if(requestCode == IMAGE_GALLERY_REQUEST) {
                // if we are here we are hearing back from the gallery

                // the address of the image on the SD card
                imageUri = data.getData();
                filePath = new File(imageUri.getPath());

                //declare a stream to read the imagedata from the sd card
                // InputStream inputStream;         Already declared in the above class

                // we are getting an inputStream, based on the URI of the image
                try{
                    inputStream = getContentResolver().openInputStream(imageUri);

                    //Get a bitmap from the stream
                    image = BitmapFactory.decodeStream(inputStream);

                    // set image at that icon
                    screenshot.setImageBitmap(image);
                    screenshot.buildDrawingCache();
                    Bitmap bm = screenshot.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);         // bm is the bitmap object
                    byte[] b= baos.toByteArray();

                    feedbackImage = Base64.encodeToString(b, Base64.DEFAULT);

                    // set the crossmark beside the screenshot image to visible
                    crossMark.setVisibility(View.VISIBLE);


                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                    // show that the image is unavailable
                    Toast.makeText(this, "Image is Unavailable", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void saveDataToDatabase(String like_application, String like_features, String suggestions, String feedbackImage, String user_id) {
        compositeDisposable.add(iMyService.saveData(like_application, like_features, suggestions, feedbackImage, user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String result = jsonObject.getString("result");
                        if(result.equals("true")) {
                            Toast.makeText(feedback_main.this, "" + message, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(feedback_main.this, "" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.completely_happy1: {
                like_application = "Satisfied";
                completely_happy1.setBackground(highlight);
                happy1.setBackground(null);
                neutral1.setBackground(null);
                completely_dissatisfied1.setBackground(null);
                break;
            }
            case R.id.happy1: {
                like_application = "Happy";
                completely_happy1.setBackground(null);
                happy1.setBackground(highlight);
                neutral1.setBackground(null);
                completely_dissatisfied1.setBackground(null);
                break;
            }
            case R.id.neutral1: {
                like_application = "Fine";
                completely_happy1.setBackground(null);
                happy1.setBackground(null);
                neutral1.setBackground(highlight);
                completely_dissatisfied1.setBackground(null);
                break;
            }
            case R.id.completely_dissatisfied1: {
                like_application = "Not Good";
                completely_happy1.setBackground(null);
                happy1.setBackground(null);
                neutral1.setBackground(null);
                completely_dissatisfied1.setBackground(highlight);
                break;
            }
            case R.id.completely_happy2: {
                like_features = "Satisfied";
                completely_happy2.setBackground(highlight);
                happy2.setBackground(null);
                neutral2.setBackground(null);
                completely_dissatisfied2.setBackground(null);
                break;
            }
            case R.id.happy2: {
                like_features = "Happy";
                completely_happy2.setBackground(null);
                happy2.setBackground(highlight);
                neutral2.setBackground(null);
                completely_dissatisfied2.setBackground(null);
                break;
            }
            case R.id.neutral2: {
                like_features = "Fine";
                completely_happy2.setBackground(null);
                happy2.setBackground(null);
                neutral2.setBackground(highlight);
                completely_dissatisfied2.setBackground(null);
                break;
            }
            case R.id.completely_dissatisfied2: {
                like_features="Not Good";
                completely_happy2.setBackground(null);
                happy2.setBackground(null);
                neutral2.setBackground(null);
                completely_dissatisfied2.setBackground(highlight);
                break;
            }
        }

    }
}

