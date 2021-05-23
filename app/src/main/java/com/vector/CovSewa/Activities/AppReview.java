package com.vector.CovSewa.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vector.CovSewa.Activities.ui.main.AppReviewAdapter;
import com.vector.CovSewa.Activities.ui.main.RequestListAdapter;
import com.vector.CovSewa.Activities.ui.main.productRecycler;
import com.vector.CovSewa.AppReviewData;
import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AppReview extends AppCompatActivity  {

    ArrayList<AppReviewData> appReviewData;
    AppReviewAdapter mAdapter ;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_review);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Help Available");
        getData();



        appReviewData = new ArrayList<>();
        recyclerView = findViewById(R.id.AppReviewRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new AppReviewAdapter(appReviewData);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("AppReview");
        TextView textView = findViewById(R.id.textView3);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appReviewData.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    appReviewData.add(dataSnapshot1.getValue(AppReviewData.class));
                    Log.d(TAG, "onDataChange: " + dataSnapshot1.getValue(ProductData.class).getCategory());
                }

                if(appReviewData.size()!=0){
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
}