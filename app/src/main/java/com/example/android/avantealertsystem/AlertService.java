package com.example.android.avantealertsystem;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class AlertService extends Service {
    public AlertService() {
    }

    private int notificationID;
    private ArrayList<JSONObject> jsonArray = new ArrayList<>();
    PendingIntent pendingIntent;
    @Override
    public void onCreate() {


        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("login");
        backgroundWorker.execute("getAlerts");

        if(jsonArray.equals(UserDetails.activeAlerts)) {

            return;
        }else {

            createNotificationChannel();
            notificationID = 0;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        pendingIntent =  PendingIntent.getActivity(this,1,new Intent(this, MainActivity.class),0);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"CHANNEL_ID1")
                            .setSmallIcon(R.mipmap.ic_avante_logo)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("New Alerts"))
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationId is a unique int for each notification that you must define
                    notificationID = notificationID +1;
                    notificationManager.notify(notificationID, builder.build());
                }
                return START_REDELIVER_INTENT;
    }









    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID1", "ChannelCustom", importance);
            channel.setDescription("CustomChannel");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
