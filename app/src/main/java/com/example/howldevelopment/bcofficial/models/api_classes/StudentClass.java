package com.example.howldevelopment.bcofficial.models.api_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Marno on 2017/10/26.
 */

public class StudentClass implements Serializable {


    @SerializedName("student_id")
    @Expose
    public String studentId;

    @SerializedName("identification_number")
    @Expose
    public String identificationNumber;

    @SerializedName("first_name")
    @Expose
    public String firstName;

    @SerializedName("last_name")
    @Expose
    public String lastName;

    @SerializedName("google_services_id")
    @Expose
    public String googleServicesId;

    @SerializedName("department")
    @Expose
    public String department;

    @SerializedName("rank")
    @Expose
    public String rank;

    @SerializedName("session_id")
    @Expose
    public String sessionId;

    @SerializedName("browser_password")
    @Expose
    public String browserPassword;

    @SerializedName("active")
    @Expose
    public String active;

    @SerializedName("session_token")
    @Expose
    public String sessionToken;

    @SerializedName("token")
    @Expose
    public String token;

}
