package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Activities.StoriesDetailedActivity;
import com.example.whatsappclone.Models.Stories;
import com.example.whatsappclone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder> {
    Context context;
    List<Stories> storiesList;

    public StoriesAdapter(Context context, List<Stories> storiesList) {
        this.context = context;
        this.storiesList = storiesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_stories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stories stories = storiesList.get(position);
        holder.stories_username.setText(stories.getUsername());
        holder.stories_date.setText(stories.getDate());
        Glide.with(context)
                .load(stories.getStories_image())
                .into(holder.stories_profile);

        holder.container_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoriesDetailedActivity.class);
                intent.putExtra("username", stories.getUsername());
                intent.putExtra("date", stories.getDate());
                intent.putExtra("profile", stories.getProfile());
                intent.putExtra("stories_image", stories.getStories_image());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView container_stories;
        CircleImageView stories_profile;
        TextView stories_username, stories_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            container_stories = itemView.findViewById(R.id.container_stories);
            stories_profile = itemView.findViewById(R.id.stories_profile);
            stories_username = itemView.findViewById(R.id.stories_username);
            stories_date = itemView.findViewById(R.id.stories_date);
        }
    }
}
