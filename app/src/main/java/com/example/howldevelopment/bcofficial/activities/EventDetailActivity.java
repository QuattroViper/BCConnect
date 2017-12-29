package com.example.howldevelopment.bcofficial.activities;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.support.transition.TransitionManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.howldevelopment.bcofficial.R;
import com.example.howldevelopment.bcofficial.models.ErrorHandling;
import com.example.howldevelopment.bcofficial.models.MySingleton;
import com.example.howldevelopment.bcofficial.models.api_classes.CommunicationClass;
import com.example.howldevelopment.bcofficial.models.api_classes.EventClass;
import com.example.howldevelopment.bcofficial.models.api_classes.StudentClass;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.google.gson.Gson;
import com.kosalgeek.android.json.JsonConverter;
import com.vstechlab.easyfonts.EasyFonts;
import com.wang.avi.AVLoadingIndicatorView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.fresco.processors.BlurPostprocessor;

public class EventDetailActivity extends AppCompatActivity {

    private EventClass eventClass;
    private StudentClass studentClass;
    private String studRank;
    private boolean isInterested = false;
    private boolean visible = false;    // For button animation
    private boolean eventClosed;
    private Toolbar tToolbar;
    private AVLoadingIndicatorView avLoadingIndicatorView;

    private SimpleDraweeView simpleDraweeView;
    private TextView tvEventItemName;
    private TextView tvEventItemDate;
    private TextView tvEventItemCost;
    private TextView tvEventItemTime;
    private TextView tvEventItemPlace;
    private TextView tvEventItemInterested;
    private Button btnInterested;
    private TextView tvEventItemMore;

    private void updatePeopleInterested() {
        tvEventItemInterested.setText("Searching audience");
        btnInterested.setEnabled(false);

        StringRequest stringRequestForCount = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_event), new Response.Listener<String>() {
            @Override
            public void onResponse(String output) {
                if (!output.isEmpty()) {
                    ArrayList<CommunicationClass> data = new JsonConverter<CommunicationClass>().toArrayList(output, CommunicationClass.class);
                    CommunicationClass comm = data.get(0);

                    if (comm.code.equals("0")) {
                        tvEventItemInterested.setText(comm.info + " People Interested");
                        eventClass.interested = comm.info;
                        btnInterested.setEnabled(true);
                    } else {
                        ErrorHandling.makeText(getApplicationContext(),comm.code, false).show();        // REMOVE THIS
                        tvEventItemInterested.setText(eventClass.interested + " People Interested");
                        btnInterested.setEnabled(true);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ErrorHandling.makeText(getApplicationContext(), volleyError).showVolley();      //REMOVE THIS
                tvEventItemInterested.setText(eventClass.interested + " People Interested");
                btnInterested.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //POST data for PHP Script
                HashMap<String, String> postDataEvents = new HashMap<String, String>();
                postDataEvents.put("eventInterestedCount", "true");
                postDataEvents.put("eventID",eventClass.eventId);
                return postDataEvents;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequestForCount);
    }

    private void addIndependantItems() {

        ImageRequest request;
        if (eventClass.postprocessing.equals("0")) {
            Uri uri = Uri.parse(getResources().getString(R.string.url_images) + eventClass.picture);
            request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(400,225))
                    .build();
        } else {
            Postprocessor postprocessor = new BlurPostprocessor(this,10);
            Uri uri = Uri.parse(getResources().getString(R.string.url_images) + eventClass.picture);
            request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(400,225))
                    .setPostprocessor(postprocessor)
                    .build();
        }

        simpleDraweeView.setController(Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setImageRequest(request).build());

        //holder.imgPoster.setImageURI(uri);
        PointF focusPoint = new PointF(0.5f, 0.5f);
        simpleDraweeView.getHierarchy().setActualImageFocusPoint(focusPoint);

        tvEventItemName.setText(eventClass.name);
        //SpannableString s = new SpannableString(eventClass.name);
        //s.setSpan(new ForegroundColorSpan(Color.WHITE), 0 , eventClass.name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //tToolbar.setTitleTextColor(Color.WHITE);
        //tToolbar.setSubtitleTextColor(Color.WHITE);
        //tToolbar.setTitle(s);
        LocalDate localeDate = new LocalDate(eventClass.date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM");
        String date = localeDate.getDayOfMonth() + " " + formatter.print(localeDate) + " " + localeDate.getYear();
        tvEventItemDate.setText(date);
        tvEventItemTime.setText(eventClass.time);
        tvEventItemPlace.setText(wrapString(eventClass.place,25));
        if (eventClass.cost.equals("0")) {
            tvEventItemCost.setText("Free entrance");
        } else {
            tvEventItemCost.setText("R" + eventClass.cost + " Per Person");
        }

        //tvEventItemInterested.setText(eventClass.interested + " People Interested");

        updatePeopleInterested();



    }

    private void updateButton() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // region Assigning Components

        // Assigning the components
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.imgEventItemImage);
        tvEventItemName = (TextView) findViewById(R.id.tvEventItemName);
        tvEventItemDate = (TextView) findViewById(R.id.tvEventItemDate);
        tvEventItemCost = (TextView) findViewById(R.id.tvEventItemCost);
        tvEventItemInterested = (TextView) findViewById(R.id.tvEventItemInterested);
        tvEventItemTime = (TextView) findViewById(R.id.tvEventItemTime);
        tvEventItemPlace = (TextView) findViewById(R.id.tvEventItemPlace);
        tvEventItemMore = (TextView) findViewById(R.id.tvEventItemMore);
        btnInterested = (Button) findViewById(R.id.btnInterested);
        tToolbar = (Toolbar) findViewById(R.id.eventToolbar);

        //Loader Icon
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.AVLoadingIndicatorView);
        avLoadingIndicatorView.hide();

        tvEventItemMore.setPaintFlags(tvEventItemMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Set fonts to components
//        tvEventItemName.setTypeface(EasyFonts.caviarDreamsBold(this));
//        tvEventItemDate.setTypeface(EasyFonts.caviarDreams(this));
//        tvEventItemCost.setTypeface(EasyFonts.caviarDreams(this));
//        tvEventItemInterested.setTypeface(EasyFonts.caviarDreams(this));
//        btnInterested.setTypeface(EasyFonts.caviarDreams(this));
//        tvEventItemTime.setTypeface(EasyFonts.caviarDreamsBold(this));
//        tvEventItemPlace.setTypeface(EasyFonts.caviarDreamsBold(this));

        tToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back,getTheme()));
        tToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //

        // endregion

        // region Global Data

        // Get global data
        SharedPreferences global =  getSharedPreferences("globalData", MODE_PRIVATE);
        if (global.contains("student")) {
            //Get json from SP and decode json to EmployeeClass.
            SharedPreferences shared =  getSharedPreferences("studentData", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = shared.getString("studentObject", "");
            studentClass = gson.fromJson(json, StudentClass.class);
            studRank = studentClass.rank;
            btnInterested.setEnabled(false);
            btnInterested.setText("CHECKING EVENT LIST");
        } else {
            studRank = "0";
            btnInterested.setEnabled(false);
            btnInterested.setText("NOT SIGNED IN");
        }

        // endregion

        // region Interested Button State

        // Get Event string from Intent
        eventClass = new Gson().fromJson(getIntent().getStringExtra("eventDetails"),EventClass.class);
        if (eventClass != null) {

            addIndependantItems();

            // check if closed
            LocalDate localDate = new LocalDate(eventClass.date);
            LocalDate localDateNow = new LocalDate().plusDays(1);
            if (localDate.isBefore(localDateNow)) {
                btnInterested.setEnabled(false);
                btnInterested.setText("EVENT CLOSED");
                eventClosed = true;
            } else {
                eventClosed = false;
            }

            if (!eventClosed) {
                if (!studRank.equals("0")) {
                    // See if the user is already interested
                    StringRequest stringRequestForInterested = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_event), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String output) {
                            if (!output.isEmpty()) {
                                ArrayList<CommunicationClass> data = new JsonConverter<CommunicationClass>().toArrayList(output, CommunicationClass.class);
                                CommunicationClass comm = data.get(0);

                                if (comm.code.equals("0")) {
                                    btnInterested.setEnabled(true);
                                    btnInterested.setText("I AM NOT INTERESTED ANYMORE");
                                    isInterested = true;

                                    btnInterested.setText("I AM NOT INTERESTED ANYMORE");

                                } else {
                                    btnInterested.setEnabled(true);
                                    btnInterested.setText("I AM INTERESTED");
                                    isInterested = false;
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            btnInterested.setEnabled(false);
                            btnInterested.setText("EVENT PLANNER UNAVAILABLE");
                            isInterested = false;
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            //POST data for PHP Script
                            HashMap<String, String> postDataEvents = new HashMap<String, String>();
                            postDataEvents.put("checkIfInterested", "true");
                            postDataEvents.put("token", studentClass.token);
                            postDataEvents.put("eventID",eventClass.eventId);
                            return postDataEvents;
                        }
                    };

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequestForInterested);
                }
            }





        } else {
            // Show Error and show default GENERIC Event Class
        }

        // endregion

        // region Interested Button

        // Interested button

        btnInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                avLoadingIndicatorView.smoothToShow();
                btnInterested.setEnabled(false);
                btnInterested.setVisibility(View.INVISIBLE);
                avLoadingIndicatorView.bringToFront();

                // Update person interested
                StringRequest stringRequestForUpdate = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_event), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String output) {
                        if (!output.isEmpty()) {
                            ArrayList<CommunicationClass> data = new JsonConverter<CommunicationClass>().toArrayList(output, CommunicationClass.class);
                            CommunicationClass comm = data.get(0);

                            Toasty.success(getApplicationContext(),"DONE",Toast.LENGTH_SHORT,true).show();

                            btnInterested.setEnabled(true);
                            avLoadingIndicatorView.smoothToHide();
                            btnInterested.setVisibility(View.VISIBLE);

                            if (isInterested) {
                                if (comm.code.equals("0")) {
                                    isInterested = false;
                                    btnInterested.setText("I am Interested");
                                } else {

                                }
                            } else {
                                if (comm.code.equals("0")) {
                                    isInterested = true;
                                    btnInterested.setText("I AM NOT Interested Anymore");
                                } else {

                                }
                            }

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ErrorHandling.makeText(getApplicationContext(),volleyError).showVolley();
                        btnInterested.setEnabled(true);
                        avLoadingIndicatorView.smoothToHide();
                        btnInterested.setVisibility(View.VISIBLE);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        //POST data for PHP Script
                        HashMap<String, String> postDataEvents = new HashMap<String, String>();
                        if (isInterested) {
                            // Run DELETE
                            postDataEvents.put("DeleteEventInterested", "true");
                        } else {
                            // Run UPDATE
                            postDataEvents.put("updateEventInterested", "true");
                        }
                        postDataEvents.put("token", studentClass.token);
                        postDataEvents.put("eventID",eventClass.eventId);
                        return postDataEvents;
                    }
                };

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequestForUpdate);

                updatePeopleInterested();
            }
        });

        // endregion

        // region More/Less TextView

        tvEventItemMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvEventItemMore.getText().equals("More")) {
                    tvEventItemMore.setText("Less");
                } else {
                    tvEventItemMore.setText("More");
                }
            }
        });

        // endregion

    }

    public static String wrapString(String string, int charWrap) {
        int lastBreak = 0;
        int nextBreak = charWrap;
        if (string.length() > charWrap) {
            String setString = "";
            do {
                while (string.charAt(nextBreak) != ' ' && nextBreak > lastBreak) {
                    nextBreak--;
                }
                if (nextBreak == lastBreak) {
                    nextBreak = lastBreak + charWrap;
                }
                setString += string.substring(lastBreak, nextBreak).trim() + "\n";
                lastBreak = nextBreak;
                nextBreak += charWrap;

            } while (nextBreak < string.length());
            setString += string.substring(lastBreak).trim();
            return setString;
        } else {
            return string;
        }
    }
}














