package com.example.howldevelopment.bcofficial.models.api_classes;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marno on 2017/10/26.
 */

public class CommunicationClass implements Serializable{

    @SerializedName("code")
    public String code;

    @SerializedName("message")
    public String message;

    @SerializedName("info")
    public String info;

}
