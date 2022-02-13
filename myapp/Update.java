package com.example.myapp;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapp.databinding.UpdateBinding;

import java.io.InputStream;
import java.util.Calendar;


public class Update extends AppCompatActivity {

    public static final String APP_ID = "id";
    public static final String UPDATED_IMAGE = "uimage";
    public static final String UPDATED_NAME = "uname";
    public static final String UPDATED_DATE = "udate";
    public static final String UPDATED_SAVE = "usave";
    public static final String UPDATED_MEMO = "umemo";
    private UpdateBinding Binding;
    public UpdateViewModel updateModel;
    public LiveData<App> app;
    private String appId;
    private Bundle bundle;
    DatePickerDialog.OnDateSetListener b;
    Bitmap bt, btm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = UpdateBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());

        bundle = getIntent().getExtras();

        if (bundle != null) {
            appId = bundle.getString("id");
        }

        updateModel = new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UpdateViewModel.class);
        app = updateModel.getapp(appId);
        app.observe(this, new Observer<App>() {
            @Override
            public void onChanged(@Nullable App app) {
                Binding.uimage.setImageBitmap(dataconverter.bitimage(app.getImage()));
                Binding.uname.setText(app.getName());
                Binding.udate.setText(app.getDate());
                Binding.usaves.setText(app.getSave());
                Binding.umemo.setText(app.getMemo());
            }
        });

        Binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) Binding.uimage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                Intent intent = new Intent();
                if (Binding.uname.getText().toString().isEmpty()) {
                    Toast.makeText(Update.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (bitmap.equals(bt)) {
                    intent.putExtra(APP_ID,appId);
                    intent.putExtra(UPDATED_IMAGE, dataconverter.image(bt));
                    intent.putExtra(UPDATED_NAME, Binding.uname.getText().toString());
                    intent.putExtra(UPDATED_DATE, Binding.udate.getText().toString());
                    intent.putExtra(UPDATED_SAVE, Binding.usaves.getText().toString());
                    intent.putExtra(UPDATED_MEMO, Binding.umemo.getText().toString());
                    setResult(RESULT_OK, intent);
                    Toast.makeText(Update.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (bitmap.equals(btm)) {
                    intent.putExtra(APP_ID,appId);
                    intent.putExtra(UPDATED_IMAGE, dataconverter.image(btm));
                    intent.putExtra(UPDATED_NAME, Binding.uname.getText().toString());
                    intent.putExtra(UPDATED_DATE, Binding.udate.getText().toString());
                    intent.putExtra(UPDATED_SAVE, Binding.usaves.getText().toString());
                    intent.putExtra(UPDATED_MEMO, Binding.umemo.getText().toString());
                    setResult(RESULT_OK, intent);
                    Toast.makeText(Update.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    intent.putExtra(APP_ID,appId);
                    intent.putExtra(UPDATED_IMAGE, dataconverter.image(bitmap));
                    intent.putExtra(UPDATED_NAME, Binding.uname.getText().toString());
                    intent.putExtra(UPDATED_DATE, Binding.udate.getText().toString());
                    intent.putExtra(UPDATED_SAVE, Binding.usaves.getText().toString());
                    intent.putExtra(UPDATED_MEMO, Binding.umemo.getText().toString());
                    setResult(RESULT_OK, intent);
                    Toast.makeText(Update.this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


        Binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Binding.usaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Update.this);
                dlg.setTitle("저장위치");
                final String[] array = new String[]{"냉동실","냉장실","실온"};
                dlg.setIcon(R.mipmap.ic_launcher);
                dlg.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Binding.usaves.setText(array[i]);
                    }
                });
                dlg.show();
            }
        });

        Binding.udate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Update.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, b, yy, mm, dd);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        b = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                mm = mm + 1;
                String smm = mm < 10? "0"+mm: ""+mm;
                String sdd = dd < 10? "0"+dd: ""+dd;
                String day = yy + "-" + smm + "-" + sdd;
                Binding.udate.setText(day);
            }
        };

        Binding.ucamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image();
            }

        });
        Binding.ugallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });

    }
    final int CAMERA = 101;
    final int GALLERY = 102;

    public void image() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(Update.this.getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                bt = (Bitmap) data.getExtras().get("data");
                Binding.uimage.setImageBitmap(bt);
            }
        }
        if (requestCode == GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();

                ContentResolver resolver = getContentResolver();

                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    btm = BitmapFactory.decodeStream(instream);
                    Binding.uimage.setImageBitmap(btm);
                    instream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY);
    }

    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.in,R.anim.out);
    }



}

