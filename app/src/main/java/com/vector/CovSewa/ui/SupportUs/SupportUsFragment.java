package com.vector.CovSewa.ui.SupportUs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.vector.CovSewa.R;

public class SupportUsFragment extends Fragment {
    private SupportUsViewModel supportUsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        supportUsViewModel =
                new ViewModelProvider(this).get(SupportUsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_support, container, false);
        supportUsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
}
