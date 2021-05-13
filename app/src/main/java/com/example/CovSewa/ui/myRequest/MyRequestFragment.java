package com.example.CovSewa.ui.myRequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.CovSewa.R;
import com.example.CovSewa.ui.AboutUs.MyRequestViewModel;

public class MyRequestFragment extends Fragment {
    private MyRequestViewModel myRequestViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myRequestViewModel =
                new ViewModelProvider(this).get(MyRequestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_request, container, false);
        myRequestViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
}
