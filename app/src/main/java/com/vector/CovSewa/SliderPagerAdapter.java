package com.vector.CovSewa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;


public class SliderPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<String> image_arraylist;
    private TextView textView;
    private ImageView imageView;

    public SliderPagerAdapter(Activity activity, ArrayList<String> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_slider, container, false);
        imageView = view.findViewById(R.id.imgPager);

        textView = view.findViewById(R.id.textPager);

        switch (position){
            case 0:
                imageView.setImageDrawable(activity.getResources().getDrawable( R.drawable.s1 ));
                textView.setText("CONNECT WITH POTENTIAL DONORS/SUPPLIERS AT CLICK OF BUTTON");
                break;
            case 1:
                imageView.setImageDrawable(activity.getResources().getDrawable( R.drawable.s2 ));
                textView.setText("COVSEWA IS A NON-PROFIT INITIATIVE TO PROVIDE A HELPING HAND TO THOSE IN NEED");
                break;
            case 2:
                imageView.setImageDrawable(activity.getResources().getDrawable( R.drawable.s3));
                textView.setText("FIND ALL YOUR MEDICAL EQUIPMENT OXYGEN PLASMA MEALS NONPRESCRIPTION MEDICINES SANITIZERS/MASKS ALL IN ONE PLACE");
                break;
        }


        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}