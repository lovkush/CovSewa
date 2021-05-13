package com.example.CovSewa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editEmail);
        editTextPassword = findViewById(R.id.editPassword);
        progressBar = findViewById(R.id.progressbar);

        //Register Listener
        findViewById(R.id.textRegister).setOnClickListener(this);

        //Login Listener
        findViewById(R.id.btnSignIn).setOnClickListener(this);

        findViewById(R.id.textForgotPassword).setOnClickListener(this);

        findViewById(R.id.textPrivacyPolicy).setOnClickListener(this);

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                finish();
                Intent intent = new Intent(SignInActivity.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textRegister:
                SpannableString Register = new SpannableString(getString(R.string.Register));
                Register.setSpan(new UnderlineSpan(), 0, Register.length(), 0);
                TextView signupText = findViewById(R.id.textRegister);
                signupText.setText(Register);
                signupText.setTextColor(Color.BLUE);
                finish();
                startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
                break;

            case R.id.btnSignIn:
                userLogin();
                break;
            case R.id.textForgotPassword:
                SpannableString resetPassword = new SpannableString(getString(R.string.forgot_you_password));
                resetPassword.setSpan(new UnderlineSpan(), 0, resetPassword.length(), 0);
                TextView resetPasswordText = findViewById(R.id.textForgotPassword);
                resetPasswordText.setText(resetPassword);
                resetPasswordText.setTextColor(Color.BLUE);
                finish();
                startActivity(new Intent(SignInActivity.this,ResetPassword.class));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

}
