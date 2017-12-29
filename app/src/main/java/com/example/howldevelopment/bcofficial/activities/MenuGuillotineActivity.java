package com.example.howldevelopment.bcofficial.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.howldevelopment.bcofficial.R;
import com.example.howldevelopment.bcofficial.fragments.EventsFragment;
import com.example.howldevelopment.bcofficial.fragments.MainPageFragment;
import com.example.howldevelopment.bcofficial.models.api_classes.AppSettingsClass;
import com.example.howldevelopment.bcofficial.models.api_classes.CommunicationClass;
import com.example.howldevelopment.bcofficial.models.ErrorHandling;
import com.example.howldevelopment.bcofficial.models.MySingleton;
import com.example.howldevelopment.bcofficial.models.api_classes.StudentClass;
import com.google.gson.Gson;
import com.kosalgeek.android.json.JsonConverter;
import com.vstechlab.easyfonts.EasyFonts;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class MenuGuillotineActivity extends AppCompatActivity {

    private static final long RIPPLE_DURATION = 500;

    private StudentClass studentClass;
    private GuillotineAnimation guillotineAnimation;
    private String studRank = "0";
    private boolean doubleBackToExitPressedOnce;
    private AppSettingsClass appSettingsClass;

    private TextView tvMainMenu;
    private TextView tvEvents;
    private TextView tvComplaint;
    private TextView tvEnquire;
    private TextView tvAnnoucements;
    private TextView tvInformation;
    private TextView tvBCMap;
    private TextView tvMarkTracker;
    private TextView tvMrMissBC;
    private TextView tvTalentShow;
    private TextView tvAddEvent;
    private TextView tvMakeAnnoucement;
    private TextView tvSignOut;

    private final int RANK_PRESIDENT = 10;
    private final int RANK_VICE_PRESIDENT = 9;
    private final int RANK_SRC_BOARD = 8;
    private final int RANK_SRC = 7;
    private final int RANK_PRIVILEGED = 6;
    private final int RANK_SECOND_PRIVILEGED = 5;
    private final int RANK_STUDENT = 4;
    private final int RANK_GUEST = 0;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.root)
    FrameLayout root;
    @BindView(R.id.content_hamburger)
    View contentHamburger;

    private View generateMenu(Context context, String personRank) {

        Integer rank = Integer.valueOf(personRank);

        if (rank == RANK_SRC_BOARD || rank == RANK_VICE_PRESIDENT || rank == RANK_PRESIDENT || rank == RANK_PRIVILEGED) {
            return LayoutInflater.from(context).inflate(R.layout.guillotine_src_board, null);
        } else if (rank == RANK_SRC) {
            return LayoutInflater.from(context).inflate(R.layout.guillotine_src, null);
        } else if (rank == RANK_STUDENT) {
            return LayoutInflater.from(context).inflate(R.layout.guillotine_students, null);
        } else if (rank == RANK_SECOND_PRIVILEGED) {
            return LayoutInflater.from(context).inflate(R.layout.guillotine_guest, null);
        } else if (rank == RANK_GUEST) {
            return LayoutInflater.from(context).inflate(R.layout.guillotine_guest, null);
        }

        return LayoutInflater.from(this).inflate(R.layout.guillotine_guest, null);
    }

    private void initiateMenuItems(String personRank) {

        Integer rank = Integer.valueOf(personRank);

        if (rank == RANK_GUEST || rank == RANK_SECOND_PRIVILEGED) {

            tvEvents = (TextView) findViewById(R.id.tvEvent);
            tvEvents.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvBCMap = (TextView) findViewById(R.id.tvBCMap);
            tvBCMap.setTypeface(EasyFonts.captureIt(getApplicationContext()));

        } else {

            tvEvents = (TextView) findViewById(R.id.tvEvent);
            tvEvents.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvComplaint = (TextView) findViewById(R.id.tvComplaint);
            tvComplaint.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvEnquire = (TextView) findViewById(R.id.tvEnquire);
            tvEnquire.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvAnnoucements = (TextView) findViewById(R.id.tvAnnouncements);
            tvAnnoucements.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvInformation = (TextView) findViewById(R.id.tvInformation);
            tvInformation.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvBCMap = (TextView) findViewById(R.id.tvBCMap);
            tvBCMap.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvMrMissBC = (TextView) findViewById(R.id.tvMrMissBC);
            tvMrMissBC.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvTalentShow = (TextView) findViewById(R.id.tvTalentShow);
            tvTalentShow.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvMarkTracker = (TextView) findViewById(R.id.tvMarks);
            tvMarkTracker.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            tvSignOut = (TextView) findViewById(R.id.tvSignOut);
            tvSignOut.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            if (rank == RANK_SRC_BOARD || rank == RANK_VICE_PRESIDENT || rank == RANK_PRESIDENT || rank == RANK_PRIVILEGED) {

                tvAddEvent = (TextView) findViewById(R.id.tvAddEvent);
                tvAddEvent.setTypeface(EasyFonts.captureIt(getApplicationContext()));

                tvMakeAnnoucement = (TextView) findViewById(R.id.tvAnnouncement);
                tvMakeAnnoucement.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            } else if (rank == RANK_SRC)  {

                tvMakeAnnoucement = (TextView) findViewById(R.id.tvAnnouncement);
                tvMakeAnnoucement.setTypeface(EasyFonts.captureIt(getApplicationContext()));

            }

        }

        tvMainMenu = (TextView) findViewById(R.id.tvMainMenu);


    }

    private void activateItem(TextView textView, String personRank) {

        Integer rank = Integer.valueOf(personRank);

        if (rank == RANK_GUEST) {
            tvBCMap.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvEvents.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
        } else {
            tvEvents.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvComplaint.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvEnquire.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvAnnoucements.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvInformation.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvBCMap.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvMarkTracker.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvTalentShow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvMrMissBC.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            tvSignOut.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            if (rank == RANK_SRC_BOARD || rank == RANK_VICE_PRESIDENT || rank == RANK_PRESIDENT || rank == RANK_PRIVILEGED) {
                tvAddEvent.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
                tvMakeAnnoucement.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            } else if (rank == RANK_SRC)  {
                tvMakeAnnoucement.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
            }
        }


        switch (textView.getId()) {
            case R.id.tvEvent : {
                tvEvents.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            case R.id.tvComplaint : {
                tvComplaint.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            case R.id.tvEnquire : {
                tvEnquire.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            case R.id.tvAnnouncements : {
                tvAnnoucements.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            case R.id.tvInformation : {
                tvInformation.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            case R.id.tvBCMap : {
                tvBCMap.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            case R.id.tvMarks : {
                tvMarkTracker.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            case R.id.tvMrMissBC : {
                tvMrMissBC.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            case R.id.tvTalentShow : {
                tvTalentShow.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            case R.id.tvAddEvent : {
                tvAddEvent.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            case R.id.tvAnnouncement : {
                tvMakeAnnoucement.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            case R.id.tvSignOut : {
                tvSignOut.setTextColor( ContextCompat.getColor(getApplicationContext(),R.color.colorActive));
            }
            break;
            default: {

                }
                break;
        }

        if (textView.getId() != R.id.tvMainMenu) {
            guillotineAnimation.close();
        }

    }

    private void createListeners(String personRank) {

        Integer rank = Integer.valueOf(personRank);
        tvMainMenu.setOnClickListener(mainMenuListener);

        if (rank == RANK_GUEST) {
            tvEvents.setOnClickListener(eventsListener);
            tvBCMap.setOnClickListener(bcmapListener);
        } else {
            tvEvents.setOnClickListener(eventsListener);
            tvEnquire.setOnClickListener(enquireListener);
            tvComplaint.setOnClickListener(complaintListener);
            tvAnnoucements.setOnClickListener(announcementsListener);
            tvInformation.setOnClickListener(informationListener);
            tvBCMap.setOnClickListener(bcmapListener);
            tvMarkTracker.setOnClickListener(markTrackerListener);
            tvMrMissBC.setOnClickListener(mrMissBCListener);
            tvTalentShow.setOnClickListener(talentShowListener);
            tvSignOut.setOnClickListener(signOutListener);
            if (rank == RANK_SRC_BOARD || rank == RANK_VICE_PRESIDENT || rank == RANK_PRESIDENT || rank == RANK_PRIVILEGED) {
                tvAddEvent.setOnClickListener(addEventListener);
                tvMakeAnnoucement.setOnClickListener(addAnnouncementListener);
            } else if (rank == RANK_SRC) {
                tvMakeAnnoucement.setOnClickListener(addAnnouncementListener);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_guillotine);
        ButterKnife.bind(this);

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        View guillotineMenu;

        // Get default menu settings
        appSettingsClass = new AppSettingsClass();

        // Get menu Settings
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_settings), new Response.Listener<String>() {
            @Override
            public void onResponse(String output) {
                if (!output.isEmpty()) {
                    ArrayList<CommunicationClass> data = new JsonConverter<CommunicationClass>().toArrayList(output, CommunicationClass.class);
                    CommunicationClass comm = data.get(0);

                    if (comm.code.equals("0")) {

                        ArrayList<AppSettingsClass> appSettingsData = new JsonConverter<AppSettingsClass>().toArrayList(comm.info, AppSettingsClass.class);
                        appSettingsClass = appSettingsData.get(0);

                    } else {
                        ErrorHandling.makeText(getApplicationContext(),"99", false).showWarning();
                    }
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ErrorHandling.makeText(getApplicationContext(), "99", false).showWarning();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //POST data for PHP Script
                HashMap<String, String> postData = new HashMap<String, String>();
                postData.put("menuSettings", "true");
                return postData;
            }
        };

        MySingleton.getInstance(MenuGuillotineActivity.this).addToRequestQueue(stringRequest);


        if (getIntent().getStringExtra("type").equals("student")) {

            //Get json from SP and decode json to EmployeeClass.
            SharedPreferences shared = getSharedPreferences("studentData", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = shared.getString("studentObject", "");
            studentClass = gson.fromJson(json, StudentClass.class);
            studRank = studentClass.rank;

            //View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine_students, null);
            guillotineMenu = generateMenu(this, studentClass.rank);
            root.addView(guillotineMenu);

            guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                    .setDuration(RIPPLE_DURATION)
                    .setActionBarViewForAnimation(toolbar)
                    .setClosedOnStart(true)
                    .build();

            // Add Components to variable.
            initiateMenuItems(studentClass.rank);

        } else {

            studRank = "0";

            //View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine_students, null);
            guillotineMenu = generateMenu(this,"0");
            root.addView(guillotineMenu);

            guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                    .setDuration(RIPPLE_DURATION)
                    .setActionBarViewForAnimation(toolbar)
                    .setClosedOnStart(true)
                    .build();


            // Add Components to variable.
            initiateMenuItems("0");



        }

        // Create listeners based on rank
        createListeners(studRank);

        // Add the MainPageFragment for summory
        MainPageFragment mainPageFragment = new MainPageFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(
                R.id.relativeLayout_for_fragment,
                mainPageFragment,
                mainPageFragment.getTag()
        ).commit();

    }

    private View.OnClickListener mainMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activateItem(tvMainMenu, studRank);
            // Add the MainPageFragment for summory
            MainPageFragment mainPageFragment = new MainPageFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(
                    R.id.relativeLayout_for_fragment,
                    mainPageFragment,
                    mainPageFragment.getTag()
            ).commit();
        }
    };

    private View.OnClickListener eventsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemEvents.equals("1")) {
                activateItem(tvEvents, studRank);
                EventsFragment eventsFragment = new EventsFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(
                        R.id.relativeLayout_for_fragment,
                        eventsFragment,
                        eventsFragment.getTag()
                ).commit();
            } else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }

        }
    };

    private View.OnClickListener complaintListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemComplaint.equals("1")) {
                activateItem(tvComplaint, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };

    private View.OnClickListener enquireListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemEnquire.equals("1")) {
                activateItem(tvEnquire, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };

    private View.OnClickListener announcementsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemAnnoucements.equals("1")) {
                activateItem(tvAnnoucements, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };

    private View.OnClickListener informationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemInformation.equals("1")) {
                activateItem(tvInformation, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };

    private View.OnClickListener markTrackerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemMarkTracker.equals("1")) {
                activateItem(tvMarkTracker, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };

    private View.OnClickListener addEventListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemAddEvent.equals("1")) {
                activateItem(tvAddEvent, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };

    private View.OnClickListener addAnnouncementListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemItemMakeAnnouncement.equals("1")) {
                activateItem(tvMakeAnnoucement, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };


    private View.OnClickListener bcmapListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemBelgiumMap.equals("1")) {
                activateItem(tvBCMap, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };

    private View.OnClickListener mrMissBCListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemMrMissBc.equals("1")) {
                activateItem(tvMrMissBC, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };

    private View.OnClickListener talentShowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appSettingsClass.itemTalentShow.equals("1")) {
                activateItem(tvTalentShow, studRank);
            }
            else {
                ErrorHandling.makeText(getApplicationContext(), "89", false).showWarning();
            }
        }
    };




    private View.OnClickListener signOutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //activateItem(tvSignOut, studRank);

            SharedPreferences studentSignIn = getSharedPreferences("studentData",MODE_PRIVATE);
            SharedPreferences.Editor studentSignInEditor = studentSignIn.edit();
            studentSignInEditor.putString("retryTimes","0");
            studentSignInEditor.putString("retryTime","0");
            studentSignInEditor.remove("studentObject");
            studentSignInEditor.remove("autoSignIn");
            studentSignInEditor.remove("token");
            studentSignInEditor.apply();
            finish();
            SharedPreferences global =  getSharedPreferences("globalData", MODE_PRIVATE);
            SharedPreferences.Editor globalEditor = global.edit();
            globalEditor.remove("student");
            globalEditor.apply();

            Intent intent = new Intent(MenuGuillotineActivity.this, StartActivity.class);
            startActivity(intent);
            Toasty.success(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT,true).show();
            finish();

            if (getIntent().getStringExtra("type").equals("student")) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_signin), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String output) {
                        if (!output.isEmpty()) {

                            ArrayList<CommunicationClass> data = new JsonConverter<CommunicationClass>().toArrayList(output, CommunicationClass.class);
                            CommunicationClass comm = data.get(0);

                            if (comm.code.equals("0")) {

                                SharedPreferences studentSignIn = getSharedPreferences("studentData",MODE_PRIVATE);
                                SharedPreferences.Editor studentSignInEditor = studentSignIn.edit();
                                studentSignInEditor.putString("retryTimes","0");
                                studentSignInEditor.putString("retryTime","0");
                                studentSignInEditor.remove("studentObject");
                                studentSignInEditor.remove("autoSignIn");
                                studentSignInEditor.remove("token");
                                studentSignInEditor.apply();
                                //finish();
                                SharedPreferences global =  getSharedPreferences("globalData", MODE_PRIVATE);
                                SharedPreferences.Editor globalEditor = global.edit();
                                globalEditor.remove("student");
                                globalEditor.apply();

                                //Intent intent = new Intent(MenuGuillotineActivity.this, StartActivity.class);
                                //startActivity(intent);
                                //Toasty.success(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT,true).show();
                                //finish();

                            } else {
                                ErrorHandling.makeText(getApplicationContext(),comm.code, false).show();
                            }
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


                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //POST data for PHP Script
                        HashMap<String, String> postDataSignout = new HashMap<String, String>();
                        postDataSignout .put("signout", "true");
                        postDataSignout.put("token", studentClass.token);
                        return postDataSignout;
                    }


                };

                MySingleton.getInstance(MenuGuillotineActivity.this).addToRequestQueue(stringRequest);
            } else {
                SharedPreferences globall =  getSharedPreferences("globalData", MODE_PRIVATE);
                SharedPreferences.Editor globallEditor = globall.edit();
                globallEditor.remove("guest");
                globallEditor.apply();
            }
        }
    };













    @Override
    public void onBackPressed() {
        //guillotineAnimation.close();
        if (doubleBackToExitPressedOnce) {
            if (studRank.equals("0")) {
                startActivity(new Intent(MenuGuillotineActivity.this, StartActivity.class));
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            Toasty.info(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();
        }
        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);


    }


}
