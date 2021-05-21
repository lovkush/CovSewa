package com.vector.CovSewa.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;
import com.vector.CovSewa.RequestData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ProductDetails extends AppCompatActivity {

    private ImageView profileImage,call;
    private TextView ProductTitle,ProductDescription,Area,requestTitle,requestContact,requestDescription;
    private ProductData productData= new ProductData();
    private Button requestButton,requestPopupButton;
    String productId,UID,textRequestTitle,textRequestContact,textRequestDescription;
    private Dialog fbDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Title");

        UID = FirebaseAuth.getInstance().getUid();
        ProductTitle = findViewById(R.id.productName);
        ProductDescription = findViewById(R.id.productDescription);
        Area = findViewById(R.id.productArea);
        call= findViewById(R.id.call);
        requestButton = findViewById(R.id.requestProduct);


        Intent intent = getIntent();
        productId = intent.getStringExtra("ProductId");
        Log.d(TAG, "onCreate: "+ getIntent().getStringExtra("ProductId"));
        getData();


        fbDialogue = new Dialog(ProductDetails.this, android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(fbDialogue.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.popup);
        fbDialogue.setCancelable(true);

        requestButton.setOnClickListener(v->{
            if(!validateUser()){
                Toast.makeText(ProductDetails.this,"Request Made.",Toast.LENGTH_LONG).show();
            }else {
                    fbDialogue.show();
            }
        });

        requestTitle = fbDialogue.findViewById(R.id.requestTopic);
        requestContact = fbDialogue.findViewById(R.id.requestContact);
        requestDescription = fbDialogue.findViewById(R.id.textDescription);
        requestPopupButton =  fbDialogue.findViewById(R.id.btnSubmitRequestDetail);

        requestPopupButton.setOnClickListener(v->{
            generateRequest();
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private  void  getData(){
        FirebaseDatabase.getInstance().getReference().child("Product").child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productData = snapshot.getValue(ProductData.class);
                setView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " +error);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setView(){
        ProductTitle.setText(productData.getProductTitle());
        Area.setText(productData.getAddLine2().split(",")[0]);
        ProductDescription.setText(productData.getDescription());
        if(productData.getContact()!=null){
            call.setVisibility(View.VISIBLE);
            call.setOnClickListener(v->{
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(productData.getContact()));
                if (ActivityCompat.checkSelfPermission(ProductDetails.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            });
        }
    }

    private void generateRequest(){

        if(validate()){
            RequestData requestData = new RequestData();
            requestData.setRequestId(UID+System.currentTimeMillis());
            requestData.setDonorId(productData.getDonorId());
            requestData.setDescription(requestDescription.getText().toString());
            requestData.setDoneeId(UID);
            requestData.setContact(requestContact.getText().toString());
            requestData.setTopic(requestTitle.getText().toString());
            requestData.setTime(System.currentTimeMillis());
            requestData.setStatus(false);
            requestData.setSaveStatus(false);
            requestData.setProductId(productId);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Request");
            reference.child(requestData.getRequestId()).setValue(requestData, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error==null){
                        Toast.makeText(ProductDetails.this,"Request Sent",Toast.LENGTH_LONG).show();
                        fbDialogue.dismiss();
                    }
                    else {
                        Log.d("Database Error",  error.toString());
                        Toast.makeText(ProductDetails.this,"Sorry,Unable to Send Request. Try Again.",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }

    boolean status;
    private boolean validateUser(){
        status = true;
            FirebaseDatabase.getInstance().getReference("Request/").orderByChild("doneeId").equalTo(UID).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    status=  false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return status;
    }

    private boolean validate(){
        if(requestDescription.getText()==null||requestDescription.getText().toString().length()<30){
            requestDescription.setError("Enter Valid Description");
            requestDescription.requestFocus();
            return false;
        }
        if(requestTitle.getText()==null||requestTitle.getText().toString().length()<10){
            requestTitle.setError("Enter Valid Title");
            requestTitle.requestFocus();
            return false;
        }
        if(requestContact.getText()==null||requestContact.getText().toString().length()!=10){
            requestContact.setError("Enter Valid Contact. (Avoid Country Code)");
            requestContact.requestFocus();
            return false;
        }
        return true;
    }

}