package com.vector.CovSewa.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vector.CovSewa.R;

public class RequestList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_request);
        View v1,v2;
        v1=findViewById(R.id.requestRecieved);
        v2=findViewById(R.id.requestSent);

        v1.setOnClickListener(v->{
            Intent intent = new Intent(RequestList.this,RequestReceived.class);
            startActivity(intent);
        });


        v2.setOnClickListener(v->{
            Intent intent = new Intent(RequestList.this,RequestSent.class);
            startActivity(intent);
        });
    }
}