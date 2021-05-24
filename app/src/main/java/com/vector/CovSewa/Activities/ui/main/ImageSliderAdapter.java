package com.vector.CovSewa.Activities.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.vector.CovSewa.R;

import java.util.ArrayList;

public class ImageSliderAdapter  extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<Bitmap> image_arraylist;
    private ImageView imageView;
    Context mContext;

    public ImageSliderAdapter(Activity activity, ArrayList<Bitmap> image_arraylist,Context context) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.mContext = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.image_slider, container, false);
        imageView = view.findViewById(R.id.imageSlider);
        imageView.setImageBitmap(image_arraylist.get(position));

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