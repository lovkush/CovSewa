package com.vector.CovSewa;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class Registration2 extends Fragment {

    public Registration2() {
    }

    UserData userData = new UserData();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.email_registration, container, false);

        return v;
    }

    private TextView textEmail, textPass, textRePass;
    private String email,pass , rePass;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserDataViewModel viewModel;

        textEmail = view.findViewById(R.id.textEmail);
        textPass = view.findViewById(R.id.input_password);
        textRePass = view.findViewById(R.id.input_reEnterPassword);


        Button btn = view.findViewById(R.id.btnRegister);
        viewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);

        btn.setOnClickListener(item -> {

            // Set a new item

            email = textEmail.getText().toString();
            pass = textPass.getText().toString();
            rePass = textRePass.getText().toString();

            if(validate()){
                String[] result = new String[2];
                result[0] = email;
                result[1] = pass;
                viewModel.selectEmailPass(result);
            }

        });
    }

    private boolean validate(){
        boolean result = false;

        if (email.isEmpty()) {
            textEmail.setError("Email is required");
            textEmail.requestFocus();
            return result;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textEmail.setError("Please enter a valid email");
            textEmail.requestFocus();
            return result;
        }

        if (pass.isEmpty()) {
            textPass.setError("Password is required");
            textPass.requestFocus();
            return result;
        }
        if (pass.length() < 6) {
            textPass.setError("Minimum length of password should be 6");
            textPass.requestFocus();
            return result;
        }

        if (!rePass.equals(pass)){
            textRePass.setError("Password does not match.");
            textRePass.requestFocus();
            return result;
        }

        return true;
    }
}
