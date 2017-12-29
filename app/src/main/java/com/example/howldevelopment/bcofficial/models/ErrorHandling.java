package com.example.howldevelopment.bcofficial.models;

import android.content.Context;
import android.support.annotation.IntDef;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.howldevelopment.bcofficial.activities.MenuActivity;

import es.dmoral.toasty.Toasty;

/**
 * Created by Marno on 2017/10/30.
 */

public class ErrorHandling {

    private String code;
    private Context context;
    private Boolean defaultToast = true;
    private VolleyError volleyError;


    private ErrorHandling(Context context,String code ) {
        this.code = code;
        this.context = context;
    }

    private ErrorHandling(Context context,String code, Boolean defaultToast ) {
        this.code = code;
        this.context = context;
        this.defaultToast = defaultToast;
    }

    private ErrorHandling(Context context, int warning, Boolean defaultToast ) {
        this.code = code;
        this.context = context;
        this.defaultToast = defaultToast;
    }

    private ErrorHandling(Context context, VolleyError volleyError ) {
        this.volleyError = volleyError;
        this.context = context;
    }

    public static ErrorHandling makeText(Context context, String code) {
        return new ErrorHandling(context, code);
    }

    public static ErrorHandling makeText(Context context, String code, Boolean defaultToast) {
        return new ErrorHandling(context, code, defaultToast);
    }

    public static ErrorHandling makeText(Context context, VolleyError volleyError) {
        return new ErrorHandling(context, volleyError);
    }


    public void show() {
        String message = "";
        switch (code) {
            case "1":  message = "False Data";
                break;
            case "2":  message = "Incorrect ID Number OR No user";
                break;
            case "3":  message = "Not an active student or Banned";
                break;
            case "4":  message = "Authentication update failed";
                break;
            case "5":  message = "Unknown Token";
                break;
            case "80":  message = "No Rank Found.";
                break;
            case "81":  message = "Menu Item not activated";
                break;
            case "99":  message = "Database Connection Failed";
                break;
            default:  message = "Unknown Problem";
                break;
        }

        if (defaultToast) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            Toasty.error(context, message, Toast.LENGTH_SHORT,true).show();
        }

    }

    public void showVolley() {
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
        Toasty.error(context, message, Toast.LENGTH_SHORT,true).show();
    }

    public void showWarning() {
        String message = "";
        switch (code) {
            case "1":  message = "Voting Polls not open ";
                break;
            case "2":  message = " not open ";
                break;
            case "3":  message = " not open ";
                break;
            case "89":  message = "This menu item is not activated by an Administrator";
                break;
            case "99":  message = "Could not fetch settings. Reverting to default";
                break;
            default:  message = "Unknown Warning";
                break;
        }

        if (defaultToast) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            Toasty.warning(context, message, Toast.LENGTH_SHORT,true).show();
        }
    }

}
