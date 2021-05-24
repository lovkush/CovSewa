package com.vector.CovSewa.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.vector.CovSewa.OnBoarding;
import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;
import com.vector.CovSewa.RequestData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vector.CovSewa.SliderPagerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ProductDetails extends AppCompatActivity {

    private ImageView profileImage,call;
    private TextView ProductTitle,ProductDescription,Area,requestTitle,requestContact,requestDescription,time;
    private ProductData productData= new ProductData();
    private Button requestButton,requestPopupButton;
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
        setContentView(R.layout.product_details);
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


        fbDialogue = new Dialog(ProductDetails.this, android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(fbDialogue.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.popup);
        fbDialogue.setCancelable(true);

        Log.d(TAG, "onCreate: donorid"+UID+productData.getDonorId());
        requestButton.setOnClickListener(v->{
            if(UID.equals(productData.getDonorId())){
                Toast.makeText(ProductDetails.this,"Cant Request Your Own Product",Toast.LENGTH_LONG).show();
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
                callIntent.setData(Uri.parse("tel:" + productData.getContact()));
                /*if (ActivityCompat.checkSelfPermission(ProductDetails.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }*/
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



    private void init() {

        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        slider_image_list = new ArrayList<>();
        bitmaps.clear();
        while (count>0){
            count--;
            setImage(productData.getTimestamp()+productData.getDonorId()+count);
        }

        sliderPagerAdapter = new ImageSliderAdapter(ProductDetails.this, bitmaps,ProductDetails.this);
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
                Toast.makeText(ProductDetails.this, "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

}