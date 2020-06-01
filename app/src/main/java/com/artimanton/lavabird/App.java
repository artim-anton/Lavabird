package com.artimanton.lavabird;

import android.app.Application;

import androidx.room.Room;

import com.artimanton.lavabird.model.NotifDao;

public class App extends Application {
    public static App instance;

    private AppDatabase database;
    private NotifDao notifDao;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();

        notifDao = database.notifDao();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public NotifDao getNotifDao() {
        return notifDao;
    }
}
