package com.vector.CovSewa.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.vector.CovSewa.R;
import com.vector.CovSewa.RequestData;

import static android.content.ContentValues.TAG;

public class RequestDetails extends AppCompatActivity {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1001;

    RequestData requestDetails;
    TextView nameProduct, contact, location, description;
    ImageView call, save;
    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_details);
        requestDetails = (RequestData) getIntent().getSerializableExtra("id");

        nameProduct = findViewById(R.id.requestDetailName);
        contact = findViewById(R.id.requestDetailContact);
        location = findViewById(R.id.requestDetailLocation);
        description = findViewById(R.id.requestDetailDescription);
        call = findViewById(R.id.requestDetailCall);
        save = findViewById(R.id.requestDetailSave);

        nameProduct.setText(requestDetails.getCategory());
        contact.setText(requestDetails.getContact());
        location.setText(requestDetails.getAdd());
        description.setText(requestDetails.getDescription());

        if(requestDetails.isSaveStatus()){
            save.setImageDrawable(getDrawable(R.drawable.ic_baseline_bookmark_24));
        }else save.setImageDrawable(getDrawable(R.drawable.ic_baseline_bookmark_border_24));



        call.setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RequestDetails.this, new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_ASK_PERMISSIONS);

            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + requestDetails.getContact()));
                startActivity(intent);
            }
        });

        save.setOnClickListener(v->{
            if(requestDetails.isSaveStatus()){
                FirebaseDatabase.getInstance().getReference().child("Request").child(requestDetails.getRequestId()).child("saveStatus").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        save.setImageDrawable(getDrawable(R.drawable.ic_baseline_bookmark_border_24));
                        requestDetails.setSaveStatus(false);
                    }
                });
            }else{
                FirebaseDatabase.getInstance().getReference().child("Request").child(requestDetails.getRequestId()).child("saveStatus").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        save.setImageDrawable(getDrawable(R.drawable.ic_baseline_bookmark_24));
                        requestDetails.setSaveStatus(true);

                    }
                });
            }

        });


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Call Permission Granted..Please dial again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Call permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}