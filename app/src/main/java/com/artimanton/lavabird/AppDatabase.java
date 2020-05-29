package com.artimanton.lavabird;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NotifEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotifDao notifDao();
}
