package com.example.pankaj.new_additem;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by pankaj on 26/3/18.
 */

public class MyfirebaseInstance extends FirebaseInstanceIdService {

    public String Token;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println(refreshedToken);
        Log.d("Telicom", "Refreshed token: " + refreshedToken);
        Token=refreshedToken;

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
    public String gettoken()
    {
        System.out.println(Token);
        return Token;
    }
}
