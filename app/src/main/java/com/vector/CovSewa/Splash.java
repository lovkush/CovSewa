package com.vector.CovSewa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Splash extends AppCompatActivity {

    private Button btnSignIn, btnRegister;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btnRegister = findViewById(R.id.register);
        btnSignIn = findViewById(R.id.sign_in);

        btnRegister.setOnClickListener(v -> {
            intent = new Intent(Splash.this,PhoneVerification.class);
            finish();
            startActivity(intent);
        });

        btnSignIn.setOnClickListener(v -> {
            intent = new Intent(Splash.this,SignInActivity.class);
            finish();
            startActivity(intent);
        });
    }
}