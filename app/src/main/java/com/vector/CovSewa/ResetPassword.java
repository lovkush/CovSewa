package com.vector.CovSewa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPassword extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private TextView email, textLogin;
    private Button btnPasswordReset;
    private String emailText;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        email = findViewById(R.id.editPassword);
        btnPasswordReset = findViewById(R.id.btnResetPassword);
        progressBar=findViewById(R.id.progressbarResetPassword);
        progressBar.setVisibility(View.GONE);
        textLogin = findViewById(R.id.textLogin);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableString login = new SpannableString("Login");
                login.setSpan(new UnderlineSpan(), 0, login.length(), 0);
                textLogin.setText(login);
                textLogin.setTextColor(Color.BLUE);
                finish();
                startActivity(new Intent(ResetPassword.this,SignInActivity.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        btnPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(ResetPassword.this);
                progressBar.setVisibility(View.VISIBLE);
                emailText = email.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    email.setError("Please enter a valid email");
                    email.requestFocus();
                    progressBar.setVisibility(View.GONE);
                }else{
                    firebaseAuth.sendPasswordResetEmail(emailText).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ResetPassword.this,"Password Reset Link Sent Successfully",Toast.LENGTH_LONG).show();
                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ResetPassword.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
