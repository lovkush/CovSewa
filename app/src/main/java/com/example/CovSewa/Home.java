package com.example.CovSewa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        TextView textView = findViewById(R.id.textView2);
        textView.setText(mAuth.getCurrentUser().getPhoneNumber());
    }
}