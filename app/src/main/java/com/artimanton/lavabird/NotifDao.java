package com.artimanton.lavabird;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotifDao {
    @Query("SELECT * FROM notifEntity")
    List<NotifEntity> getAll();

    @Query("SELECT * FROM notifEntity WHERE id = :id")
    NotifEntity getById(long id);

    @Insert
    void insert(NotifEntity notifEntity);

    @Update
    void update(NotifEntity notifEntity);

    @Delete
    void delete(NotifEntity notifEntity);
}
