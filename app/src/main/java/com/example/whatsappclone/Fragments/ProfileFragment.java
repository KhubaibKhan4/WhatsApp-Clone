package com.example.whatsappclone.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Models.Users;
import com.example.whatsappclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference myStorageReference;

    Button updateBtn, editProfileBtn;
    String value;
    AlertDialog alertDialog;
    CircleImageView Profile;
    String imageUrl;

    TextView txtUsername, txtFullName, txtGender, txtDate;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        storage = FirebaseStorage.getInstance();
        myStorageReference = storage.getReference().child("images/");

        txtUsername = view.findViewById(R.id.txtUsername);
        txtFullName = view.findViewById(R.id.txtFullname);
        txtDate = view.findViewById(R.id.txtDate);
        txtGender = view.findViewById(R.id.txtGender);

        editProfileBtn = view.findViewById(R.id.edtProfileBtn);
        Profile = view.findViewById(R.id.imageProfile);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateProfileDialog();
            }
        });


        try {
            fetchAllData();
            fetchPhone();
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void fetchAllData() {
        myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                txtUsername.setText("Username: " + users.getUsername());
                txtFullName.setText("FullName: " + users.getFullname());
                txtDate.setText("Date: " + users.getDate());
                txtGender.setText("Gender: " + users.getGender());

                Glide.with(getContext()).load(users.getProfile()).into(Profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            Uri uri = data.getData();
            myStorageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageUrl = myStorageReference.toString();
                    myStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).child("profile").setValue(uri.toString())
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

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    private void showUpdateProfileDialog() {
        // Inflate the dialog layout
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_profile_edit, null);

        // Get references to the UI elements in the dialog layout
        ImageView Profile = view.findViewById(R.id.profile_image);
        EditText edtUsername = view.findViewById(R.id.edtUsername);
        EditText edtFullName = view.findViewById(R.id.edtFullName);
        EditText edtGender = view.findViewById(R.id.edtGender);
        Button updateBtn = view.findViewById(R.id.updateBtn);
        DatePicker datePicker1 = view.findViewById(R.id.datePicker);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        // Set a click listener for the update button
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated profile information from the UI elements
                String username = edtUsername.getText().toString();
                String fullName = edtFullName.getText().toString();
                String gender = edtGender.getText().toString();
                int date = datePicker1.getDayOfMonth();
                int month = datePicker1.getMonth();
                int year = datePicker1.getYear();

                String Date = date + "/" + month + "/" + year;
                Users users = new Users(username, fullName, value, gender, Date);
                myRef.child("Users")
                        .child(FirebaseAuth.getInstance().getUid()).child("username")
                        .setValue(username)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), task.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                myRef.child("Users")
                        .child(FirebaseAuth.getInstance().getUid()).child("fullname")
                        .setValue(fullName)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), task.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                myRef.child("Users")
                        .child(FirebaseAuth.getInstance().getUid()).child("gender")
                        .setValue(gender)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), task.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                myRef.child("Users")
                        .child(FirebaseAuth.getInstance().getUid()).child("phone")
                        .setValue(value)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), task.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                myRef.child("Users")
                        .child(FirebaseAuth.getInstance().getUid()).child("date")
                        .setValue(Date)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), task.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                myRef.child("Users")
                        .child(FirebaseAuth.getInstance().getUid()).child("userid")
                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), task.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                // Update the user's profile information in the database
                // ...

                // Dismiss the dialog
                alertDialog.dismiss();
            }
        });

        // Create the dialog and show it
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }


    private void fetchPhone() {
        myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value = snapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}