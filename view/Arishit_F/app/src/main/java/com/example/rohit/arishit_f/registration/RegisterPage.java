package com.example.rohit.arishit_f.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.dashboard.Contacts;
import com.example.rohit.arishit_f.dashboard.ContactsAdapter;
import com.example.rohit.arishit_f.facialRecognition.FacialImageUpload;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.login.chat.DatabaseAdapter;
import com.example.rohit.arishit_f.registration.otp.SendOtp;
import com.example.rohit.arishit_f.registration.otp.VerifyOtp;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.hbb20.CountryCodePicker;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.rohit.arishit_f.registration.PasswordStrength;

public class RegisterPage extends AppCompatActivity {
    private static final int PROGRESSBAR_REQUEST = 1;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    Button btn_register;
    private int showPasswordFlag = 0;
    private int StreangthFlag = 0;
    private CountryCodePicker cpp;
    public static String reg_username;
    ImageView show_password;
    MaterialEditText edit_login_email, edit_login_password, edit_name;
  //  String organization_name;
    MaterialEditText edt_register_password;
    String org_domain;
    private String IMEI = null;
    private String ts;
    private String Register_token;
    private int streangth;
    private String org_token;
    DatabaseAdapter myDb;
    private String RegisterToken;
    TextView already_signin;

    // this is for send otp page
    IMyService saveEncrypted;
    String userId = "123";
    String Mobile="";
    String Email="";
    String country_code="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        myDb=new DatabaseAdapter(this);
        //to get the permission for the IMEI access
        if (ContextCompat.checkSelfPermission(RegisterPage.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterPage.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }

        final MaterialEditText edt_register_email = (MaterialEditText) findViewById(R.id.edit_emailid);
        final MaterialEditText edt_register_name = (MaterialEditText) findViewById(R.id.edit_name);
        edt_register_password = (MaterialEditText) findViewById(R.id.edit_password);
        final MaterialEditText edt_register_username = (MaterialEditText) findViewById(R.id.edit_username);
        final MaterialEditText edt_register_mobile = (MaterialEditText) findViewById(R.id.edit_mobileno);
        final MaterialEditText edt_register_uid = (MaterialEditText) findViewById(R.id.edit_uid);
        final MaterialEditText edt_register_designation = (MaterialEditText) findViewById(R.id.edit_designation);
        final MaterialEditText edt_uid = (MaterialEditText) findViewById(R.id.edit_uid);
        final MaterialEditText edt_register_org_token = (MaterialEditText) findViewById(R.id.edit_org_token);
        already_signin = (TextView) findViewById(R.id.already_signin);

        already_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cpp = (CountryCodePicker) findViewById(R.id.cpp);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        edt_register_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                streangth = updatePasswordStrengthView(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (streangth == 1) {
                    edt_register_password.setError("Minimum 8 characters with atleast 1 Uppercase, 1 Lowercase, 1 Numeric and 1 Special character");
                    edt_register_password.setErrorColor(getColor(R.color.weak_error));
                    StreangthFlag = 0;
                    return;
                } else if (streangth == 2) {
                    edt_register_password.setError("Minimum 8 characters with atleast 1 Uppercase, 1 Lowercase, 1 Numeric and 1 Special character");
                    edt_register_password.setErrorColor(getColor(R.color.medium_error));
                    StreangthFlag = 0;
                    return;
                } else if (streangth == 3) {
                    edt_register_password.setError("Strong");
                    edt_register_password.setErrorColor(getColor(R.color.strong_error));
                    StreangthFlag = 1;
                } else if (streangth == 4) {
                    edt_register_password.setError("Very Strong");
                    edt_register_password.setErrorColor(getColor(R.color.vstrong_error));
                    StreangthFlag = 1;
                }
            }
        });

        edt_register_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidEmail(edt_register_email.getText().toString())) {
                    edt_register_email.setError("Your email is not valid");
                    return;
                }
            }
        });

        edt_register_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edt_register_password.getRight() - edt_register_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (showPasswordFlag == 1) {
                            edt_register_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            edt_register_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_black_24dp, 0);
                            showPasswordFlag = 0;
                        } else {
                            edt_register_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            edt_register_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            showPasswordFlag = 1;
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country_code = cpp.getSelectedCountryCode();
            }
        });


        Button register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Register button clicked", Toast.LENGTH_SHORT).show();
                 int register_flag=0;

                if (TextUtils.isEmpty(edt_register_name.getText().toString()) ) {
                    edt_register_name.setError("Please enter the Name");
                    return;
                }else if(!isValidName(edt_register_name.getText().toString())){
                    edt_register_name.setError("Name should contain only letters");
                    return;
                }else if (TextUtils.isEmpty(edt_register_email.getText().toString())) {
                    edt_register_email.setError("Email cannot be empty");
                    return;
                }else if(TextUtils.isEmpty(edt_register_mobile.getText().toString())){
                    edt_register_mobile.setError("Please enter the mobile number");
                    return;
                }else if (TextUtils.isEmpty(edt_register_password.getText().toString())) {
                    edt_register_password.setError("Please enter the password");
                    return;
                }else if(TextUtils.isEmpty(edt_register_uid.getText().toString())){
                    edt_register_uid.setError("Please enter the organization user ID");
                    return;
                }else if(TextUtils.isEmpty(edt_register_designation.getText().toString())){
                    edt_register_designation.setError("Please enter the designation");
                    return;
                }else if(StreangthFlag==0){
                    return;
                }else if (ContextCompat.checkSelfPermission(RegisterPage.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterPage.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
                    return;
                }else{
                    register_flag = 1;
                }

                if(register_flag == 1) {
                            //To get the IMEI number of the device
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    IMEI = telephonyManager.getImei();
                    if(IMEI == null){
                        IMEI = "123"; //IMEI for VD is supposed as 123
                        }
                    Log.v("IMEI"," "+IMEI);

                    //to get the current time in milliseconds
                    ts = String.valueOf(System.currentTimeMillis());
                    Log.v("time"," "+ts);

                }


                reg_username = edt_register_username.getText().toString();
                registerUser(edt_register_email.getText().toString(),
                        edt_register_name.getText().toString(),
                        edt_register_password.getText().toString(),
                        edt_register_username.getText().toString(),
                        "+"+country_code+edt_register_mobile.getText().toString(),
                        edt_register_designation.getText().toString(),
                        edt_uid.getText().toString(),
                        IMEI,ts,edt_register_org_token.getText().toString());

                SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UID",edt_register_username.getText().toString());
                editor.putString("MOBILE",edt_register_mobile.getText().toString());
                editor.putString("EMAIL",edt_register_email.getText().toString());
                editor.commit();

            }
        });

    }

    private void registerUser(String email, String name, String password, String username, String mobileno, String designation,  String uid,String imei,String ts,String org_token) {
        compositeDisposable.add(iMyService.registerUser(email, name, password,username,mobileno,designation, uid,imei,ts,org_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONObject j = (JSONObject) new JSONObject(s);
                        String x = j.getString("result");
                        RegisterToken = j.getString("message");

                        if(x.equals("ack")){
                            Register_token = j.getString("message");
                            myDb.insertToken(Register_token);
                            String[] tokenSplit = Register_token.split("_", 3);
                            String veriIMEI = tokenSplit[1];
                            Log.v("Recieved IMEI",tokenSplit[1]);
                            if(veriIMEI.equals(IMEI)|| veriIMEI.equals("123") ){    //IMEI for VD is NULL
                                Log.v("Result","Done with verification of IMEI...!");

                                Retrofit retrofitClient = RetrofitClient.getInstance();
                                iMyService = retrofitClient.create(IMyService.class);

                                Call<MessageResult> requestStore = iMyService.requestStore("true");

                                requestStore.enqueue(new Callback<MessageResult>() {
                                    @Override
                                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                                    /*
                                    Intent intent = new Intent(RegisterPage.this, ProfilePage.class);
                                    startActivityForResult(intent, PROGRESSBAR_REQUEST);*/

                                        if(response.body().getResult().equals("true")) {
                                            Toast.makeText(RegisterPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.v("Response with true",response.body().getMessage());

                                            try {
                                                RegisterToken = j.getString("message");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            // Switch to the Verify Page directly without going to the send otp page

                                            initialize();

                                            Call<MessageResult> requestOtp = saveEncrypted.requestOtp(userId,Email,Mobile);

                                            requestOtp.enqueue(new Callback<MessageResult>() {
                                                @Override
                                                public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
//                                                    Toast.makeText(RegisterPage.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(RegisterPage.this, VerifyOtp.class));
                                                    finish();
                                                }

                                                @Override
                                                public void onFailure(Call<MessageResult> call, Throwable t) {
                                                    t.printStackTrace();
                                                    Toast.makeText(RegisterPage.this, "Error in sending OTP!", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        }else{
                                            Toast.makeText(RegisterPage.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.v("Response with false", response.body().getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MessageResult> call, Throwable t) {
                                        Toast.makeText(RegisterPage.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                                        Log.v("Failure","Registration unsuccessful");
                                    }
                                });

                            }else{
                                Log.v("error","Mismatch in IMEI");
                                Toast.makeText(RegisterPage.this, "IMEI Mismatch", Toast.LENGTH_SHORT).show();
                            }


                        }else{
                            Toast.makeText(RegisterPage.this, j.getString("message"), Toast.LENGTH_SHORT).show();

                        }
                    }
                }));
    }

    public void initialize() {
        //Initialize retrofit
        Retrofit retrofit = RetrofitClient.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        Mobile = sharedPreferences.getString("MOBILE","12345");
        Email = sharedPreferences.getString("EMAIL","a@a.com");
        userId = sharedPreferences.getString("UID","123");
        saveEncrypted = retrofit.create(IMyService.class);
    }


    public static boolean isValidEmail(CharSequence target) {

        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

    }

    public static boolean isValidName(String txt) {

        String regx = "^[a-zA-Z\\s]+$";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();

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
}


