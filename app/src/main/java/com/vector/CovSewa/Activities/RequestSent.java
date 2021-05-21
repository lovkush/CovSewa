package com.vector.CovSewa.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vector.CovSewa.Activities.ui.main.RequestListAdapter;
import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;
import com.vector.CovSewa.RequestData;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RequestSent extends AppCompatActivity implements RequestListAdapter.OnCardClickListener {
    RequestListAdapter mAdapter;
    RecyclerView recyclerView;
    TextView textView;
    ArrayList<RequestData> requestData =  new ArrayList<com.vector.CovSewa.RequestData>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_sent);
        recyclerView = findViewById(R.id.requestSentRecycler);
        textView= findViewById(R.id.textView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new RequestListAdapter(requestData, this);


        String Uid = FirebaseAuth.getInstance().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Request").orderByChild("doneeId").equalTo(Uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestData.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    requestData.add(dataSnapshot1.getValue(RequestData.class));
                    Log.d(TAG, "onDataChange: " + dataSnapshot1.getValue(RequestData.class).getCategory());
                }

                if(requestData.size()!=0){
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
        Intent intent = new Intent(RequestSent.this,ProductDetails.class);
        Log.d(TAG, "onCardClick: "
                + requestData.get(position).getProductId() + position);
        intent.putExtra("ProductId", requestData.get(position).getProductId());
        startActivity(intent);
    }
}
