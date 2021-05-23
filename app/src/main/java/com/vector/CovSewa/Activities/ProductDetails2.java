package com.vector.CovSewa.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vector.CovSewa.Activities.ui.main.ImageSliderAdapter;
import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;
import com.vector.CovSewa.RequestData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vector.CovSewa.ReviewData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ProductDetails2 extends AppCompatActivity {

    private ImageView call;
    private TextView ProductTitle,ProductDescription,Area, reviewTitle, reviewContact, reviewDescription,time;
    private ProductData productData= new ProductData();
    private Button requestButton, reviewPopupButton;
    String productId,UID,textRequestTitle,textRequestContact,textRequestDescription;
    private Dialog fbDialogue;
    private int count =0;

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    ImageSliderAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Title");

        UID = FirebaseAuth.getInstance().getUid();
        ProductTitle = findViewById(R.id.productName);
        ProductDescription = findViewById(R.id.productDescription);
        Area = findViewById(R.id.productArea);
        call= findViewById(R.id.call);
        requestButton = findViewById(R.id.requestProduct);
        time = findViewById(R.id.time);

        Intent intent = getIntent();
        productId = intent.getStringExtra("ProductId");
        Log.d(TAG, "onCreate: "+ getIntent().getStringExtra("ProductId"));
        getData();


        fbDialogue = new Dialog(ProductDetails2.this, android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(fbDialogue.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.popup);
        fbDialogue.setCancelable(true);

        requestButton.setOnClickListener(v->{
            if(!validateUser()){
                Toast.makeText(ProductDetails2.this,"Request Made.",Toast.LENGTH_LONG).show();
            }else {
                fbDialogue.show();
            }
        });

        reviewTitle = fbDialogue.findViewById(R.id.requestTopic);
        reviewContact = fbDialogue.findViewById(R.id.requestContact);
        reviewContact.setVisibility(View.GONE);
        reviewDescription = fbDialogue.findViewById(R.id.textDescription);
        reviewPopupButton =  fbDialogue.findViewById(R.id.btnSubmitRequestDetail);
        reviewPopupButton.setText("Submit Review");
        reviewPopupButton.setOnClickListener(v->{
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
        count = productData.getImageCount();
        ProductDescription.setText(productData.getDescription());
        DateFormat formatter = new SimpleDateFormat("hh:mm dd MMM");
        Date date = new Date(productData.getTimestamp());
        time.setText(formatter.format(date));
        init();
        addBottomDots(0);
        if(productData.getContact()!=null){
            call.setVisibility(View.VISIBLE);
            call.setOnClickListener(v->{
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(productData.getContact()));
                if (ActivityCompat.checkSelfPermission(ProductDetails2.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            });
        }
    }

    private void generateRequest(){

        if(validate()){
            ReviewData reviewData = new ReviewData();
            reviewData.setReviewId(System.currentTimeMillis()+UID);
            reviewData.setDescription(reviewDescription.getText().toString());
            reviewData.setDoneeId(UID);
            reviewData.setTopic(reviewTitle.getText().toString());
            reviewData.setTimestamp(System.currentTimeMillis());
            reviewData.setProductId(productId);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Review");
            reference.child(reviewData.getReviewId()).setValue(reviewData, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error==null){
                        Toast.makeText(ProductDetails2.this,"Review Sent",Toast.LENGTH_LONG).show();
                        fbDialogue.dismiss();
                    }
                    else {
                        Log.d("Database Error",  error.toString());
                        Toast.makeText(ProductDetails2.this,"Sorry,Unable to Send Review. Try Again.",Toast.LENGTH_LONG).show();

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
        if(reviewDescription.getText()==null|| reviewDescription.getText().toString().length()<30){
            reviewDescription.setError("Enter Valid Description (Min 30)");
            reviewDescription.requestFocus();
            return false;
        }
        if(reviewTitle.getText()==null|| reviewTitle.getText().toString().length()<10){
            reviewTitle.setError("Enter Valid Title (Min 10)");
            reviewTitle.requestFocus();
            return false;
        }
        return true;
    }



    private void init() {

        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        slider_image_list = new ArrayList<>();
        bitmaps.clear();
        while (count>0){
            count--;
            setImage(productData.getTimestamp()+UID+count);
        }

        sliderPagerAdapter = new ImageSliderAdapter(ProductDetails2.this, bitmaps,ProductDetails2.this);
        vp_slider.setAdapter(sliderPagerAdapter);
        sliderPagerAdapter.notifyDataSetChanged();

        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                page_position = position;
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }

    private void setImage(String userPhoto) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if(userPhoto==null)
            return;
        StorageReference photoReference= storageReference.child("Product").child(userPhoto);
        Log.d(TAG, "setImage: " + photoReference);
        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmaps.add(bmp);
                sliderPagerAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ProductDetails2.this, "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

}