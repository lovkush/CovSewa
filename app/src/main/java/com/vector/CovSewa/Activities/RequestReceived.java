package com.vector.CovSewa.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vector.CovSewa.Activities.ui.main.PlaceholderFragment;
import com.vector.CovSewa.Activities.ui.main.RequestListAdapter;
import com.vector.CovSewa.R;
import com.vector.CovSewa.RequestData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RequestReceived extends AppCompatActivity  {

    TabLayout tabLayout;
    ViewPager viewPager;
    Context context;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_received);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        defineView();
        bindView();

        UID = FirebaseAuth.getInstance().getUid();
    }
    private void defineView(){
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.main_viewpager);
    }

    private void bindView(){
        setUpRequestList();

    }

    private void setupViewPager(ViewPager viewPager) {

        context = this.getApplicationContext();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //  adapter.addFragment(new CuriosityModeFeatured(),"Featured");
        new PlaceholderFragment();
        adapter.addFragment(PlaceholderFragment.newInstance(requestList), "Received");
        new PlaceholderFragment();
        adapter.addFragment(PlaceholderFragment.newInstance(requestList1),"Saved");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private List<RequestData> requestList=new ArrayList<>();
    private List<RequestData> requestList1=new ArrayList<>();

    private void setUpRequestList(){

        FirebaseDatabase.getInstance().getReference().child("Request").orderByChild("donorId").equalTo(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestList.clear();
                Log.d(TAG, "onDataChange: " + requestList.size());
                Iterator<DataSnapshot> iterator=  dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    DataSnapshot snapshot=iterator.next();
                    requestList.add(snapshot.getValue(RequestData.class));
                    Log.d(TAG, "onDataChange: " + snapshot.getValue(RequestData.class).getDoneeId());
                }
                if(requestList.size()==0){

                }
                setUpSavedList();
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });
    }
    private void setUpSavedList(){
        requestList1.clear();
            for(RequestData r : requestList)
                if(r.isSaveStatus())requestList1.add(r);
                setupViewPager(viewPager);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}