package com.example.whatsappclone.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;

import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;


public class StoriesDetailedActivity extends AppCompatActivity {
    CircleImageView Profile;
    TextView Username, Date;
    ImageView Stories;
    RelativeLayout relativeLayout;
    ConstraintLayout constraintLayout;
    private Handler handler;
    private Runnable runnable;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_detailed);


        String username = getIntent().getStringExtra("username");
        String profile = getIntent().getStringExtra("profile");
        String date = getIntent().getStringExtra("date");
        String stories_image = getIntent().getStringExtra("stories_image");

        Profile = (CircleImageView) findViewById(R.id.storyDetailedProfile);
        Username = (TextView) findViewById(R.id.storyDetailedUsername);
        Date = (TextView) findViewById(R.id.storyDetailedDate);
        Stories = findViewById(R.id.storyDetailedStories);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint);


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Close the activity
                finish();
            }
        };

        // Start the timer for 30 seconds
        handler.postDelayed(runnable, 3000);

        Username.setText(username);
        Date.setText(date);

        Glide.with(this)
                .load(profile)
                .into(Profile);
        Glide.with(this)
                .load(stories_image)
                .into(Stories);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the runnable from the handler to prevent memory leaks
        handler.removeCallbacks(runnable);
    }
}