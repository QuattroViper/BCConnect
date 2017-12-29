package com.example.howldevelopment.bcofficial.models.api_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marno on 2017/11/06.
 */

public class AppSettingsClass {

    public AppSettingsClass() {
        this.appId = "0";
        this.itemEvents = "1";
        this.itemComplaint = "0";
        this.itemEnquire = "0";
        this.itemAnnoucements = "1";
        this.itemInformation = "1";
        this.itemBelgiumMap = "1";
        this.itemMarkTracker = "1";
        this.itemAddEvent = "0";
        this.itemItemMakeAnnouncement = "0";
        this.itemMrMissBc = "0";
        this.itemTalentShow = "0";
    }

    @SerializedName("app_id")
    @Expose
    public String appId;

    @SerializedName("item_events")
    @Expose
    public String itemEvents;

    @SerializedName("item_complaint")
    @Expose
    public String itemComplaint;

    @SerializedName("item_enquire")
    @Expose
    public String itemEnquire;

    @SerializedName("item_annoucements")
    @Expose
    public String itemAnnoucements;

    @SerializedName("item_information")
    @Expose
    public String itemInformation;

    @SerializedName("item_belgium_map")
    @Expose
    public String itemBelgiumMap;

    @SerializedName("item_mark_tracker")
    @Expose
    public String itemMarkTracker;

    @SerializedName("item_add_event")
    @Expose
    public String itemAddEvent;

    @SerializedName("item_item_make_announcement")
    @Expose
    public String itemItemMakeAnnouncement;

    @SerializedName("item_mr_miss_bc")
    @Expose
    public String itemMrMissBc;

    @SerializedName("item_talent_show")
    @Expose
    public String itemTalentShow;
}
