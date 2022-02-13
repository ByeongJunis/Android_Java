package com.example.myapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.myapp.databinding.ActivityMain2Binding;

import java.util.List;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity implements AppAdapter.OnDeleteClickListener, View.OnClickListener {

    private ActivityMain2Binding Binding;
    public AppAdapter adapter;
    public AppViewModel appViewModel;
    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        Binding.fab.setOnClickListener(this);
        Binding.fab1.setOnClickListener(this);
        Binding.fab2.setOnClickListener(this);


        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
        Binding.recyclerView2.setLayoutManager(layoutManager);

        adapter = new AppAdapter(this, this);
        Binding.recyclerView2.setAdapter(adapter);

        appViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(AppViewModel.class);
        appViewModel.getList1().observe(this, new Observer<List<App>>() {
            @Override
            public void onChanged(@Nullable List<App> apps) {
                adapter.setApps(apps);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.NEW_APP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            final String app_id = UUID.randomUUID().toString();
            App app = new App(app_id, data.getByteArrayExtra(Add.ADD_IMAGE),
                    data.getStringExtra(Add.ADD_NAME),
                    data.getStringExtra(Add.ADD_DATE),
                    data.getStringExtra(Add.ADD_SAVE),
                    data.getStringExtra(Add.ADD_MEMO));
            appViewModel.insert(app);

        } else if (requestCode == MainActivity.UPDATE_APP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            App app = new App(
                    data.getStringExtra(Update.APP_ID),
                    data.getByteArrayExtra(Update.UPDATED_IMAGE),
                    data.getStringExtra(Update.UPDATED_NAME),
                    data.getStringExtra(Update.UPDATED_DATE),
                    data.getStringExtra(Update.UPDATED_SAVE),
                    data.getStringExtra(Update.UPDATED_MEMO));
            appViewModel.update(app);
        }

    }



    @Override
    public void OnDeleteClickListener(App app) {
        appViewModel.delete(app);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();
                break;
            case R.id.fab1:
                anim();
                Intent intent = new Intent(MainActivity2.this, Setting.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
            case R.id.fab2:
                anim();
                Intent intent2 = new Intent(MainActivity2.this, Add.class);
                startActivityForResult(intent2, MainActivity.NEW_APP_ACTIVITY_REQUEST_CODE);
                overridePendingTransition(R.anim.in, R.anim.out);
                break;
        }

    }

    public void anim() {

        if (isFabOpen) {
            Binding.fab1.startAnimation(fab_close);
            Binding.fab2.startAnimation(fab_close);
            Binding.fab1.setClickable(false);
            Binding.fab2.setClickable(false);
            isFabOpen = false;
        } else {
            Binding.fab1.startAnimation(fab_open);
            Binding.fab2.startAnimation(fab_open);
            Binding.fab1.setClickable(true);
            Binding.fab2.setClickable(true);
            isFabOpen = true;
        }
    }
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.in,R.anim.out);
    }
}