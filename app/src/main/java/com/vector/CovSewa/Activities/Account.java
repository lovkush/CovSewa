package com.vector.CovSewa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vector.CovSewa.R;

import static android.content.ContentValues.TAG;

public class Account extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        findViewById(R.id.RequestSent).setOnClickListener(v->{
            Intent intent;
            Log.d(TAG, "onCreate: req");

            intent = new Intent(Account.this,RequestSent.class);
            startActivity(intent);
        });

        findViewById(R.id.DonatedItems).setOnClickListener(v->{
            Intent intent;
            intent = new Intent(Account.this,MyProduct.class);
            Log.d(TAG, "onCreate: req");

            startActivity(intent);
        });

        findViewById(R.id.Profile).setOnClickListener(v->{
            Intent intent;
            intent = new Intent(Account.this,ProfileActivity.class);
            Log.d(TAG, "onCreate: req");
            startActivity(intent);
        });

        findViewById(R.id.Reviews).setOnClickListener(v->{
            Intent intent;
            intent = new Intent(Account.this,DonationReview.class);
            Log.d(TAG, "onCreate: req");
            startActivity(intent);
        });
    }

}