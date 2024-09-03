package com.example.resueforce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class UserActivity2 extends AppCompatActivity {

    CardView Insights;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);
        Insights = (CardView) findViewById(R.id.c1);

        Insights.setOnClickListener(view ->
        {
            Intent i = new Intent(UserActivity2.this, UserHistory2.class);
            startActivity(i);
        });
    }
}