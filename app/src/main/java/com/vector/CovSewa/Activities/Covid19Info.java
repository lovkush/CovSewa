package com.vector.CovSewa.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.vector.CovSewa.Activities.ui.main.AppReviewAdapter;
import com.vector.CovSewa.AppReviewData;
import com.vector.CovSewa.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class Covid19Info extends AppCompatActivity {

    ArrayList<String> VideoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Covid-19 Portal");


        YouTubeAdapter youTubeAdapter = new YouTubeAdapter(VideoList);
        RecyclerView recyclerView = findViewById(R.id.youtube_player_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(youTubeAdapter);

        FirebaseDatabase.getInstance().getReference().child("VideoId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                VideoList.clear();
                for(DataSnapshot s : snapshot.getChildren())
                VideoList.add(s.getValue(String.class));
                youTubeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.ViewHolder>{


        ArrayList<String> VideoId;
        public YouTubeAdapter(ArrayList<String> VideoId){
            this.VideoId = VideoId;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            YouTubePlayerView youTubePlayerView1;
            public ViewHolder(View itemView) {
                super(itemView);
                youTubePlayerView1 = itemView.findViewById(R.id.youtube_player_view1);
            }

            public void bindView(String s){
                getLifecycle().addObserver(youTubePlayerView1);

                youTubePlayerView1.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(s, 0);
                    }
                });
            }

        }


        @NonNull
        @Override
        public YouTubeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_card,null));
        }

        @Override
        public void onBindViewHolder(@NonNull YouTubeAdapter.ViewHolder holder, int position) {
            holder.bindView(VideoId.get(position));
        }

        @Override
        public int getItemCount() {
            return VideoId.size();
        }


    }
}

