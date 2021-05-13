package com.example.CovSewa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private TextView textSignIn, textUserName, textZipcode, textAdd1, textAdd2 ;
    private  String signIn, userName, zipcode, add1, add2;
    private RequestQueue mRequestQueue;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDataViewModel viewModel;

        mRequestQueue = Volley.newRequestQueue(getContext());

        textSignIn = view.findViewById(R.id.textSignIn);
        textUserName = view.findViewById(R.id.textUserName);
        textAdd1 = view.findViewById(R.id.textAddLine1);
        textAdd2 = view.findViewById(R.id.textAddLine2);
        textZipcode = view.findViewById(R.id.textZipcode);

        Button btnContinue = view.findViewById(R.id.btnSubmitUserDetails);
        viewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);


        textAdd1.setOnClickListener(v -> {
                zipcode= textZipcode.getText().toString();
                if(zipcode==null||zipcode.trim().length()<6){
                    textZipcode.setError("Enter a Valid zipcode");
                    textZipcode.requestFocus();
                }else{
                    getDataFromPinCode(zipcode);
                }
        });
        btnContinue.setOnClickListener(item -> {
            userName = textUserName.getText().toString();
            zipcode = textZipcode.getText().toString();
            add1 = textAdd1.getText().toString();
            add2 = textAdd2.getText().toString();
            if(verifyData()){
                userData.setName(userName);
                userData.setZipcode(zipcode);
                userData.setAddLine1(add1);
                userData.setAddLine2(add2);
                viewModel.selectItem(userData);
            }
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
        if(!userName.matches("^[A-Za-z]+$")||userName.length()<4){
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
                            textAdd2.setText("Add " + name + ", " + district + ", " + state + ", " + country);
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
}
