package com.vector.CovSewa.ui.myDonation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vector.CovSewa.Activities.MyProduct;
import com.vector.CovSewa.Activities.ProductDetails;
import com.vector.CovSewa.Activities.ui.main.productRecycler;
import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MyDonationFragment extends Fragment implements productRecycler.OnCardClickListener{

    private MyDonationViewModel myDonationViewModel;
    ArrayList<ProductData> productDataList;
    productRecycler mAdapter ;
    RecyclerView recyclerView;
    TextView textView;
    String UID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_my_product, container, false);
        UID = FirebaseAuth.getInstance().getUid();

        productDataList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.productRecycler);
        textView= root.findViewById(R.id.textProductList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter=new productRecycler(productDataList, this);
        getData("");
        return root;
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
        Intent intent = new Intent(getContext(), ProductDetails.class);
        intent.putExtra("ProductId",productDataList.get(position).getProductId());
        startActivity(intent);
    }
}