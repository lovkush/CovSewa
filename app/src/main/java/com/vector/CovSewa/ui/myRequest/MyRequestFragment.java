package com.vector.CovSewa.ui.myRequest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vector.CovSewa.R;
import com.vector.CovSewa.ui.AboutUs.MyRequestViewModel;

public class MyRequestFragment extends Fragment implements View.OnClickListener{
    private MyRequestViewModel myRequestViewModel;
    private TextView requestReceived, requestGenerated;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request, container, false);



        return root;
    }



    private void changeView(TextView textView){
        textView.setTextColor(getResources().getColor(R.color.primaryDarkColor));
    }

    @Override
    public void onClick(View v) {
        /*if(v.getId()==R.id.textView3)

            if(v.getId()==R.id.textView3)*/
    }
}
