package com.artimanton.lavabird.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotifEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String packages;
    public String title;
    public String text;
}