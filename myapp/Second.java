package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.databinding.SecondBinding;

public class Second extends AppCompatActivity {

    private SecondBinding Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = SecondBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());

        Binding.card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Second.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in,R.anim.out);
            }
        });

        Binding.card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Second.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in,R.anim.out);
            }
        });

        Binding.card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Second.this, MainActivity3.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in,R.anim.out);
            }
        });


    }
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.down2,R.anim.up2);
    }



}
