package com.example.rohit.arishit_f.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.arishit_f.dashboard.ThreetabSlider;
import com.example.rohit.arishit_f.login.chat.Message;
import com.example.rohit.arishit_f.registration.otp.SendOtp;
import com.example.rohit.arishit_f.resetPassword.ForgotPassword;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.registration.RegisterPage;
import com.example.rohit.arishit_f.voiceCall.BaseActivity;
import com.example.rohit.arishit_f.voiceCall.SinchService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sinch.android.rtc.SinchError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginScreen extends BaseActivity implements SinchService.StartFailedListener {
    private static final int SIGNUP_REQUEST = 1;
    private static final int LOGIN_REQUEST = 1;
    MaterialEditText edit_login_email, edit_login_password;
    Button btn_login, btn_signup;
    public static String Username=null;
    Bitmap bitmap;
    String uuid;
    private TextView forgotPassword;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    SharedPreferences.Editor editor;

    SharedPreferences sharedPreferences1;


    private static  String token = "3";
    String Final_Token;



    //Push the tokens of the user group to be notified in this vector
    public Vector<String> registrationids = new Vector<>();

    public static int stringCompare(String str1, String str2) {
        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);

        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int) str1.charAt(i);
            int str2_ch = (int) str2.charAt(i);

            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }

        // Edge case for strings like
        // String 1="Geeks" and String 2="Geeksforgeeks"
        if (l1 != l2) {
            return l1 - l2;
        }

        // If none of the above conditions is true,
        // it implies both the strings are equal
        else {
            return 0;
        }
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sharedPreferences1 = getSharedPreferences("userDetails", MODE_PRIVATE);
        editor = sharedPreferences1 .edit();

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        edit_login_email = (MaterialEditText) findViewById(R.id.edit_uid);
        edit_login_password = (MaterialEditText) findViewById(R.id.edit_password);
        forgotPassword = findViewById(R.id.forgotPasswordText);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, ForgotPassword.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Forgot password",Toast.LENGTH_SHORT).show();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> Task) {
                if(Task.isSuccessful()){
                    token = Task.getResult().getToken();
                    Log.d("Token ", token);
                    System.out.println("Notification_token"+token);
                    Final_Token = token;
                }
            }
        });


        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, ProfilePage.class);
                startActivityForResult(intent, LOGIN_REQUEST);*/
                if (TextUtils.isEmpty(edit_login_email.getText())) {
                    Toast.makeText(getApplicationContext(), "Email id cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edit_login_password.getText())) {
                    Toast.makeText(getApplicationContext(), "Please enter the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Username=edit_login_email.getText().toString();

                Call<MessageResult> call2=iMyService.checkLogin(Username);
                call2.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        String res=response.body().getResult();
                        System.out.println("Response"+res);
                        if(res.equals("false"))
                        {
                            System.out.println("Not Logged in ChatPage");
                            loginUser(edit_login_email.getText().toString(),
                                    edit_login_password.getText().toString());
                        }
                        else if(res.equals("true"))
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
                            String bearerToken = sharedPreferences.getString("bearer_token","12345");
                            System.out.println("Bearer token in chat"+bearerToken);
                            Call<MessageResult> call3 = iMyService.getMyDetails("Bearer "+bearerToken);
                            call3.enqueue(new Callback<MessageResult>() {
                                @Override
                                public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                                    System.out.println("Response "+response.body().getMessage());
                                    try {
                                        if(!response.body().getMessage().equals("error"))
                                        {
                                            JSONObject user_detail_json=new JSONObject(response.body().getMessage());
                                            if(user_detail_json.getString("token").equals(bearerToken))
                                            {
                                                System.out.println("True Token MAtched");
                                                loginUser(edit_login_email.getText().toString(),
                                                        edit_login_password.getText().toString());
                                            }
                                            else
                                            {
                                                Toast.makeText(LoginScreen.this, "Same User Login", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(LoginScreen.this, "Same User Login", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MessageResult> call, Throwable t) {

                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(LoginScreen.this, "User does not exist", Toast.LENGTH_SHORT).show();
                            editor.putString("ishoney","yes");
                            //editor.putString("bearer_token","123");
                            editor.commit();
                            System.out.println("login Screen honey");
                            Intent intent = new Intent(LoginScreen.this, ThreetabSlider.class);
                            startActivityForResult(intent, LOGIN_REQUEST);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {

                    }
                });

            }
        });

        ImageButton faceRecognition = findViewById(R.id.face_button);
        faceRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
                uuid = sharedPreferences.getString("UID", "");
                Log.d("User", "onClick: "+uuid);
                imgpicker();
            }
        });

        Button submit = findViewById(R.id.signup_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, RegisterPage.class);
                startActivityForResult(intent, SIGNUP_REQUEST);

            }
        });
    }

    private void loginUser(String email, String password) {
        compositeDisposable.add(iMyService.loginUser(email, password,Final_Token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        JSONObject j = (JSONObject) new JSONObject(s);
                        Log.d("s",s);
                        String response = j.getString("message");
                        String result = j.getString("result");
                        String verified = j.getString("verified");
                        String token = j.getString("token");
                        Toast.makeText(LoginScreen.this, "" + response, Toast.LENGTH_SHORT).show();
                        if (result.equals("true") && verified.equals("true")) {
                            System.out.println("Token  " + token);
                            editor.putString("bearer_token",token);
                            editor.putString("ishoney","no");
                            editor.commit();
                            Toast.makeText(LoginScreen.this, "Token "+token, Toast.LENGTH_SHORT).show();
                            System.out.println("Token  " + sharedPreferences1.getString("bearer_token","12345"));

                            loginClicked(email);
                            Intent intent = new Intent(LoginScreen.this, ThreetabSlider.class);
                            intent.putExtra("username",Username);
                            intent.putExtra("ishoney","no");
                            startActivityForResult(intent, LOGIN_REQUEST);
                            finish();

                        }
                        else if (result.equals("true") && verified.equals("false")) {
                            editor.putString("ishoney", "no");
                            editor.commit();
                            Intent intent = new Intent(LoginScreen.this, SendOtp.class);
                            startActivityForResult(intent, LOGIN_REQUEST);
                            finish();
                        }
                        else if(result.equals("false")){
                            editor.putString("ishoney","yes");
                            //editor.putString("bearer_token","123");
                            editor.commit();
                            System.out.println("login Screen honey");
                            Intent intent = new Intent(LoginScreen.this, ThreetabSlider.class);
                            intent.putExtra("username",Username);
                            intent.putExtra("ishoney","yes");
                            startActivityForResult(intent, LOGIN_REQUEST);
                            finish();
                        }
                        else if(result.equals("Same"))
                        {
                            //Toast.makeText(LoginScreen.this, "Same User Login Exception", Toast.LENGTH_SHORT).show();

                        }
                    }
                }));
    }

    //Face Recognition
    public void imgpicker(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            startActivityForResult(takePictureIntent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.png");
            try {
                FileOutputStream fout = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 85, fout);
                fout.flush();
                fout.close();
                upload(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void upload(File file){
        //Create a file object using file path
        // Create a request body with file and image media type
        Retrofit retrofit = RetrofitClient.getRetrofitClient(this);
        IMyService iMyService1 = retrofit.create(IMyService.class);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call<MessageResult> uploadImage = iMyService1.uploadImage(part, description);
        uploadImage.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                Log.d("my", response.body().getResult());
                Toast.makeText(LoginScreen.this, "" + response.body().getResult(), Toast.LENGTH_SHORT).show();
                if(response.body().getResult().equals(uuid)) {
                    Intent checkanimation = new Intent(LoginScreen.this, ThreetabSlider.class);
                    startActivity(checkanimation);
                }
                else{
                    Intent startover = new Intent(LoginScreen.this,LoginScreen.class);
                    startActivity(startover);
                }
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(LoginScreen.this, "Error", Toast.LENGTH_SHORT).show();
                Intent startover = new Intent(LoginScreen.this,RegisterPage.class);
                startActivity(startover);
            }
        });

    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
        Log.d("login", "onServiceConnected: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        Log.d("login f", "onStartFailed: ");
    }

    @Override
    public void onStarted() {
        Log.d("start", "onStarted: ");
        openPlaceCallActivity();
    }

    private void loginClicked(String userName) {

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!userName.equals(getSinchServiceInterface().getUserName())) {
            System.out.println("Stop Sinch");
            getSinchServiceInterface().stopClient();
        }

        if (!getSinchServiceInterface().isStarted()) {
            System.out.println("Starting Sinch");
            getSinchServiceInterface().startClient(userName);
        } else {
            System.out.println("Started Sinch");
            openPlaceCallActivity();
        }
    }

    private void openPlaceCallActivity() {
        //System.out.println("Place Call");
        Toast.makeText(this, "Place Call", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        System.out.println("In Destroy Login Acivity");
        super.onDestroy();
    }
}
