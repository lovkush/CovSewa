package com.vector.CovSewa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.vector.CovSewa.R;

public class Review extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Review");
        findViewById(R.id.AppReview).setOnClickListener(v->{
            Intent intent = new Intent(Review.this,AppReview.class);
            startActivity(intent);
        });
        findViewById(R.id.DonationReview).setOnClickListener(v->{
            Intent intent = new Intent(Review.this,DonationReview.class);
            startActivity(intent);
        });
    }
}