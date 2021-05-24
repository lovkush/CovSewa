package com.vector.CovSewa.Activities.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vector.CovSewa.R;
import com.vector.CovSewa.ReviewData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DonationReviewAdapter extends RecyclerView.Adapter<DonationReviewAdapter.ViewHolder> {


    List<ReviewData> requestData;
    public DonationReviewAdapter(List<ReviewData> requestData) {
        this.requestData = requestData;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_card2,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(requestData.get(position));
    }


    @Override
    public int getItemCount() {
        return requestData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, name,time,description;

        public ViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.productCardTitle);
            name =itemView.findViewById(R.id.productCardAdd);
            time = itemView.findViewById(R.id.productCardTime);
            description = itemView.findViewById(R.id.productCardDescription);
        }

        public void bindView(ReviewData appReviewData){
            title.setText(appReviewData.getTopic());
            name.setVisibility(View.GONE);
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd,MMM");
            String dateString = formatter.format(new Date(appReviewData.getTimestamp()));
            time.setText(dateString);
            description.setText(appReviewData.getDescription());
        }

    }

}
