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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.howldevelopment.bcofficial.R;
import com.example.howldevelopment.bcofficial.activities.EventDetailActivity;
import com.example.howldevelopment.bcofficial.adapters.EventsAdapter;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    ArrayList<EventClass> eventClassArrayList;
    RecyclerView.Adapter eventClassEventsAdapter;
    TwoWayView lvSocialEvents;
    TwoWayView lvSportEvents;
    TwoWayView lvOfficialEvents;
    TwoWayView lvClosedEvents;
    StudentClass studentClass;
    View masterView;
    String studRank;
    Context context;
    ArrayList<EventClass> eventSocialArrayList = new ArrayList<>();
    ArrayList<EventClass> eventSportArrayList = new ArrayList<>();
    ArrayList<EventClass> eventOfficialArrayList = new ArrayList<>();
    ArrayList<EventClass> eventClosedArrayList = new ArrayList<>();



    public EventsFragment() {
        // Required empty public constructor
    }

    private void createHeadings(View view) {
        TextView lvHeaderSocialEvents = (TextView) view.findViewById(R.id.tvHeaderSocialEvents);
        lvHeaderSocialEvents.setTypeface(EasyFonts.caviarDreamsBold(context));
        TextView lvHeaderSportEvents = (TextView) view.findViewById(R.id.tvHeaderSportEvents);
        lvHeaderSportEvents.setTypeface(EasyFonts.caviarDreamsBold(context));
        TextView lvHeaderOfficialEvents = (TextView) view.findViewById(R.id.tvHeaderOfficialEvents);
        lvHeaderOfficialEvents.setTypeface(EasyFonts.caviarDreamsBold(context));
        TextView lvHeaderClosedEvents = (TextView) view.findViewById(R.id.tvHeaderClosedEvents);
        lvHeaderClosedEvents.setTypeface(EasyFonts.caviarDreamsBold(context));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        masterView = view;


        // Get Application Context
        context = getContext().getApplicationContext();
        Fresco.initialize(context);


        // Assign components
        lvSocialEvents = (TwoWayView) view.findViewById(R.id.lvSocialEvents);
        lvSocialEvents.setLayoutManager(new ListLayoutManager(context, TwoWayLayoutManager.Orientation.HORIZONTAL));
        lvSocialEvents.setHasFixedSize(true);

        lvSportEvents = (TwoWayView) view.findViewById(R.id.lvSportEvents);
        lvSportEvents.setLayoutManager(new ListLayoutManager(context, TwoWayLayoutManager.Orientation.HORIZONTAL));
        lvSportEvents.setHasFixedSize(true);

        lvOfficialEvents = (TwoWayView) view.findViewById(R.id.lvOfficialEvents);
        lvOfficialEvents.setLayoutManager(new ListLayoutManager(context, TwoWayLayoutManager.Orientation.HORIZONTAL));
        lvOfficialEvents.setHasFixedSize(true);

        lvClosedEvents = (TwoWayView) view.findViewById(R.id.lvClosedEvents);
        lvClosedEvents.setLayoutManager(new ListLayoutManager(context, TwoWayLayoutManager.Orientation.HORIZONTAL));
        lvClosedEvents.setHasFixedSize(true);

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


        // Get all the events from the Web server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_event), new Response.Listener<String>() {
            @Override
            public void onResponse(String output) {
                if (!output.isEmpty()) {
                    ArrayList<CommunicationClass> data = new JsonConverter<CommunicationClass>().toArrayList(output, CommunicationClass.class);
                    CommunicationClass comm = data.get(0);

                    if (comm.code.equals("0")) {

                        // Create list for Events
                        eventClassArrayList = new JsonConverter<EventClass>().toArrayList(comm.info, EventClass.class);

//                        ArrayList<EventClass> eventSocialArrayList = new ArrayList<>();
//                        ArrayList<EventClass> eventSportArrayList = new ArrayList<>();
//                        ArrayList<EventClass> eventOfficialArrayList = new ArrayList<>();
//                        ArrayList<EventClass> eventClosedArrayList = new ArrayList<>();

                        for (EventClass event:eventClassArrayList) {
                            LocalDate localDate = new LocalDate(event.date);
                            LocalDate localDateNow = new LocalDate().plusDays(1);
                            if (localDateNow.isAfter(localDate)) {
                                eventClosedArrayList.add(event);
                            } else {
                                if (event.type.equals("social")) {
                                    eventSocialArrayList.add(event);
                                } else if (event.type.equals("sport")) {
                                    eventSportArrayList.add(event);
                                } else if (event.type.equals("official")) {
                                    eventOfficialArrayList.add(event);
                                }
                            }

                        }

                        // Create and add events to EventsAdapter and add to TwoWayView
                        //eventClassEventsAdapter = new EventsAdapter(context,eventClassArrayList);
                        RecyclerView.Adapter eventSocialEventsAdapter = new EventsAdapter(context,eventSocialArrayList);
                        RecyclerView.Adapter eventSportEventsAdapter = new EventsAdapter(context,eventSportArrayList);
                        RecyclerView.Adapter eventOfficialEventsAdapter = new EventsAdapter(context,eventOfficialArrayList);
                        RecyclerView.Adapter eventClosedEventsAdapter = new EventsAdapter(context,eventClosedArrayList);


                        //lvSocialEvents.setAdapter(eventClassEventsAdapter);
                        lvSocialEvents.setAdapter(eventSocialEventsAdapter);
                        lvSportEvents.setAdapter(eventSportEventsAdapter);
                        lvOfficialEvents.setAdapter(eventOfficialEventsAdapter);
                        lvClosedEvents.setAdapter(eventClosedEventsAdapter);

                        //eventClassEventsAdapter.notifyDataSetChanged();
                        eventSocialEventsAdapter.notifyDataSetChanged();
                        eventSportEventsAdapter.notifyDataSetChanged();
                        eventOfficialEventsAdapter.notifyDataSetChanged();
                        eventClosedEventsAdapter.notifyDataSetChanged();

                    } else {
                        ErrorHandling.makeText(context,comm.code, false).show();
                    }
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ErrorHandling.makeText(context, volleyError).showVolley();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //POST data for PHP Script
                HashMap<String, String> postDataEvents = new HashMap<String, String>();
                postDataEvents.put("event", "true");
                if (studRank.equals("0")) {
                    postDataEvents.put("token", "public");
                } else {
                    postDataEvents.put("token", studentClass.token);
                }
                return postDataEvents;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

        com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport itemSocialClickSupport = com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.addTo(lvSocialEvents);
        itemSocialClickSupport.setOnItemClickListener(new com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(context, eventSocialArrayList.get(position).name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("eventDetails",new Gson().toJson(eventSocialArrayList.get(position)));
                intent.putExtra("eventType","social");
                startActivity(intent);
            }
        });
        com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport itemSportClickSupport = com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.addTo(lvSportEvents);
        itemSportClickSupport.setOnItemClickListener(new com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(context, eventSportArrayList.get(position).name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("eventDetails",new Gson().toJson(eventSportArrayList.get(position)));
                intent.putExtra("eventType","sport");
                startActivity(intent);
            }
        });
        com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport itemOfficialClickSupport = com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.addTo(lvOfficialEvents);
        itemOfficialClickSupport.setOnItemClickListener(new com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(context, eventOfficialArrayList.get(position).name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("eventDetails",new Gson().toJson(eventOfficialArrayList.get(position)));
                intent.putExtra("eventType","official");
                startActivity(intent);
            }
        });
        com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport itemClosedClickSupport = com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.addTo(lvClosedEvents);
        itemClosedClickSupport.setOnItemClickListener(new com.example.howldevelopment.bcofficial.interfaces.ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(context, eventClosedArrayList.get(position).name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("eventDetails",new Gson().toJson(eventClosedArrayList.get(position)));
                intent.putExtra("eventType","closed");
                startActivity(intent);

            }
        });

        return view;
    }

}
