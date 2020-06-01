package com.artimanton.lavabird.model;

import android.graphics.Bitmap;
import android.media.Image;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotifEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String packages;
    public String title;
    public String text;
    public String date;
    public String time;
    public byte[] byteArray;
}
