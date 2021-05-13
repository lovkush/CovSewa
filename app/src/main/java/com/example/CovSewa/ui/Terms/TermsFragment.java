package com.example.CovSewa.ui.Terms;

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
import com.example.CovSewa.ui.SupportUs.SupportUsViewModel;

public class TermsFragment extends Fragment {
    private TermsViewModel termsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        termsViewModel =
                new ViewModelProvider(this).get(TermsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_terms, container, false);
        termsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
}
