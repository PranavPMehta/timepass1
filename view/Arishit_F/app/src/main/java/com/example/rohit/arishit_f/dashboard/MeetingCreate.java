package com.example.rohit.arishit_f.dashboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rohit.arishit_f.login.MessageResult;
import com.example.rohit.arishit_f.presentation.Presentation;
import com.example.rohit.arishit_f.R;
import com.example.rohit.arishit_f.retrofit.IMyService;
import com.example.rohit.arishit_f.retrofit.RetrofitClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.Call;

public class MeetingCreate extends AppCompatActivity {
    EditText edittext,edittext2;
    MaterialEditText editAgenda,editDate,editTime;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    private Button notify_button;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    //For Notification
    public static final String CHANNEL_ID = "1";
    private static final String CHANNEL_NAME = "2";
    private static final String CHANNEL_DESC = "3";
    private static  String token = "3";
    private RequestQueue req;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAO76t_FE:APA91bGVqx9Zl3KTRi3GcjT0R3Z7_QzmJmBsT2z5tBDqMplKbUzYPU86DCQM-IaJTTTKSn2WBYJN9E0o0tORpvCYFlCS1uaCsYHejRfcQlTo9y4NJj8gnfLGPU6jOrO0r_ChW-DGVAqO";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE = "Creator Name + Has Created the meeting + Agenda";
    String NOTIFICATION_MESSAGE = "Date & time Hope to see u on time !!";
    public Vector<String> registrationids = new Vector<>();
    private int mYear, mMonth, mDay, mHour, mMinute;
    TextView dateText,timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetingcreate);

        final Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        edittext2 = (EditText) findViewById(R.id.edit_members);
        editAgenda = (MaterialEditText) findViewById(R.id.edit_agenda);
//        editDate = (MaterialEditText) findViewById(R.id.edit_date);
//        editTime = (MaterialEditText) findViewById(R.id.edit_time);
        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);
        listItems = getResources().getStringArray(R.array.members_list);
        checkedItems = new boolean[listItems.length];
        notify_button = (Button) findViewById(R.id.notify_button);

        dateText.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(MeetingCreate.this,
                    (view, year, monthOfYear, dayOfMonth) -> dateText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });


        timeText.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(MeetingCreate.this,
                    (view, hourOfDay, minute) -> timeText.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
            timePickerDialog.show();
        });


        edittext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MeetingCreate.this);
                mBuilder.setTitle(R.string.dialog_title);
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                        if (isChecked) {
                            mUserItems.add(position);
                        } else {
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                    }
                });

                edittext = (EditText) findViewById(R.id.edit_date);

                edittext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        new DatePickerDialog(MeetingCreate.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                final EditText chooseTime = findViewById(R.id.edit_time);
                chooseTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(MeetingCreate.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                chooseTime.setText(hourOfDay + ":" + minutes);
                            }
                        }, 0, 0, false);
                        timePickerDialog.show();
                    }


                });
            }

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };


            private void updateLabel() {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edittext.setText(sdf.format(myCalendar.getTime()));
            }
        });
        req = Volley.newRequestQueue(this);
        notify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String agenda = editAgenda.getText().toString();
                String members = edittext2.getText().toString();
                String date = dateText.getText().toString();
                String time = timeText.getText().toString();
                Log.d("Contents",agenda+" -- "+members+" -- "+date+" -- "+time);
                create_meeting(agenda,members,date,time,"arishti3");

                //Notification Code
                String[] mem = members.split(",");
                for (int i = 0; i < mem.length; i++){
                    Log.d("Members",mem[i]);
                    getToken(mem[i]);
                }

                send("e63ACHKiVYA:APA91bGzm3m_MaBsFFClVfeRj1p699_Q2xcau5yv_2AoYZIgn6qR6MRM4cR7d1OpRhVxLSc6APLfWHGp7Ja-nV7Tetcm6T6CaSUQ1zCaDs6Gk7IIruYQGE9f1RmcgGHHY677LzVrlX5D");
                /*for(int i=0;i<registrationids.size();i++) {
                    Log.d("Token",registrationids.get(i));
                    editTime.setText(registrationids.get(i));
                    send(registrationids.get(i));

                }*/


            }
        });

    }

    private void create_meeting(String agenda, String members, String date, String time,String presentation_id) {
        Call<MessageResult> requestOtp = iMyService.createMeeting(agenda,members,date,time,presentation_id);

        requestOtp.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                Toast.makeText(getApplicationContext(), " Meeting Scheduled Successfully, Notifications Sent", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MeetingCreate.this, Presentation.class));
                finish();
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error in sending OTP!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getToken(String user) {
        Call<MessageResult> requestOtp = iMyService.getToken(user);

        requestOtp.enqueue(new Callback<MessageResult>() {
            @Override
            public void onResponse(Call<MessageResult> call, Response<MessageResult> response) {
                if(response.body().getResult().equals("true")) {
                    registrationids.add(response.body().getMessage());
                    System.out.println("Meeting_Notification "+response.body().getMessage());
                    send(response.body().getMessage());
                    Log.d("M", response.body().getMessage());
                }
                else{
                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResult> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void send(String TOPIC){
        JSONObject main = new JSONObject();
        try {
            main.put("to", TOPIC);
            JSONObject noti = new JSONObject();
            noti.put("title", NOTIFICATION_TITLE);
            noti.put("body", NOTIFICATION_MESSAGE);
            main.put("notification", noti);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_API, main,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "onResponse: " + response.toString());
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", serverKey);
                    params.put("Content-Type", contentType);
                    return params;
                }
            };
            req.add(request);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

}
