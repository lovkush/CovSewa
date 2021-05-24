package com.vector.CovSewa.Activities.ui.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vector.CovSewa.RequestData;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PageViewModel extends ViewModel {
    ArrayList<RequestData> requestData = new ArrayList<>();
    //String UID  = FirebaseAuth.getInstance().getUid();

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<ArrayList<RequestData>> requestDataLiveData = Transformations.map(mIndex, new Function<Integer, ArrayList<RequestData>>() {
        @Override
        public ArrayList<RequestData> apply(Integer input) {

            FirebaseDatabase.getInstance().getReference().child("Request").orderByChild("donorId").equalTo("uBeFIgd2NCT2e7fCdStDdBEBKTG2").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot d : snapshot.getChildren()){
                        requestData.add( d.getValue(RequestData.class));
                        Log.d(TAG, "onDataChange: "+d.getValue(RequestData.class).getDoneeId()+ " " + input);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: " + error);
                }
            });

            if(input==1){
                return requestData;
            }
            if(input == 2){
                ArrayList<RequestData> requestData1 = new ArrayList<>();
                for(RequestData r: requestData){
                    if(r.isSaveStatus()){
                        requestData1.add(r);
                    }
                }

                return requestData1;
            }

            return requestData;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<ArrayList<RequestData>> getRequestData() {
        return requestDataLiveData;
    }
}