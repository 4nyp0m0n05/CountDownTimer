package com.example.applist.countdowntimer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

public class Receiver extends BroadcastReceiver {
    private static final String NOTIF_ID = "deneme";
    private static final String NOTIF_CHANNEL_ID = "channelD";
    @Override
    public void onReceive(Context context, Intent intent) {
        Datum datum=(Datum)intent.getSerializableExtra("datum");//from Service

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context , NOTIF_ID ) ;
        boolean connected = true;
        builder.setContentTitle( datum.getDate().toString() ) ;

        builder.setContentText( datum.getInformation().replace(',',' ') ) ;
        builder.setSmallIcon(R.drawable.ic_launcher_background) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIF_CHANNEL_ID ) ;
        Notification notification = builder.build() ;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES.O ) {
            int importance = NotificationManager.IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIF_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;
        if ( connected ) {
            notificationManager.notify( 1 , notification) ;
            connected = false;
        } else {
            notificationManager.cancel( 1 ) ;
            connected = true;
        }
    }

}
