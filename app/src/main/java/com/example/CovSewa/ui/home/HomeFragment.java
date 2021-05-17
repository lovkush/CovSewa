package com.example.CovSewa.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.CovSewa.Activities.Donor;
import com.example.CovSewa.Activities.ProductList;
import com.example.CovSewa.Activities.RequestList;
import com.example.CovSewa.MainActivity;
import com.example.CovSewa.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class HomeFragment extends Fragment  {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        CardView donorCard, doneeCard, requestCard;

        donorCard = root.findViewById(R.id.cardDonor);
        doneeCard = root.findViewById(R.id.cardDonee);
        requestCard = root.findViewById(R.id.cardRequest);

        doneeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductList.class);
                startActivity(intent);
            }
        });
        donorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Donor.class);
                startActivity(intent);
            }
        });
        requestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestList.class);
                startActivity(intent);
            }
        });

        return root;
    }
/*
    @Override
    public void onClick(View v) {
        Class<?> s;
        switch (v.getId()){
            case R.id.cardDonate:
                s=Donor.class;
                break;
            case R.id.cardDonee:
                s= ProductList.class;
                break;
            case R.id.cardRequest:
                s= RequestList.class;
                break;
            default:
                s= MainActivity.class;
        }
        Intent intent = new Intent(getActivity(), s);
        startActivity(intent);
    }*/
}