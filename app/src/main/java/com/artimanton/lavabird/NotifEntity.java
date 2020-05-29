package com.artimanton.lavabird;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotifEntity {
    @PrimaryKey
    public long id;
    public String packages;
    public String title;
    public String text;
}
