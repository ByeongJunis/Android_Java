package com.example.myapp;


import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

@SuppressWarnings("ALL")
public class AppViewModel extends AndroidViewModel {

    private AppDao appDao;
    private AppDatabase appDB;
    private LiveData<List<App>> Allapp;
    private LiveData<List<App>> Allapp1;
    private LiveData<List<App>> Allapp2;

    public AppViewModel(Application application) {
        super(application);

        appDB = AppDatabase.getDbInstance(application);
        appDao = appDB.appDao();
        Allapp = appDao.getList();
        Allapp1 = appDao.getList1();
        Allapp2 = appDao.getList2();
    }

    public void insert(App app) {
        new Insert(appDao).execute(app);
    }

    LiveData<List<App>> getList() {
        return Allapp;
    }
    LiveData<List<App>> getList1() {
        return Allapp1;
    }
    LiveData<List<App>> getList2() {
        return Allapp2;
    }

    public void update(App app) {
        new Update(appDao).execute(app);
    }

    public void delete(App app) {
        new Delete(appDao).execute(app);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private class Operations extends AsyncTask<App, Void, Void> { // 백그라운드 작업
                                                                    //비동기 작업
        AppDao TaskDao;

        Operations(AppDao dao) {
            this.TaskDao = dao;
        }

        @Override
        protected Void doInBackground(App... apps) {
            return null;
        }
    }

    private class Insert extends Operations {

        Insert(AppDao appDao) {
            super(appDao);
        }

        @Override
        protected Void doInBackground(App... apps) {
            TaskDao.insert(apps[0]);
            return null;
        }
    }

    private class Update extends Operations {

        Update(AppDao appDao) {
            super(appDao);
        }

        @Override
        protected Void doInBackground(App... apps) {
            TaskDao.update(apps[0]);
            return null;
        }
    }

    private class Delete extends Operations {

        public Delete(AppDao appDao) {
            super(appDao);
        }

        @Override
        protected Void doInBackground(App... apps) {
            TaskDao.delete(apps[0]);
            return null;
        }
    }
}
