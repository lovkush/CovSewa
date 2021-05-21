package com.vector.CovSewa.Activities.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vector.CovSewa.R;
import com.vector.CovSewa.RequestData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.ViewHolder> {

    List<RequestData> requestData;
    OnCardClickListener cardClickListener;
    public RequestListAdapter(List<RequestData> requestData, OnCardClickListener onCardClickListener) {
        this.requestData = requestData;
        this.cardClickListener = onCardClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card,null),cardClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bindView(requestData.get(position));

    }




    @Override
    public int getItemCount() {
        return requestData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnCardClickListener onCardClickListener;
        TextView title, location,time;

        public ViewHolder(View itemView, OnCardClickListener cardClickListener) {
            super(itemView);
            title=itemView.findViewById(R.id.name);
            location =itemView.findViewById(R.id.location);
            time = itemView.findViewById(R.id.date);
            this.onCardClickListener = cardClickListener;
            itemView.setOnClickListener(this);
        }

        public void bindView(RequestData requestData){
            title.setText(requestData.getTopic());
            location.setText(requestData.getContact());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm dd,MMM");
            String dateString = formatter.format(new Date(requestData.getTime()));
            time.setText(dateString);
        }

        @Override
        public void onClick(View v) {
            onCardClickListener.onCardClick(getAdapterPosition() );
        }
    }

    public interface OnCardClickListener{
        void onCardClick(int position);
    }
}
