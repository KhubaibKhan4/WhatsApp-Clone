package com.example.whatsappclone.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whatsappclone.Fragments.ChatFragment;
import com.example.whatsappclone.Fragments.ProfileFragment;
import com.example.whatsappclone.Fragments.StoriesFragment;
import com.example.whatsappclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button Logout;
    BottomNavigationView navigationView;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference myRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        String phone_no = getIntent().getStringExtra("phone_no");
        loadFragment(new ChatFragment());

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_chat) {
                    ChatFragment chatFragment = new ChatFragment();
                    loadFragment(chatFragment);

                } else if (id == R.id.nav_stories) {
                    loadFragment(new StoriesFragment());
                } else if (id == R.id.nav_profile) {
                    loadFragment(new ProfileFragment());
                }
                return true;
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = (FragmentManager) getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online");
                }
            }
        });
    }
}