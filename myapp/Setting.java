package com.example.myapp;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.databinding.SettingBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class Setting extends AppCompatActivity {

    private SettingBinding Binding;
    private SharedPreferences sharedPreferences;
    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;
    private NotificationManager notificationManager;
    TimePickerDialog.OnTimeSetListener c;
    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    private AppDatabase db = AppDatabase.getDbInstance(this);
    private List<App> one = db.appDao().getall("-1 day");
    private List<App> two = db.appDao().getall("-2 day");
    private List<App> three = db.appDao().getall("-3 day");
    private int th = 0, tm = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = SettingBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());

        sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        Binding.onoff.setChecked(sharedPreferences.getBoolean("switch", false));
        Binding.onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    editor.putBoolean("switch", true);
                } else {
                    editor.putBoolean("switch", false);
                }
                editor.commit();
            }
        });
        String day = sharedPreferences.getString("day", "");
        String time = sharedPreferences.getString("time", "");
        Binding.day.setText(day);
        Binding.time.setText(time);


        Binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int h = calendar.get(Calendar.HOUR_OF_DAY);
                int m = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(Setting.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, c, h, m, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.updateTime(h, m);
                dialog.show();
            }
        });

        c = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {

                th = h;
                tm = m;
                String date = h + ":" + m;
                SimpleDateFormat f24 = new SimpleDateFormat("HH:mm");
                try {
                    Date day = f24.parse(date);
                    SimpleDateFormat f12 = new SimpleDateFormat("aa hh:mm");
                    Binding.time.setText(f12.format(day));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        Binding.day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Setting.this);
                dlg.setTitle("알림날짜");
                final String[] array = new String[]{"1일전", "2일전", "3일전"};
                dlg.setIcon(R.mipmap.ic_launcher);
                dlg.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Binding.day.setText(array[i]);

                        if (Binding.onoff.isChecked() && !one.isEmpty() && Binding.day.getText().toString().equals("1일전")) {
                            String name = one.get(0).getName();
                            String save = one.get(0).getSave();
                            alarm(save + "에 " + name + "의 유통기한이 1일 남았습니다. 그 외 " + (one.size() - 1) + "건이 있습니다.");
                        } else if (Binding.onoff.isChecked() && !two.isEmpty() && Binding.day.getText().toString().equals("2일전")) {
                            String name = two.get(0).getName();
                            String save = two.get(0).getSave();
                            alarm(save + "에 " + name + "의 유통기한이 2일 남았습니다. 그 외 " + (two.size() - 1) + "건이 있습니다.");
                        } else if (Binding.onoff.isChecked() && !three.isEmpty() && Binding.day.getText().toString().equals("3일전")) {
                            String name = three.get(0).getName();
                            String save = three.get(0).getSave();
                            alarm(save + "에 " + name + "의 유통기한이 3일 남았습니다. 그 외 " + (three.size() - 1) + "건이 있습니다.");
                        }
                    }
                });
                dlg.show();
            }
        });

    }

    private void alarm(String msg) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        mCalender = new GregorianCalendar();


        Intent receiverIntent = new Intent(Setting.this, AlarmRecevier.class);
        receiverIntent.putExtra("msg", msg);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Setting.this,
                0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        if (th == 0 & tm == 0) {
            calendar.set(calendar.HOUR_OF_DAY, 8);
            calendar.set(calendar.MINUTE, 0);
            calendar.set(calendar.SECOND, 0);
        } else {
            calendar.set(calendar.HOUR_OF_DAY, th);
            calendar.set(calendar.MINUTE, tm);
            calendar.set(calendar.SECOND, 0);
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String day = Binding.day.getText().toString();
        String time = Binding.time.getText().toString();
        editor.putString("day", day);
        editor.putString("time", time);
        editor.commit();
    }

    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.in,R.anim.out);
    }

}

