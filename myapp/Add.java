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

import com.example.myapp.databinding.AddBinding;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Add extends AppCompatActivity {

    private AddBinding Binding;
    public static final String ADD_IMAGE = "image";
    public static final String ADD_NAME = "name";
    public static final String ADD_DATE = "date";
    public static final String ADD_SAVE = "save";
    public static final String ADD_MEMO = "memo";
    DatePickerDialog.OnDateSetListener b;
    Bitmap bt, btm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = AddBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());
        bt = null;
        btm = null;

        Binding.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) Binding.image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                Intent intent = new Intent();
                if (Binding.name.getText().toString().isEmpty()) {
                    Toast.makeText(Add.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (Binding.date.getText().toString().isEmpty()) {
                    Toast.makeText(Add.this, "유통기한을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(Binding.saves.getText().toString().isEmpty()){
                    Toast.makeText(Add.this, "저장위치를 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else if (bitmap.equals(bt)) {
                    intent.putExtra(ADD_IMAGE, dataconverter.image(bt));
                    intent.putExtra(ADD_NAME, Binding.name.getText().toString());
                    intent.putExtra(ADD_DATE, Binding.date.getText().toString());
                    intent.putExtra(ADD_SAVE, Binding.saves.getText().toString());
                    intent.putExtra(ADD_MEMO, Binding.memo.getText().toString());
                    setResult(RESULT_OK, intent);
                    Toast.makeText(Add.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (bitmap.equals(btm)) {
                    intent.putExtra(ADD_IMAGE, dataconverter.image(btm));
                    intent.putExtra(ADD_NAME, Binding.name.getText().toString());
                    intent.putExtra(ADD_DATE, Binding.date.getText().toString());
                    intent.putExtra(ADD_SAVE, Binding.saves.getText().toString());
                    intent.putExtra(ADD_MEMO, Binding.memo.getText().toString());
                    setResult(RESULT_OK, intent);
                    Toast.makeText(Add.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    intent.putExtra(ADD_IMAGE, dataconverter.image(bitmap));
                    intent.putExtra(ADD_NAME, Binding.name.getText().toString());
                    intent.putExtra(ADD_DATE, Binding.date.getText().toString());
                    intent.putExtra(ADD_SAVE, Binding.saves.getText().toString());
                    intent.putExtra(ADD_MEMO, Binding.memo.getText().toString());
                    setResult(RESULT_OK, intent);
                    Toast.makeText(Add.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });

        Binding.saves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(Add.this);
                dlg.setTitle("저장위치");
                final String[] array = new String[]{"냉동실", "냉장실", "실온"};
                dlg.setIcon(R.mipmap.ic_launcher);
                dlg.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Binding.saves.setText(array[i]);
                    }
                });
                dlg.show();
            }
        });
        Binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image();
            }

        });
        Binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });
        Binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Add.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, b, yy, mm, dd);
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
                Binding.date.setText(day);
            }
        };



    }

    final int CAMERA = 101;
    final int GALLERY = 102;

    public void image() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(Add.this.getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                bt = (Bitmap) data.getExtras().get("data");
                Binding.image.setImageBitmap(bt);
            }
        }
        if (requestCode == GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();

                ContentResolver resolver = getContentResolver();

                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    btm = BitmapFactory.decodeStream(instream);
                    Binding.image.setImageBitmap(btm);
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

