package com.example.howldevelopment.bcofficial.models.firebase;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Marno on 2017/12/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,refreshedToken);


        //Send the new token to the server when the token is refreshed.
        //sendRegistrationToServer(refreshedToken);
    }


}
