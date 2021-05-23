package com.vector.CovSewa.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.vector.CovSewa.Activities.ui.main.productRecycler;
import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ProductList extends AppCompatActivity implements productRecycler.OnCardClickListener {
    ArrayList<ProductData> productDataList;
    productRecycler mAdapter ;
    RecyclerView recyclerView;
    TextView textView;
    String category = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help Available");


        initUI();


        productDataList = new ArrayList<>();
        recyclerView = findViewById(R.id.productRecycler);
        textView= findViewById(R.id.textProductList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new productRecycler(productDataList, this);
        getData("");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCardClick(int position) {

        Intent intent = new Intent(ProductList.this,ProductDetails.class);
        intent.putExtra("ProductId",productDataList.get(position).getProductId());
        startActivity(intent);
    }

    private void getData(String category) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Product");

        if(!category.equals("")&&!
                category.equals("All")){
            query = reference.child("Product").orderByChild("category").equalTo(category);
        }


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productDataList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    productDataList.add(dataSnapshot1.getValue(ProductData.class));
                    Log.d(TAG, "onDataChange: " + dataSnapshot1.getValue(ProductData.class).getCategory());
                }

                if(productDataList.size()!=0){
                    mAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled:  " + databaseError);
            }

        });
    }

    private void initUI()
    {
        //UI reference of textView
        final AutoCompleteTextView customerAutoTV = findViewById(R.id.textArea);
        final AutoCompleteTextView customerAutoTV1 = findViewById(R.id.textCategory);

        // create list of customer
        ArrayList<String> customerList = getCategoryList();
        ArrayList<String> areaList = new ArrayList<>();
        areaList.add("Delhi");

        //Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, customerList);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item, areaList);

        //Set adapter
        customerAutoTV.setAdapter(adapter1);
        customerAutoTV1.setAdapter(adapter);

        //submit button click event registration
        //
        customerAutoTV1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = customerList.get(position);
                getData(category);
            }
        });
    }

    private ArrayList<String> getCategoryList()
    {
        ArrayList<String> category = new ArrayList<>();
        category.add("All");
        category.add("Non Prescription Medicines");
        category.add("Oxygen Concentrators");
        category.add("Oxygen Cylinders");
        category.add("Masks and Sanitizers");
        category.add("Food");
        category.add("Medical Equipments");
        category.add("Ambulance");
        return category;
    }


}