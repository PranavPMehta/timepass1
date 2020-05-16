package com.example.rohit.arishit_f.profileInfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.login.LoginScreen;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.rohit.arishit_f.constants.Constants.IP_ADDRESS;

public class setting_main extends AppCompatActivity {

    Spinner spinnerProfilePicture;
    Spinner spinnerOrganisationDetails;
    Spinner spinnerEmailId;
    Spinner spinnerContactDetails;
    Spinner spinnerDesignation;
    Button saveSettingsButton;
    Button logoutButton;

    int spinnerPosition;

    int profilePictureValue = 1;
    int organisationDetailsValue = 0;
    int emailIdValue = 0;
    int contactDetailsValue = 0;
    int designationValue = 2;
    String user_id;
    Button logout;
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
        setContentView(R.layout.activity_setting_main);

        spinnerProfilePicture = (Spinner) findViewById(R.id.spinnerProfilePicture);
        spinnerOrganisationDetails = (Spinner) findViewById(R.id.spinnerOrganisationDetails);
        spinnerEmailId = (Spinner) findViewById(R.id.spinnerEmailId);
        spinnerContactDetails = (Spinner) findViewById(R.id.spinnerContactDetails);
        spinnerDesignation = (Spinner) findViewById(R.id.spinnerDesignation);
        saveSettingsButton = (Button) findViewById(R.id.saveSettingsButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);

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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit2.Call<MessageResult> call = iMyService.logout("Bearer "+bearerToken);
                call.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        if(response.body().getMessage().equals("Logout successfully"))
                        {
                            Toast.makeText(setting_main.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(setting_main.this, LoginScreen.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(setting_main.this, "Error while logging out", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {

                    }
                });
            }
        });

        // spinner drop down
        final List<String> optionList = new ArrayList<String>();
        optionList.add("Self Organization");
        optionList.add("Department");
        optionList.add("None");


        //     PROFILE PICTURE SPINNER SETUP

        // creating adapter for profile picture spinner
        ArrayAdapter<String> arrayAdapterForProfilePicture = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionList);

        // drop down layout style - list view with radio button
        arrayAdapterForProfilePicture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerProfilePicture.setAdapter(arrayAdapterForProfilePicture);

        // set default value for profile picture adapter
        String defaultProfilePicture = "Department";
        spinnerPosition = arrayAdapterForProfilePicture.getPosition(defaultProfilePicture);
        spinnerProfilePicture.setSelection(spinnerPosition);


        // ORGANISATION DETAILS SPINNER SETUP
        // creating adapter for organisation details spinner
        ArrayAdapter<String> arrayAdapterForOrganisationDetails = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionList);

        // drop down layout style - list view with radio button
        arrayAdapterForOrganisationDetails.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerOrganisationDetails.setAdapter(arrayAdapterForOrganisationDetails);

        // set default value for organisation details adapter
        String defaultOrganisationDetails = "None";
        spinnerPosition = arrayAdapterForOrganisationDetails.getPosition(defaultOrganisationDetails);
        spinnerOrganisationDetails.setSelection(spinnerPosition);


        // EMAIL ID SPINNER SETUP

        // creating adapter for email id spinner
        ArrayAdapter<String> arrayAdapterForEmailId = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionList);

        // drop down layout style - list view with radio button
        arrayAdapterForEmailId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerEmailId.setAdapter(arrayAdapterForEmailId);

        // set default value for email id adapter
        String defaultEmailId = "None";
        spinnerPosition = arrayAdapterForEmailId.getPosition(defaultEmailId);
        spinnerEmailId.setSelection(spinnerPosition);



        // CONTACT DETAILS SPINNER SETUP

        // creating adapter for contact details spinner
        ArrayAdapter<String> arrayAdapterForContactDetails = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionList);

        // drop down layout style - list view with radio button
        arrayAdapterForContactDetails.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerContactDetails.setAdapter(arrayAdapterForContactDetails);

        // set default value for contact details adapter
        String defaultContactDetails = "None";
        spinnerPosition = arrayAdapterForContactDetails.getPosition(defaultContactDetails);
        spinnerContactDetails.setSelection(spinnerPosition);


        // DESIGNATION SPINNER SETUP

        // creating adapter for designation spinner
        ArrayAdapter<String> arrayAdapterForDesignation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionList);

        // drop down layout style - list view with radio button
        arrayAdapterForDesignation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerDesignation.setAdapter(arrayAdapterForDesignation);

        // set default value for designation adapter
        String defaultDesignation = "Self Organisation";
        spinnerPosition = arrayAdapterForDesignation.getPosition(defaultDesignation);
        spinnerDesignation.setSelection(spinnerPosition);


                                        // SET VALUES FOR SPINNERS BASED UPON SELECTION

        // PROFILE PICTURE VALUE
        spinnerProfilePicture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                profilePictureValue = (optionList.size()-1)-position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // ORGANISATION DETAILS VALUE
        spinnerOrganisationDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organisationDetailsValue = (optionList.size()-1)-position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // EMAIL ID VALUE
        spinnerEmailId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                emailIdValue = (optionList.size()-1)-position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // CONTACT DETAILS VALUE
        spinnerContactDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contactDetailsValue = (optionList.size()-1)-position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // DESIGNATION VALUE
        spinnerDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designationValue = (optionList.size()-1)-position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettingsData(profilePictureValue, organisationDetailsValue, emailIdValue, contactDetailsValue, designationValue,user_id);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit2.Call<MessageResult> call = iMyService.logout("Bearer "+bearerToken);
                call.enqueue(new Callback<MessageResult>() {
                    @Override
                    public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                        if(response.body().getMessage().equals("Logout successfully"))
                        {
                            Toast.makeText(setting_main.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                            Intent intent = new Intent(setting_main.this, LoginScreen.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(setting_main.this, "Error while logging out", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResult> call, Throwable t) {

                    }
                });
            }
        });


    }

    private void saveSettingsData(int profilePictureValue, int organisationDetailsValue, int emailIdValue, int contactDetailsValue, int designationValue,String uid) {
        compositeDisposable.add(iMyService.saveData(profilePictureValue, organisationDetailsValue,emailIdValue, contactDetailsValue, designationValue,uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String result = jsonObject.getString("result");
                        if(result.equals("true")) {
                            Toast.makeText(setting_main.this, "" + message, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(setting_main.this, "" + message, Toast.LENGTH_SHORT).show();
                        }                    }
                })
        );
    }


}
