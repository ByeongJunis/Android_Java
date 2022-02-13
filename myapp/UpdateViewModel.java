package com.example.myapp;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UpdateViewModel extends AndroidViewModel {

    public AppDao appDao;
    public AppDatabase db;

    public UpdateViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDbInstance(application);
        appDao = db.appDao();
    }

    public LiveData<App> getapp(String appId) {
        return appDao.getapp(appId);
    }
}

