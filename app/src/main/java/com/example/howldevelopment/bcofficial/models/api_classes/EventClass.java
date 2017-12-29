package com.example.howldevelopment.bcofficial.models.api_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marno on 2017/11/01.
 */

public class EventClass {

    @SerializedName("event_id")
    @Expose
    public String eventId;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("date")
    @Expose
    public String date;

    @SerializedName("date_added")
    @Expose
    public String date_added;

    @SerializedName("cost")
    @Expose
    public String cost;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("access")
    @Expose
    public String access;

    @SerializedName("time")
    @Expose
    public String time;

    @SerializedName("erank")
    @Expose
    public String erank;

    @SerializedName("more_details")
    @Expose
    public String moreDetails;

    @SerializedName("place")
    @Expose
    public String place;

    @SerializedName("interested")
    @Expose
    public String interested;

    @SerializedName("picture")
    @Expose
    public String picture;

    @SerializedName("postprocessing")
    @Expose
    public String postprocessing;

}
