package com.example.CovSewa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private UserDataViewModel viewModel;
    UserData userData;
    private String email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment, new Registration1(), "SOMETAG").
                commit();

        viewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        viewModel.getSelectedItem().observe(this, item -> {
            userData = item;
            Log.d("userdata", "onCreate: " + userData.getName());
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment, new Registration2(), "SOMETAG").
                    commit();
        });


        viewModel.getEmailPass().observe(this, item -> {
            email = item[0];
            pass = item[1];
            register();
        });

    }


    private void register (){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(RegisterActivity.this, "Authentication Done.",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this,DashBoard.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}