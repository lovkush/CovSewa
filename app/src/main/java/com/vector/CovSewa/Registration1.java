package com.vector.CovSewa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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

import static android.app.Activity.RESULT_CANCELED;

public class Registration1 extends Fragment {

    public Registration1() {
    }

    UserData userData = new UserData();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.get_user_details, container, false);

        return v;
    }
    private TextView textSignIn, textUserName, textZipcode, textAdd1;
    AutoCompleteTextView textAdd2 ;
    private  String signIn, userName, zipcode, add1, add2;
    private RequestQueue mRequestQueue;
    private FloatingActionButton editImage;
    private ImageView imageView;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference reference;
    UserDataViewModel viewModel;
    FirebaseAuth mAuth;
    private ArrayList<String> area = new ArrayList<>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(getContext());

        textSignIn = view.findViewById(R.id.textSignIn);
        textUserName = view.findViewById(R.id.textUserName);
        textAdd1 = view.findViewById(R.id.textAddLine1);
        textAdd2 = view.findViewById(R.id.textAddLine2);
        textZipcode = view.findViewById(R.id.textZipcode);
        editImage = view.findViewById(R.id.editProfilImage);
        imageView = view.findViewById(R.id.profileimage);

       textZipcode.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length()==6){
                        //zipcode = textZipcode.getText().toString();
                       /* if (zipcode.trim().length() < 6) {
                            textZipcode.setError("Enter a Valid zipcode");
                            textZipcode.requestFocus();
                        } else {*/
                            getDataFromPinCode(zipcode);
                        //}
                    }

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
        viewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        userData.setContact(mAuth.getCurrentUser().getPhoneNumber());


        textAdd1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                zipcode = textZipcode.getText().toString();
                if (zipcode.trim().length() < 6) {
                    textZipcode.setError("Enter a Valid zipcode");
                    textZipcode.requestFocus();
                } else {
                    getDataFromPinCode(zipcode);
                }
                return false;
            }
        });

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),SignInActivity.class);
                getActivity().finish();
                    startActivity(intent);
            }
        });



        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        reference = FirebaseDatabase.getInstance().getReference();
        initUI(view);


        editImage.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), UploadPhoto.class);
            startActivityForResult(intent,2);
        });

    }

    boolean verifyData(){
        boolean result = false;
        if(userName==null)
        {
            textUserName.setError("UserName is required");
            textUserName.requestFocus();
            return result;
        }
        if(!userName.matches("^[A-Za-z ]+$")||userName.length()<3){
            textUserName.setError("Enter a valid Name");
            textUserName.requestFocus();
            return result;
        }
        if(add1.length()<6){
            textAdd1.setError("Enter a valid address");
            textAdd1.requestFocus();
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());

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
                Toast.makeText(getContext(), "Pin code is not valid.", Toast.LENGTH_SHORT).show();
                textZipcode.setError("Pin code is not valid");
                Log.d("TAG", "onErrorResponse: "+error);
            }
        });
        // below line is use for adding object
        // request to our request queue.
        queue.add(objectRequest);
    }



    private void initUI(View v)
    {
        final AutoCompleteTextView category = v.findViewById(R.id.selectCategory);
        ArrayList<String> customerList = getCustomerList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, customerList);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), R.layout.spinner_item, area);
        category.setAdapter(adapter);

        textAdd2.setAdapter(adapter1);
        v.findViewById(R.id.btnSubmitUserDetails).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userName = textUserName.getText().toString();
                zipcode = textZipcode.getText().toString();
                add1 = textAdd1.getText().toString();
                add2 = textAdd2.getText().toString();

                if(verifyData()){
                    userData.setName(userName);
                    userData.setZipcode(zipcode);
                    userData.setAddLine1(add1);
                    userData.setAddLine2(add2);
                    userData.setCategory(category.getText().toString());
                    Log.d("phone", "onClick: "+ userData.getContact());
                    viewModel.selectItem(userData);
                }
            }
        });
    }

    private ArrayList<String> getCustomerList()
    {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("Individual");
        customers.add("NGO/Trust/Organisation");
        customers.add("Supplier");
        customers.add("Other");
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
                        imageView.setImageBitmap(bitmap);
                        uploadImage(Uri.fromFile(f));
                        Log.d("TAG", "onActivityResult: imageuri" + path);

                    } else {
                        Uri imageUri = intent.getParcelableExtra("URI");
                        if (imageUri != null) {
                            imageView.setImageURI(imageUri);
                            uploadImage(imageUri);
                            Log.d("TAG", "onActivityResult: imageuri" + imageUri);
                        } else {
                            Toast.makeText(getContext(), "No image is set to show", Toast.LENGTH_LONG).show();
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
                    = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference storageReference= FirebaseStorage.getInstance().getReference();
            StorageReference ref
                    = storageReference
                    .child(
                            "ProfileImage/"
                                    + userData.getContact());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(

                            taskSnapshot -> {

                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                Toast
                                        .makeText(getContext(),
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();

                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getContext(),
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


}
