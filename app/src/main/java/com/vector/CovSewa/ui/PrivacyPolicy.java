package com.vector.CovSewa.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.vector.CovSewa.R;

public class PrivacyPolicy extends Fragment {

    public PrivacyPolicy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_privacy_policy, container, false);

        WebView mywebview = v.findViewById(R.id.webview);
        mywebview.loadUrl("https://lovkush.github.io");
        return v;
    }
}