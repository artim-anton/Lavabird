package com.artimanton.lavabird.services;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


import androidx.room.Room;

import com.artimanton.lavabird.App;
import com.artimanton.lavabird.AppDatabase;
import com.artimanton.lavabird.model.NotifDao;
import com.artimanton.lavabird.model.NotifEntity;

import java.io.ByteArrayOutputStream;


public class NotificationListener extends android.service.notification.NotificationListenerService {
    Context context;
    private NotifDao notifDao;
    private AppDatabase db;

    @Override

    public void onCreate() {
        db = App.getInstance().getDatabase();
        db =  Room.databaseBuilder(this, AppDatabase.class, "MyDatabase").allowMainThreadQueries().build();
        notifDao = db.notifDao();

        super.onCreate();
        context = getApplicationContext();

    }
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        String ticker ="";
        if(sbn.getNotification().tickerText !=null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();
        int id1 = extras.getInt(Notification.EXTRA_SMALL_ICON);
        //Bitmap id = sbn.getNotification().largeIcon;
        Bitmap id = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);

        NotifEntity notifEntity = new NotifEntity();
        notifEntity.packages = pack;
        notifEntity.title = title;
        notifEntity.text = text;

        notifDao.insert(notifEntity);

        Log.i("Package",pack);
        Log.i("Ticker",ticker);
        Log.i("Title",title);
        Log.i("Text",text);

        Intent msgrcv = new Intent("com.artimanton.lavabird");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);
        if(id != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            id.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            msgrcv.putExtra("icon", byteArray);
        }
        //LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        sendBroadcast(msgrcv);


    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}
