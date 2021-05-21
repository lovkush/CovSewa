package com.vector.CovSewa.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;
import com.vector.CovSewa.UploadPhoto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Donor extends AppCompatActivity {
    private TextView textTitle, textDescription, textZipcode, textAdd1;
    AutoCompleteTextView textAdd2 ;
    private RequestQueue mRequestQueue;
    private AutoCompleteTextView customerAutoTV;
    ImageView[] imageView = new ImageView[4];
    int imageCount=0;
    private String category,UID,description,zipcode,add1,add2,productId,title;
    FirebaseAuth mAuth;
    ProductData productData = new ProductData();
    long timestamp;

    private ArrayList<String> area = new ArrayList<>();
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference reference;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Enter Details");

        UID = FirebaseAuth.getInstance().getUid();

        mRequestQueue = Volley.newRequestQueue(Donor.this);

        textDescription = findViewById(R.id.textDescription);
        textAdd1 = findViewById(R.id.textAddLine1);
        textAdd2 = findViewById(R.id.textAddLine2);
        textZipcode = findViewById(R.id.textZipcode);
        textTitle = findViewById(R.id.productTitle);
        imageView[0] = findViewById(R.id.donorImg1);
        imageView[1] = findViewById(R.id.donorImg2);
        imageView[2] = findViewById(R.id.donorImg3);
        imageView[3] = findViewById(R.id.donorImg4);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, area);
        textAdd2.setAdapter(adapter);
        timestamp = System.currentTimeMillis();
        productId = timestamp + UID;

        View v = findViewById(R.id.imageViewProduct);
        v.setOnClickListener(v1->{
            Intent intent = new Intent(Donor.this, UploadPhoto.class);
            startActivityForResult(intent,2);
        });

        mAuth = FirebaseAuth.getInstance();

        textAdd1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                zipcode= textZipcode.getText().toString();
                if(zipcode.trim().length()<6){
                    textZipcode.setError("Enter a Valid zipcode");
                    textZipcode.requestFocus();
                }else{
                    getDataFromPinCode(zipcode);
                }
                return false;
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        reference = FirebaseDatabase.getInstance().getReference();
        initUI();



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    boolean verifyData(){
        boolean result = false;
        if( description.length()<20)
        {
            textDescription.setError("Description is required");
            textDescription.requestFocus();
            return result;
        }
        if(category==null){
            customerAutoTV.setError("Select a category");
            customerAutoTV.requestFocus();
            return result;
        }
        if(zipcode==null){
            textZipcode.setError("ZipCode is required");
            textZipcode.requestFocus();
            return result;
        }
        if(add1.length()<6){
            textAdd1.setError("Enter a valid address");
            textAdd1.requestFocus();
            return result;
        }
        if(imageCount<1){
            Toast.makeText(Donor.this,"Upload atleast 1 image",Toast.LENGTH_LONG).show();
            return result;
        }

        if(title.length()<10){
            textTitle.setError("Enter a valid Title");
            textTitle.requestFocus();
            return result;
        }
        return true;
    }

    private void getDataFromPinCode(String pinCode) {

        // clearing our cache of request queue.
        mRequestQueue.getCache().clear();
        // below is the url from where we will be getting
        // our response in the json format.
        String url = "http://www.postalpincode.in/api/pincode/" + pinCode;

        // below line is use to initialize our request queue.
        RequestQueue queue = Volley.newRequestQueue(Donor.this);

        // in below line we are creating a
        // object request using volley.
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // inside this method we will get two methods
                // such as on response method
                // inside on response method we are extracting
                // data from the json format.
                try {
                    // we are getting data of post office
                    // in the form of JSON file.
                    JSONArray postOfficeArray = response.getJSONArray("PostOffice");
                    Log.d("TAG", "onResponse: "+response);
                    if (response.getString("Status").equals("Error")) {
                        // validating if the response status is success or failure.
                        // in this method the response status is having error and
                        // we are setting text to TextView as invalid pincode.
                        textZipcode.setText("Pin code is not valid.1");
                    } else {
                        // if the status is success we are calling this method
                        // in which we are getting data from post office object
                        // here we are calling first object of our json array.
                        area.clear();
                        for(int i=0;i<postOfficeArray.length();i++){
                            JSONObject obj = postOfficeArray.getJSONObject(i);

                            // inside our json array we are getting district name,
                            // state and country from our data.
                            String district = obj.getString("District");
                            String state = obj.getString("State");
                            String country = obj.getString("Country");
                            String name = obj.getString("Name");
                            // after getting all data we are setting this data in
                            // our text view on below line.
                            Log.d("post office", "onResponse: " + name);
                            area.add(name+", "+district+", "+state);
                        }

                    }
                } catch (JSONException e) {
                    // if we gets any error then it
                    // will be printed in log cat.
                    e.printStackTrace();
                    textZipcode.setError("Pin code is not valid");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // below method is called if we get
                // any error while fetching data from API.
                // below line is use to display an error message.
                Toast.makeText(Donor.this, "Pin code is not valid.", Toast.LENGTH_SHORT).show();
                textZipcode.setError("Pin code is not valid");
                Log.d("TAG", "onErrorResponse: "+error);
            }
        });
        // below line is use for adding object
        // request to our request queue.
        queue.add(objectRequest);
    }


    private void initUI()
    {
        //UI reference of textView
        customerAutoTV = findViewById(R.id.selectProductCategory);

        // create list of customer
        ArrayList<String> customerList = getCustomerList();

        //Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Donor.this, R.layout.spinner_item, customerList);

        //Set adapter
        customerAutoTV.setAdapter(adapter);

        //submit button click event registration
        findViewById(R.id.btnSubmitProductDetail).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                description = textDescription.getText().toString();
                zipcode = textZipcode.getText().toString();
                add1 = textAdd1.getText().toString();
                add2 = textAdd2.getText().toString();
                category = customerAutoTV.getText().toString();
                title = textTitle.getText().toString();
                if(verifyData()){
                    productData.setDescription(description);
                    productData.setZipCode(zipcode);
                    productData.setAddLine1(add1);
                    productData.setAddLine2(add2);
                    productData.setCategory(category);
                    productData.setProductId(productId);
                    productData.setTimestamp(timestamp);
                    productData.setCategory(category);
                    productData.setImageCount(imageCount);
                    productData.setProductTitle(title);
                    //initUI();
                    update();
                }
            }
        });
    }

    private ArrayList<String> getCustomerList()
    {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("Non Prescription Medicines");
        customers.add("Oxygen Concentrators");
        customers.add("Oxygen Cylinders");
        customers.add("Masks and Sanitizers");
        customers.add("Food");
        customers.add("Medical Equipments");
        customers.add("Ambulance");
        return customers;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode==2)
        {
            if(resultCode != RESULT_CANCELED){
                assert intent != null;
                String path = intent.getStringExtra("path");
                assert path != null;
                if(!path.equals("null")){
                    File f= new File(path);
                    if (f.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        imageView[imageCount].setImageBitmap(bitmap);
                        uploadImage(Uri.fromFile(f));
                        Log.d("TAG", "onActivityResult: imageuri" + path);

                    } else {
                        Uri imageUri = intent.getParcelableExtra("URI");
                        if (imageUri != null) {
                            imageView[imageCount].setImageURI(imageUri);
                            uploadImage(imageUri);
                            Log.d("TAG", "onActivityResult: imageuri" + imageUri);
                        } else {
                            Toast.makeText(Donor.this, "No image is set to show", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

        }

    }

    private void uploadImage(Uri filePath)
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(Donor.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference storageReference= FirebaseStorage.getInstance().getReference();
            StorageReference ref
                    = storageReference
                    .child(
                            "Product/"
                                    + productId+imageCount);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(

                            taskSnapshot -> {

                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                Toast
                                        .makeText(Donor.this,
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                                imageCount++;

                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(Donor.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                }
            });
        }
    }


    private void update(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Product");
        reference.child(productId).setValue(productData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error==null){
                        Toast.makeText(Donor.this,"Product Uploaded",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Donor.this,DashBoard.class);
                        finish();
                        startActivity(intent);
                    }
                    else {
                        Log.d("Database Error",  error.toString());
                        Toast.makeText(Donor.this,"Sorry,Unable to Upload. Try Again.",Toast.LENGTH_LONG).show();

                    }
            }
        });
    }

}