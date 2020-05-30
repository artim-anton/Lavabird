package com.artimanton.lavabird;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.artimanton.lavabird.model.NotifDao;
import com.artimanton.lavabird.model.NotifEntity;

@Database(entities = {NotifEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotifDao notifDao();
}
