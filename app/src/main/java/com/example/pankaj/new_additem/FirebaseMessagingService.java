package com.example.pankaj.new_additem;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by pankaj on 26/3/18.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

        super.onMessageReceived(remoteMessage);
        String notificationTitle=remoteMessage.getNotification().getTitle();
        String notificationBody=remoteMessage.getNotification().getBody();

        String messagedata=remoteMessage.getData().get("message");
        String userId=remoteMessage.getData().get("fromid");
        String postid=remoteMessage.getData().get("postid");

        String click_action=remoteMessage.getNotification().getClickAction();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("message",messagedata);
        resultIntent.putExtra("fromid",userId);
        resultIntent.putExtra("postid",postid);

        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);


        int Notificationid=(int) System.currentTimeMillis();

        NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Notificationid, mBuilder.build());

    }
}
