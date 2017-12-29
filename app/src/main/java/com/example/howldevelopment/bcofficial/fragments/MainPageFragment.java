package com.example.howldevelopment.bcofficial.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.howldevelopment.bcofficial.activities.EventDetailActivity;
import com.example.howldevelopment.bcofficial.adapters.EventsAdapter;
import com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport;
import com.example.howldevelopment.bcofficial.models.api_classes.CommunicationClass;
import com.example.howldevelopment.bcofficial.models.ErrorHandling;
import com.example.howldevelopment.bcofficial.models.api_classes.EventClass;
import com.example.howldevelopment.bcofficial.models.MySingleton;
import com.example.howldevelopment.bcofficial.models.api_classes.StudentClass;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.kosalgeek.android.json.JsonConverter;
import com.vstechlab.easyfonts.EasyFonts;

import org.joda.time.LocalDate;
import org.lucasr.twowayview.TwoWayLayoutManager;
import org.lucasr.twowayview.widget.ListLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragment extends Fragment {

    View masterView;
    Context context;
    StudentClass studentClass;
    String studRank;
    TwoWayView lvUpcommingEvents;
    TwoWayView lvNewEvents;
    TwoWayView lvNewAnnouncements;
    ArrayList<EventClass> eventUpcomingArrayList = new ArrayList<>();
    ArrayList<EventClass> eventNewArrayList = new ArrayList<>();

    private void createHeadings(View view) {
        TextView tvHeaderUpcommingEvents = (TextView) view.findViewById(R.id.tvHeaderUpcommingEvents);
        tvHeaderUpcommingEvents.setTypeface(EasyFonts.caviarDreamsBold(context));
        TextView tvHeaderNewEvents = (TextView) view.findViewById(R.id.tvHeaderNewEvents);
        tvHeaderNewEvents.setTypeface(EasyFonts.caviarDreamsBold(context));
        TextView tvHeaderNewAnnouncements = (TextView) view.findViewById(R.id.tvHeaderNewAnnouncements);
        tvHeaderNewAnnouncements.setTypeface(EasyFonts.caviarDreamsBold(context));

    }

    public MainPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        masterView = view;

        // Get Application Context
        context = getContext().getApplicationContext();
        Fresco.initialize(context);

        // Assign components
        lvUpcommingEvents = (TwoWayView) view.findViewById(R.id.lvUpcommingEvents);
        lvUpcommingEvents.setLayoutManager(new ListLayoutManager(context, TwoWayLayoutManager.Orientation.HORIZONTAL));
        lvUpcommingEvents.setHasFixedSize(true);

        lvNewEvents = (TwoWayView) view.findViewById(R.id.lvNewEvents);
        lvNewEvents.setLayoutManager(new ListLayoutManager(context, TwoWayLayoutManager.Orientation.HORIZONTAL));
        lvNewEvents.setHasFixedSize(true);

        lvNewAnnouncements = (TwoWayView) view.findViewById(R.id.lvNewAnnouncements);
        lvNewAnnouncements.setLayoutManager(new ListLayoutManager(context, TwoWayLayoutManager.Orientation.HORIZONTAL));
        lvNewAnnouncements.setHasFixedSize(true);

        createHeadings(view);

        // Get global data
        SharedPreferences global =  context.getSharedPreferences("globalData", MODE_PRIVATE);
        if (global.contains("student")) {
            //Get json from SP and decode json to EmployeeClass.
            SharedPreferences shared =  context.getSharedPreferences("studentData", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = shared.getString("studentObject", "");
            studentClass = gson.fromJson(json, StudentClass.class);
            studRank = studentClass.rank;


        } else {
            studRank = "0";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_event), new Response.Listener<String>() {
            @Override
            public void onResponse(String output) {
                if (!output.isEmpty()) {

                    ArrayList<CommunicationClass> data = new JsonConverter<CommunicationClass>().toArrayList(output, CommunicationClass.class);
                    CommunicationClass comm = data.get(0);

                    if (comm.code.equals("0")) {

                        // Create list for Events
                        ArrayList<EventClass> eventClassArrayList = new JsonConverter<EventClass>().toArrayList(comm.info, EventClass.class);

                        //Seperate Array lists
//                        ArrayList<EventClass> eventUpcomingArrayList = new ArrayList<>();
//                        ArrayList<EventClass> eventNewArrayList = new ArrayList<>();

                        for (EventClass event: eventClassArrayList) {
                            LocalDate localDate = new LocalDate().plusDays(1);
                            LocalDate eventLocalDate = new LocalDate(event.date);
                            LocalDate eventAddedLocalDate = new LocalDate(event.date_added);

                            if ( eventLocalDate.isAfter(localDate) && eventLocalDate.isBefore(localDate.plusWeeks(1)) ) {
                                eventUpcomingArrayList.add(event);
                            }

                            if ( localDate.isBefore(eventAddedLocalDate.plusWeeks(2)) && eventAddedLocalDate.isBefore(eventLocalDate) ) {
                                eventNewArrayList.add(event);
                            }
                        }


                        // Create and add events to EventsAdapter and add to TwoWayView
                        RecyclerView.Adapter eventUpcommingEventsAdapter = new EventsAdapter(context, eventUpcomingArrayList);
                        RecyclerView.Adapter eventNewEventsAdapter = new EventsAdapter(context,eventNewArrayList);

                        lvUpcommingEvents.setAdapter(eventUpcommingEventsAdapter);
                        lvNewEvents.setAdapter(eventNewEventsAdapter);

                        eventUpcommingEventsAdapter.notifyDataSetChanged();
                        eventNewEventsAdapter.notifyDataSetChanged();


                    } else {
                        ErrorHandling.makeText(context.getApplicationContext(),comm.code, false).show();
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
                Toasty.error(context.getApplicationContext(), message, Toast.LENGTH_SHORT,true).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //POST data for PHP Script
                HashMap<String, String> postDataEvents = new HashMap<String, String>();
                postDataEvents.put("eventNew", "true");
                if (studRank.equals("0")) {
                    postDataEvents.put("token", "public");
                } else {
                    postDataEvents.put("token", studentClass.token);
                }
                return postDataEvents;
            }


        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


        com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport itemUpcomingClickSupport = com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.addTo(lvUpcommingEvents);
        itemUpcomingClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("eventDetails",new Gson().toJson(eventUpcomingArrayList.get(position)));
                intent.putExtra("eventType","Upcoming");
                startActivity(intent);
            }
        });


        com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport itemNewClickSupport = com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.addTo(lvNewEvents);
        itemNewClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("eventDetails",new Gson().toJson(eventNewArrayList.get(position)));
                intent.putExtra("eventType","New");
                startActivity(intent);
            }
        });


        com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport itemAnnounceClickSupport = com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.addTo(lvNewAnnouncements);
        itemAnnounceClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });





        return view;
    }

}
