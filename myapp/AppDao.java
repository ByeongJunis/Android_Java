package com.example.myapp;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface AppDao {
    @Query("SELECT * FROM App WHERE save=='냉동실'")
    LiveData<List<App>> getList();

    @Query("SELECT * FROM App WHERE save=='냉장실'")
    LiveData<List<App>> getList1();

    @Query("SELECT * FROM App WHERE save=='실온'")
    LiveData<List<App>> getList2();

    @Query("SELECT * FROM App WHERE id=:appId")
    LiveData<App> getapp(String appId);

    @Query("SELECT * FROM App WHERE date(dates,:dday,'localtime')=date('now','localtime')")
    List<App> getall(String dday);



    @Insert
    void insert(App app);

    @Update
    void update(App app);

    @Delete
    void delete(App app);

}

