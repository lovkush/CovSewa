package com.vector.CovSewa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vector.CovSewa.Activities.DashBoard;

import static com.google.firebase.database.FirebaseDatabase.*;

public class RegisterActivity extends AppCompatActivity {

    private UserDataViewModel viewModel;
    UserData userData;
    private String email,pass;
    String UID;

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
            Log.d("TAG", "onCreate: user data" + userData.getName()+userData.getAddLine1());
            userData.setEmail(email);
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
                            UID = mAuth.getUid();
                            uploadData();
                            Toast.makeText(RegisterActivity.this, "Authentication Done.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void uploadData(){
        FirebaseDatabase firebaseDatabase = getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Log.d("TAG", "uploadData: " + reference);
            reference.child("User").child(UID).setValue(userData, (databaseError, databaseReference) -> {
                if(databaseError!=null){
                    System.out.println("Error : " + databaseError);
                    Log.d("error", "onComplete: "+ databaseError);
                }else{

                    Intent intent = new Intent(RegisterActivity.this, DashBoard.class);
                    finish();
                    startActivity(intent);
                    Log.d("TAG", "onComplete: data uploaded");
                }
            });
    }



}