package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.myapp.databinding.FirstBinding;

public class First extends AppCompatActivity {

    private Back back;
    private FirstBinding Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FirstBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());

        back = new Back(this);

        GlideDrawableImageViewTarget gif = new GlideDrawableImageViewTarget(Binding.gif);
        Glide.with(this).load(R.drawable.points).into(gif);

        Binding.gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(First.this, Second.class);
                startActivity(intent);
                overridePendingTransition(R.anim.up,R.anim.down);
            }
        });

        Binding.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(First.this, Second.class);
                startActivity(intent);
                overridePendingTransition(R.anim.up,R.anim.down);
            }
        });
    }

    @Override
    public void onBackPressed() {

        back.onBackPressed();
    }

}
