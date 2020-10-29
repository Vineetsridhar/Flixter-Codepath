package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitle;
    RatingBar ratingBar;
    TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        ratingBar = findViewById(R.id.ratingsBar);
        tvOverview = findViewById(R.id.tvOverview);
        Intent i = getIntent();
        tvTitle.setText(i.getStringExtra("title"));
    }
}