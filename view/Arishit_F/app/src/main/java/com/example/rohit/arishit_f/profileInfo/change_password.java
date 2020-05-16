package com.example.rohit.arishit_f.profileInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.LoginScreen;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.registration.otp.SendOtp;
import com.example.rohit.arishit_f.registration.otp.VerifyOtp;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.example.rohit.arishit_f.registration.PasswordStrength;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;


public class change_password extends AppCompatActivity {

    private IMyService iMyService;
    private String userId,email;
    private Button save,verifyOTP;
    private MaterialEditText previousPassword;
    private MaterialEditText newPassword;
    private MaterialEditText confirmPassword,EnterOTP;
    private int streangth;
    private int StreangthFlag=0;
    private int PreviousshowPasswordFlag=0;
    private int newshowPasswordFlag=0;
    private int confirmshowPasswordFlag=0;
    private Button getOTP;
    private int flag_save_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        save = (Button) findViewById(R.id.savebutton);
        previousPassword = (MaterialEditText) findViewById(R.id.currentpass);
        newPassword = (MaterialEditText)findViewById(R.id.newpass);
        confirmPassword = (MaterialEditText) findViewById(R.id.confirmpass);
        EnterOTP = (MaterialEditText) findViewById(R.id.otpEdittext);
        getOTP = (Button) findViewById(R.id.otptext);
        verifyOTP = (Button) findViewById(R.id.verifyOTP);

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
                    userId = info.getString("user_id");
                    Log.v("userid",userId);
                    email = info.getString("Email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MessageResult> call, Throwable t) {

            }
        });

        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(previousPassword.getText().toString())) {
                    previousPassword.setError("Please enter the Password");
                    return;
                } else if (TextUtils.isEmpty(newPassword.getText().toString())) {
                    newPassword.setError("Please enter the Password");
                    return;

                } else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {
                    confirmPassword.setError("Please enter the Password");
                    return;
                } else if (StreangthFlag == 0) {
                    return;
                } else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                    confirmPassword.setError("Password is different");
                    return;
                } else {
                    Call<MessageResult> check = iMyService.checkPreviousPassword(userId, previousPassword.getText().toString(), newPassword.getText().toString());

                    check.enqueue(new Callback<MessageResult>() {

                        @Override
                        public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                            Log.d("info", response.body().getMessage());
                            if(response.body().getMessage().equals("wrong previous password")){
                                Toast.makeText(change_password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }else{
                                Call<MessageResult> requestOtp = iMyService.requestEmailOtp(userId, email);
                                requestOtp.enqueue(new Callback<MessageResult>() {
                                    @Override
                                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                                        Toast.makeText(change_password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        if (response.body().getMessage().equals("OTP Send Successfully")) {
                                            verifyOTP.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MessageResult> call, Throwable t) {
                                        t.printStackTrace();
                                        Toast.makeText(change_password.this, "Error in sending OTP!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }

                        @Override
                        public void onFailure(Call<MessageResult> call, Throwable t) {
                            Toast.makeText(change_password.this, "Some Error Occured", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(EnterOTP.getText().toString())){
                    EnterOTP.setError("Please Enter the OTP");
                    return;
                }else if(EnterOTP.length() != 6) {
                    EnterOTP.setError("Please Enter valid OTP");
                    return;
                }else{
                    Call<MessageResult> verifyOtp = iMyService.verifyEmailOtp(userId, EnterOTP.getText().toString());
                    verifyOtp.enqueue(new Callback<MessageResult>() {
                        @Override
                        public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                            String response_result = response.body().getResult();
                            if (response_result.equals("true")) {
                                save.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(change_password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResult> call, Throwable t) {
                            Toast.makeText(change_password.this, "Error in Verifying OTP", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                }

            }
        });

        previousPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (previousPassword.getRight() - previousPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if(PreviousshowPasswordFlag==1){
                            previousPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            previousPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_black_24dp, 0);
                            PreviousshowPasswordFlag=0;
                        }
                        else{
                            previousPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            previousPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            PreviousshowPasswordFlag=1;
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        newPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (newPassword.getRight() - newPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if(newshowPasswordFlag==1){
                            newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_black_24dp, 0);
                            newshowPasswordFlag=0;
                        }
                        else{
                            newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            newPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            newshowPasswordFlag=1;
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        confirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (confirmPassword.getRight() - confirmPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if(confirmshowPasswordFlag==1){
                            confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_black_24dp, 0);
                            confirmshowPasswordFlag=0;
                        }
                        else{
                            confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            confirmPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            confirmshowPasswordFlag=1;
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                streangth=updatePasswordStrengthView(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

                    if(streangth==1){
                        newPassword.setError("Minimum 8 characters with atleast 1 Uppercase, 1 Lowercase, 1 numeric & 1 special character");
                        newPassword.setErrorColor(getColor(R.color.weak_error));
                        StreangthFlag=0;
                        return;

                    }else if(streangth==2){
                        newPassword.setError("Minimum 8 characters with atleast 1 Uppercase, 1 Lowercase, 1 numeric & 1 special character");
                        newPassword.setErrorColor(getColor(R.color.medium_error));
                        StreangthFlag=0;
                        return;
                    }else if(streangth==3){
                        newPassword.setError("Strong");
                        newPassword.setErrorColor(getColor(R.color.strong_error));
                        StreangthFlag=1;
                    }else if(streangth==4){
                        newPassword.setError("Very Strong");
                        newPassword.setErrorColor(getColor(R.color.vstrong_error));
                        StreangthFlag=1;
                    }


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        Call<MessageResult> changep = iMyService.changePassword(userId, previousPassword.getText().toString(), newPassword.getText().toString());

                        changep.enqueue(new Callback<MessageResult>() {

                            @Override
                            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                                Log.d("info", response.body().getMessage());
                                if(response.body().getMessage().equals("wrong previous password")){
                                    Toast.makeText(change_password.this, "Verification details are incorrect", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(change_password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(change_password.this, profile_info.class);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onFailure(Call<MessageResult> call, Throwable t) {
                                Toast.makeText(change_password.this, "Some Error Occured", Toast.LENGTH_SHORT).show();

                            }
                        });
                }
        });


    }

    private int updatePasswordStrengthView(String password) {
        int stre=0;
        PasswordStrength str = PasswordStrength.calculateStrength(password);
        if (str.getText(this).equals("Weak")) {
            stre=1;
            return stre;
        } else if (str.getText(this).equals("Medium")) {
            stre=2;
            return stre;
        } else if (str.getText(this).equals("Strong")) {
            stre=3;
            return stre;
        } else {
            stre=4;
            return stre;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,profile_edit_page.class);
        i.putExtra("uid",userId);
        startActivity(i);

    }
}
