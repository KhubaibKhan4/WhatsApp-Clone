package com.example.whatsappclone.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Date;

public class ProfileDetailedActivity extends AppCompatActivity {
    ImageView ProfileImage;
    TextView UsernameTxt, FullNameTxt, DateTxt, PhoneTxt, GenderTxt,UserIdTxt,StatusTxt;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference myRef;
    Button backToChat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detailed);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        String username = getIntent().getStringExtra("username");
        String profile = getIntent().getStringExtra("profile");
        String status = getIntent().getStringExtra("status");
        String fullname = getIntent().getStringExtra("fullname");
        String date = getIntent().getStringExtra("date");
        String gender = getIntent().getStringExtra("gender");
        String receiverId = getIntent().getStringExtra("userid");
        String phone = getIntent().getStringExtra("phone");

        backToChat = (Button) findViewById(R.id.backToChat);
        ProfileImage = (ImageView) findViewById(R.id.ProfileDetailedProfile);
        UsernameTxt = (TextView) findViewById(R.id.ProfileDetailedUsername);
        FullNameTxt = (TextView) findViewById(R.id.ProfileDetailedFullname);
        DateTxt = (TextView) findViewById(R.id.ProfileDetailedDate);
        PhoneTxt = (TextView) findViewById(R.id.ProfileDetailedPhone);
        GenderTxt = (TextView) findViewById(R.id.ProfileDetailedGender);
        UserIdTxt = (TextView) findViewById(R.id.ProfileDetailedUserId);
        StatusTxt = (TextView) findViewById(R.id.ProfileDetailedStatus);


        Glide.with(this)
                .load(profile).into(ProfileImage);
        UsernameTxt.setText("Username:"+username);
        FullNameTxt.setText("FullName:"+fullname);
        DateTxt.setText("Date:"+date);
        PhoneTxt.setText("Phone NO:"+phone);
        GenderTxt.setText("Gender:"+gender);
        UserIdTxt.setText("UserID:"+receiverId);
        StatusTxt.setText("Status:"+status);

        backToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}