package com.example.myapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class dataconverter {

    public static byte[] image(Bitmap bitmap){ //비트맵 -> 바이트
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();
    }
    public static Bitmap bitimage(byte [] array){ // 바이트 -> 비트맵
        return BitmapFactory.decodeByteArray(array,0,array.length);
    }


}


