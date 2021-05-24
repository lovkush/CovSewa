package com.vector.CovSewa.Activities.ui.main;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vector.CovSewa.ProductData;
import com.vector.CovSewa.R;

import java.sql.Date;
import java.util.List;

public class productRecycler extends RecyclerView.Adapter<productRecycler.MyViewHolder> {

    private List<ProductData> productDataList;
    private OnCardClickListener mOnCardClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView productTopic, add, date,description;
        private ImageView imageView;
        OnCardClickListener onCardClickListener;
        public MyViewHolder(View view, OnCardClickListener onCardClickListener ) {

            super(view);
            productTopic = view.findViewById(R.id.productCardTitle);
            add = view.findViewById(R.id.productCardAdd);
            date = view.findViewById(R.id.productCardTime);
            description = view.findViewById(R.id.productCardDescription);
            imageView = view.findViewById(R.id.myDonationCardImage);
            this.onCardClickListener = onCardClickListener;
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            onCardClickListener.onCardClick(getAdapterPosition() );
        }


    }

    public interface OnCardClickListener{
        void onCardClick(int position);
    }

    public productRecycler(List<ProductData> productDataList, OnCardClickListener onCardClickListener){
        this.productDataList = productDataList;
        this.mOnCardClickListener=onCardClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View orderView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donation_card2, parent, false);

        return new MyViewHolder(orderView,mOnCardClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductData productData = productDataList.get(position);
        holder.productTopic.setText(productData.getCategory());
        holder.add.setText(productData.getAddLine2());
        holder.description.setText(productData.getDescription());
        holder.date.setText(String.valueOf(new Date(productData.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }

}

