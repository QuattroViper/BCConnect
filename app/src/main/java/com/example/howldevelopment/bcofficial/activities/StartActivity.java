package com.example.howldevelopment.bcofficial.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.howldevelopment.bcofficial.R;
import com.example.howldevelopment.bcofficial.models.api_classes.CommunicationClass;
import com.example.howldevelopment.bcofficial.models.ErrorHandling;
import com.example.howldevelopment.bcofficial.models.MySingleton;
import com.example.howldevelopment.bcofficial.models.api_classes.StudentClass;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.kosalgeek.android.json.JsonConverter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class StartActivity extends AppCompatActivity {

    private TextView tvGuestAccount;
    private TextView tvNotRegistered;
    private Button btnLSignIn;
    private EditText etStudentID;
    private Handler handler = new Handler();
    private AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Hide the action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Make the notification bar transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Loader Icon
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.AVLoadingIndicatorView);

        // Underline the TextView
        tvGuestAccount = (TextView) findViewById(R.id.tvGuestAccount);
        tvGuestAccount.setPaintFlags(tvGuestAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvNotRegistered = (TextView) findViewById(R.id.tvNotRegistered);
        tvNotRegistered.setPaintFlags(tvNotRegistered.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnLSignIn = (Button) findViewById(R.id.btnSignIn);
        etStudentID = (EditText) findViewById(R.id.etStudentID);

        // Timer that fires every 100ms to check of button can be enabled

        handler.postDelayed(runnable,100);

        // Event Listener for TextView
        tvGuestAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MenuGuillotineActivity.class);
                intent.putExtra("type","guest");
                startActivity(intent);
                SharedPreferences global =  getSharedPreferences("globalData", MODE_PRIVATE);
                SharedPreferences.Editor globalEditor = global.edit();
                globalEditor.putString("guest","1");
                globalEditor.apply();
            }
        });

        // Event Listener for When student is not registered
        tvNotRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Initiate the retryTimes as 0
        SharedPreferences studentSignIn = getSharedPreferences("studentData",MODE_PRIVATE);
        if (!studentSignIn.contains("retryTimes")) {
            SharedPreferences.Editor studentSignInEditor = studentSignIn.edit();
            studentSignInEditor.putString("retryTimes","0");
            studentSignInEditor.putString("retryTime","0");
            studentSignInEditor.apply();
        }



        // Shared preferences to see if the Student already signed in.
        SharedPreferences studentSignedIn = getSharedPreferences("studentData",MODE_PRIVATE);
        if (studentSignedIn.contains("autoSignIn")) {

            // Go to Main Menu
            Intent intent = new Intent(StartActivity.this, MenuGuillotineActivity.class);
            intent.putExtra("type", "student");
            startActivity(intent);
            finish();
            Toasty.success(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();

        }

        if (studentSignedIn.contains("retryTimes")) {
            if (Integer.parseInt(studentSignedIn.getString("retryTimes","")) > 2) {
                if (Long.parseLong(studentSignedIn.getString("retryTime","")) != 0) {
                    Date date = new Date();
                    String timeInString = studentSignedIn.getString("retryTime","");
                    //if ( (new Date(Long.valueOf(timeInString)).getTime() + 7200000 ) <= date.getTime()  ) {
                    if ( (new Date(Long.valueOf(timeInString)).getTime() + 30000 ) <= date.getTime()  ) {
                        btnLSignIn.setEnabled(true);
                        SharedPreferences.Editor studentSignInEditorReset = studentSignIn.edit();
                        studentSignInEditorReset.putString("retryTimes","2");
                        studentSignInEditorReset.putString("retryTime","0");
                        studentSignInEditorReset.apply();
                    } else {
                        btnLSignIn.setEnabled(false);
                    }
                }
            } else {
                btnLSignIn.setEnabled(true);
            }
        }



        // Shared preferences to see if user tried more than 3 times. Otherwise 2 hour time out.
        // TimeWhenTimedOut
        // TimeWhenFreeUp
        // If TimeWhenFreeUp > (TimeWhenTimedOut + 2 hours) OR TimeWhenTimedOut != 0
        // Enable sign in button again
        // Otherwise disable sign in button

        // Event Listener for Button
        btnLSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                avLoadingIndicatorView.smoothToShow();
                tvGuestAccount.setEnabled(false);
                btnLSignIn.setEnabled(false);
                btnLSignIn.setVisibility(View.INVISIBLE);
                avLoadingIndicatorView.bringToFront();

                RequestQueue requestQueue;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_signin), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String output) {
                        //Toast.makeText(StartActivity.this, output, Toast.LENGTH_SHORT).show();

                        if (!output.isEmpty()) {
                            //Get the code, message and info JSON and Decode the output to the Communication class.
                            ArrayList<CommunicationClass> data = new JsonConverter<CommunicationClass>().toArrayList(output, CommunicationClass.class);
                            CommunicationClass comm = data.get(0);

                            if (comm.code.equals("0")) {

                                ArrayList<StudentClass> studentData = new JsonConverter<StudentClass>().toArrayList(comm.info, StudentClass.class);
                                StudentClass studentClass = studentData.get(0);

                                // Add information when Student is signed in.
                                SharedPreferences studentSignIn = getSharedPreferences("studentData",MODE_PRIVATE);
                                SharedPreferences.Editor studentSignInEditor = studentSignIn.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(studentClass);
                                studentSignInEditor.putString("studentObject",json);
                                studentSignInEditor.putString("autoSignIn","true");
                                studentSignInEditor.putString("retryTimes","0");
                                studentSignInEditor.putString("retryTime","0");
                                studentSignInEditor.putString("token",studentClass.token);
                                studentSignInEditor.apply();
                                SharedPreferences global =  getSharedPreferences("globalData", MODE_PRIVATE);
                                SharedPreferences.Editor globalEditor = global.edit();
                                globalEditor.putString("student","1");
                                globalEditor.apply();

                                // Go to Main Menu
                                Intent intent = new Intent(StartActivity.this, MenuGuillotineActivity.class);
                                intent.putExtra("type", "student");
                                startActivity(intent);
                                finish();

                                // Show Message
                                Toasty.success(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), studentClass.token, Toast.LENGTH_SHORT).show();




                            } else {
                                if (comm.code.equals("2")) {
                                    //Toast.makeText(StartActivity.this, "Incorrect ID Number OR No user", Toast.LENGTH_SHORT).show();
                                    Toasty.error(getApplicationContext(), "Incorrect ID Number OR No user", Toast.LENGTH_SHORT,true).show();
                                    SharedPreferences studentSignIn = getSharedPreferences("studentData",MODE_PRIVATE);
                                    SharedPreferences.Editor studentSignInEditor = studentSignIn.edit();
                                    studentSignInEditor.putString("retryTimes", String.valueOf(Integer.parseInt(studentSignIn.getString("retryTimes","")) + 1));
                                    if (studentSignIn.contains("retryTimes")) {
                                        Integer times = Integer.parseInt(studentSignIn.getString("retryTimes",""));
                                        if (times > 2) {
                                            btnLSignIn.setEnabled(false);
                                            Date date = new Date();
                                            studentSignInEditor.putString("retryTime",String.valueOf(date.getTime()));
                                            Toasty.info (getApplicationContext(), "Retry again in 2 Hours ", Toast.LENGTH_SHORT,true).show();
                                        } else {
                                            Toasty.warning(getApplicationContext(), "Retries Left: " + String.valueOf(3 - times), Toast.LENGTH_SHORT,true).show();
                                            //Toast.makeText(StartActivity.this, "Retries Left: " + String.valueOf(3 - times), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    studentSignInEditor.apply();

                                } else {
                                    ErrorHandling.makeText(getApplicationContext(),comm.code, false).show();
                                }
                                btnLSignIn.setText("SIGN IN");

                            }


                        } else {
                            btnLSignIn.setEnabled(true);
                            avLoadingIndicatorView.smoothToHide();
                            btnLSignIn.setText("SIGN IN");
                            btnLSignIn.setVisibility(View.VISIBLE);
                            tvGuestAccount.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        String message = "";
                        if (volleyError instanceof TimeoutError)  {
                            message = "Connection timed out";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "No connection";
                        } else if (volleyError instanceof ParseError) {
                            message = "Unknown parsed string";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Incorrect student ID";
                        } else {
                            message = "Unknown problem";
                        }
                        Toasty.error(getApplicationContext(), message, Toast.LENGTH_SHORT,true).show();

                        btnLSignIn.setEnabled(true);
                        avLoadingIndicatorView.smoothToHide();
                        btnLSignIn.setText("SIGN IN");
                        btnLSignIn.setVisibility(View.VISIBLE);
                        tvGuestAccount.setEnabled(true);

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //POST data for PHP Script
                        HashMap<String, String> postDataRegister = new HashMap<String, String>();
                        postDataRegister.put("signin", "true");
                        postDataRegister.put("studentID", etStudentID.getText().toString());
                        postDataRegister.put("googleID", FirebaseInstanceId.getInstance().getToken());
                        return postDataRegister;
                    }
                };

                MySingleton.getInstance(StartActivity.this).addToRequestQueue(stringRequest);







            }
        });



    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            SharedPreferences studentSignedIn = getSharedPreferences("studentData",MODE_PRIVATE);
            Date date = new Date();
            String timeInString = studentSignedIn.getString("retryTime","");
            if ( (new Date(Long.valueOf(timeInString)).getTime() + 30000 ) <= date.getTime() && !btnLSignIn.isEnabled() ) {
                btnLSignIn.setEnabled(true);
                SharedPreferences.Editor studentSignInEditor = studentSignedIn.edit();
                studentSignInEditor.putString("retryTimes","1");
                studentSignInEditor.putString("retryTime","0");
                studentSignInEditor.apply();
            }

            //Tell it to run again
            handler.postDelayed(runnable,100);
        }
    };


}
