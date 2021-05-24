package com.vector.CovSewa.Activities.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vector.CovSewa.R;
import com.vector.CovSewa.AppReviewData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppReviewAdapter extends RecyclerView.Adapter<AppReviewAdapter.ViewHolder> {

    List<AppReviewData> requestData;
    public AppReviewAdapter(List<AppReviewData> requestData) {
        this.requestData = requestData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_card2,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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

        public void bindView(AppReviewData appReviewData){
            title.setText(appReviewData.getTopic());
            name.setText(appReviewData.getUserName());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd,MMM");
            String dateString = formatter.format(new Date(appReviewData.getTime()));
            time.setText(dateString);
            description.setText(appReviewData.getDescription());
        }

    }

}
