package com.example.whatsappclone.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Adapters.StoriesAdapter;
import com.example.whatsappclone.Models.Stories;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class StoriesFragment extends Fragment {

    RecyclerView recyclerView;
    List<Stories> storiesList;
    StoriesAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference myStorageRef;
    FloatingActionButton addStoriesBtn;
    AlertDialog alertDialog;
    String imageUrl;
    String username, profile;

    public StoriesFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stories, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        storage = FirebaseStorage.getInstance();
        myStorageRef = storage.getReference("Images/");


        addStoriesBtn = view.findViewById(R.id.addFloatBtn);
        recyclerView = view.findViewById(R.id.recyclerview_stories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storiesList = new ArrayList<>();
        adapter = new StoriesAdapter(getContext(), storiesList);
        recyclerView.setAdapter(adapter);

        myRef.child("Stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storiesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Stories stories = dataSnapshot.getValue(Stories.class);
                    storiesList.add(stories);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addStoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStoriesDialog();
            }
        });


        try {
            fetchAllData();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void fetchAllData() {
        myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Toast.makeText(getContext(), "" + users.getUsername(), Toast.LENGTH_SHORT).show();
                username = users.getUsername();
                profile = users.getProfile();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    private void showAddStoriesDialog() {
        // Inflate the dialog layout
        View storyView = LayoutInflater.from(getContext()).inflate(R.layout.add_stories, null);

        // Get references to the UI elements in the dialog layout

        ImageView addStoriesImage = storyView.findViewById(R.id.stories_add_image);
        EditText edtStory_Desc = storyView.findViewById(R.id.edtStories_Description);
        Button uploadStoriesBtn = storyView.findViewById(R.id.uploadStoriesBtn);


        addStoriesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        // Set a click listener for the update button
        uploadStoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the updated profile information from the UI elements


                // Dismiss the dialog
                alertDialog.dismiss();
            }
        });

        // Create the dialog and show it
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(storyView);
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            Uri uri = data.getData();
            myStorageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageUrl = myStorageRef.toString();
                    myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Calendar calendar = Calendar.getInstance();
                            Date today = calendar.getTime();
                            String time = String.valueOf(today.getTime());
                            String date = String.valueOf(today.getDate());
                            String month = String.valueOf(today.getMonth());
                            String year = String.valueOf(today.getYear());

                            String TODAY_DATE = date + "/" + month + "/" + year;

                            Stories stories = new Stories(username, profile, uri.toString(), TODAY_DATE);
                            myRef.child("Stories").child(UUID.randomUUID().toString()).setValue(stories)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Image uploaded successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "Failed to upload image: " + e.getMessage());
                                        }
                                    });
                        }
                    });
                }
            });
        }
    }

}