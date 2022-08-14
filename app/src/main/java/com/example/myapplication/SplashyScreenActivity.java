package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashyScreenActivity extends AppCompatActivity {
    private ImageView ivLogo;
    private TextView tvLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashy_screen);

        ivLogo=findViewById(R.id.ivLogo);
        tvLogo=findViewById(R.id.tvLogo);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent i=new Intent(SplashyScreenActivity.this, com.example.myapplication.ExtraActivity.class);
               startActivity(i);
               finish();
           }
       },3000);

    }
}