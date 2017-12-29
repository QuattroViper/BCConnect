package com.example.howldevelopment.bcofficial.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.howldevelopment.bcofficial.models.api_classes.CommunicationClass;
import com.example.howldevelopment.bcofficial.models.ErrorHandling;
import com.example.howldevelopment.bcofficial.models.MySingleton;
import com.example.howldevelopment.bcofficial.models.api_classes.StudentClass;
import com.google.gson.Gson;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private StudentClass studentClass;

    private final int NAV_GROUP_EVENT = 1;
    private final int NAV_SOCIAL_EVENTS = 11;
    private final int NAV_SPORT_EVENTS = 12;
    private final int NAV_OTHER_EVENTS = 13;
    private final int NAV_CLOSED_EVENTS = 14;

    private final int NAV_GROUP_COMPLAINTS = 2;
    private final int NAV_COMPLAIN = 21;
    private final int NAV_ENQUIRY = 22;

    private final int NAV_GROUP_INFORMATION = 3;
    private final int NAV_ANNOUNCEMENTS = 31;
    private final int NAV_QUICKINFO = 32;

    private final int NAV_GROUP_FEATURES = 4;
    private final int NAV_MARK_TRACKER = 41;
    private final int NAV_BC_MAP = 42;

    private final int NAV_GROUP_ADMINISTRATION = 7;
    private final int NAV_ADD_EVENT = 71;
    private final int NAV_QUICK_ANNOUNCEMENT = 72;

    private final int NAV_GROUP_SIGNOUT = 8;
    private final int NAV_SIGNOUT = 81;

    private final int RANK_PRESIDENT = 10;
    private final int RANK_VICE_PRESIDENT = 9;
    private final int RANK_SRC_BOARD = 8;
    private final int RANK_SRC = 7;
    private final int RANK_PRIVILEGED = 6;
    private final int RANK_SECOND_PRIVILEGED = 5;
    private final int RANK_STUDENT = 4;
    private final int RANK_GUEST = 0;


    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void addItemsInMenuDrawer(NavigationView navigationView, String personRank) {
        Menu tempMenu = navigationView.getMenu();
        //Menu subMenu = tempMenu.

        Integer rank = Integer.valueOf(personRank);

        if (rank >= RANK_STUDENT) {
            Menu submenuMain = tempMenu.addSubMenu("Events");
            submenuMain.add(NAV_GROUP_EVENT, NAV_SOCIAL_EVENTS, 1, "Social Events").setIcon(R.drawable.ic_menu_camera).setCheckable(true);
            submenuMain.add(NAV_GROUP_EVENT, NAV_SPORT_EVENTS, 2, "Sport Events").setIcon(R.drawable.ic_menu_camera).setCheckable(true);
            submenuMain.add(NAV_GROUP_EVENT, NAV_OTHER_EVENTS, 3, "Another Event Name").setIcon(R.drawable.ic_menu_camera).setCheckable(true);
            submenuMain.add(NAV_GROUP_EVENT, NAV_CLOSED_EVENTS, 4, "Closed Events").setIcon(R.drawable.ic_menu_camera).setCheckable(true);

            Menu submenuNotice = tempMenu.addSubMenu("Complaints");
            submenuNotice.add(NAV_GROUP_COMPLAINTS, NAV_COMPLAIN, 1, "Complaint").setIcon(R.drawable.ic_menu_camera).setCheckable(true);        // Complaint will have category like, Techinical, Hostil, Behavior. Max 2
            submenuNotice.add(NAV_GROUP_COMPLAINTS, NAV_ENQUIRY, 2, "Enquiry").setIcon(R.drawable.ic_menu_camera).setCheckable(true);        // If the Student wants to know something. Max 2

            Menu submenuInformation = tempMenu.addSubMenu("Information");
            submenuInformation.add(NAV_GROUP_INFORMATION, NAV_ANNOUNCEMENTS, 1, "Announcements").setIcon(R.drawable.ic_menu_camera).setCheckable(true);
            submenuInformation.add(NAV_GROUP_INFORMATION, NAV_QUICKINFO, 2, "Quick Information").setIcon(R.drawable.ic_menu_camera).setCheckable(true);

            Menu submenuFeatures = tempMenu.addSubMenu("Features");
            submenuFeatures.add(NAV_GROUP_FEATURES, NAV_MARK_TRACKER, 1, "Mark Tracker").setIcon(R.drawable.ic_menu_camera).setCheckable(true);
            submenuFeatures.add(NAV_GROUP_FEATURES, NAV_BC_MAP, 2, "Belgium Campus Map").setIcon(R.drawable.ic_menu_camera).setCheckable(true);

            if (rank >= RANK_SRC) {
                Menu submenuAdministration = tempMenu.addSubMenu("Administration");
                if (rank >=  RANK_SRC_BOARD) {
                    submenuAdministration.add(NAV_GROUP_ADMINISTRATION, NAV_ADD_EVENT, 1, "Add Event").setIcon(R.drawable.ic_menu_camera).setCheckable(true);
                }
                submenuAdministration.add(NAV_GROUP_ADMINISTRATION, NAV_QUICK_ANNOUNCEMENT, 2, "Announcement").setIcon(R.drawable.ic_menu_camera).setCheckable(true);
            }


        } else if (rank == RANK_GUEST) {
            Menu submenuFeatures = tempMenu.addSubMenu("Features");
            submenuFeatures.add(NAV_GROUP_FEATURES, NAV_BC_MAP, 2, "Belgium Campus Map").setIcon(R.drawable.ic_menu_camera).setCheckable(true);
        }

        Menu submenuSignOut = tempMenu.addSubMenu("Sign out");
        submenuSignOut.add(NAV_GROUP_SIGNOUT, NAV_SIGNOUT, 1, "Sign out").setIcon(R.drawable.ic_menu_camera).setCheckable(true);

        navigationView.invalidate();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (getIntent().getStringExtra("type").equals("student")) {

            //Get json from SP and decode json to EmployeeClass.
            SharedPreferences shared = getSharedPreferences("studentData", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = shared.getString("studentObject", "");
            studentClass = gson.fromJson(json, StudentClass.class);

            addItemsInMenuDrawer(navigationView, studentClass.rank);

        } else {
            addItemsInMenuDrawer(navigationView, "0");
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == NAV_SIGNOUT) {


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
                                finish();

                                Intent intent = new Intent(MenuActivity.this, StartActivity.class);
                                startActivity(intent);
                                Toasty.success(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT,true).show();
                                finish();

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
                        Toasty.error(getApplicationContext(), message, Toast.LENGTH_SHORT,true).show();
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

                MySingleton.getInstance(MenuActivity.this).addToRequestQueue(stringRequest);
            } else {
                Intent intent = new Intent(MenuActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }




        }  else if (id == NAV_ADD_EVENT) {

        } else if (id == NAV_BC_MAP) {
            Intent intent = new Intent(MenuActivity.this, MenuGuillotineActivity.class);
            intent.putExtra("type","guest");
            startActivity(intent);
        }
//        else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
