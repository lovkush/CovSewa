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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vector.CovSewa.Activities.ui.main.productRecycler;
import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyProduct extends AppCompatActivity implements productRecycler.OnCardClickListener {

    ArrayList<ProductData> productDataList;
    productRecycler mAdapter ;
    RecyclerView recyclerView;
    TextView textView;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Donations");

        UID = FirebaseAuth.getInstance().getUid();

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


    private void getData(String category) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Product").orderByChild("donorId").equalTo(UID);

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

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(MyProduct.this,ProductDetails.class);
        intent.putExtra("ProductId",productDataList.get(position).getProductId());
        startActivity(intent);
    }
}