package com.example.CovSewa.ui.myDonation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.CovSewa.R;

public class MyDonationFragment extends Fragment {

    private MyDonationViewModel myDonationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myDonationViewModel =
                new ViewModelProvider(this).get(MyDonationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_donation, container, false);
        final TextView textView = root.findViewById(R.id.textTotalDonation);
        myDonationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
}