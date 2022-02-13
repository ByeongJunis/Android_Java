package com.example.myapp;

import android.graphics.Bitmap;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class App {
    @PrimaryKey
    @NonNull
    String id;
    @NonNull
    @ColumnInfo(name = "name")
    String name;
    @NonNull
    @ColumnInfo(name = "dates")
    String dates;
    @NonNull
    @ColumnInfo(name = "save")
    String save;
    @NonNull
    @ColumnInfo(name = "memo")
    String memo;
    @NonNull
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    byte [] image;

    public App(String id,byte[] image, String name, String dates, String save, String memo) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.dates = dates;
        this.save = save;
        this.memo = memo;
    }

    @NonNull
    public String getMemo() {
        return memo;
    }

    public void setMemo(@NonNull String memo) {
        this.memo = memo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return dates;
    }

    public void setDate(String dates) {
        this.dates = dates;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "App{" +
                "name='" + name + '\'' +
                ", dates='" + dates + '\'' +
                '}';
    }


}

