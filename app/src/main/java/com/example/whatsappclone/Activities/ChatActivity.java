package com.example.whatsappclone.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Adapters.MessageAdpater;
import com.example.whatsappclone.Fragments.ChatFragment;
import com.example.whatsappclone.Models.Messages;
import com.example.whatsappclone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    CircleImageView ProfileImage;
    ImageView BackBtn;
    TextView txtUsername, txtStatus;
    Toolbar toolbar;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference myRef, myRefSender, myRefReceiver;

    RecyclerView recyclerView;
    String fullname, date, gender, phone;
    ImageView messageSendBtn, ImageSendBtn;
    EditText edtMessage;
    MessageAdpater adpater;
    List<Messages> messagesList;
    String senderRoom, receiverRoom;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        recyclerView = (RecyclerView) findViewById(R.id.message_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesList = new ArrayList<>();
        adpater = new MessageAdpater(this, messagesList);
        recyclerView.setAdapter(adpater);


        String Username = getIntent().getStringExtra("username");
        String Status = getIntent().getStringExtra("status");
        String Profile = getIntent().getStringExtra("profile");
        String FullName = getIntent().getStringExtra("fullname");
        String Date = getIntent().getStringExtra("date");
        String Gender = getIntent().getStringExtra("gender");
        String UserId = getIntent().getStringExtra("userid");


        //init
        ProfileImage = (CircleImageView) findViewById(R.id.imageProfile);
        BackBtn = (ImageView) findViewById(R.id.imageBack);
        txtUsername = (TextView) findViewById(R.id.chatdUsername);
        txtStatus = (TextView) findViewById(R.id.chatdStatus);
        messageSendBtn = (ImageView) findViewById(R.id.messageSendBtn);
        edtMessage = (EditText) findViewById(R.id.message_input);

        txtUsername.setText(Username);
//      txtStatus.setText(Status);
        Glide.with(this).load(Profile).into(ProfileImage);

        senderRoom = FirebaseAuth.getInstance().getUid() + UserId;
        receiverRoom = UserId + FirebaseAuth.getInstance().getUid();

        myRefSender = FirebaseDatabase.getInstance().getReference("Chats").child(senderRoom);
        myRefReceiver = FirebaseDatabase.getInstance().getReference("Chats").child(receiverRoom);
        myRefSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesList.add(messages);
                }
                adpater.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messageSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edtMessage.getText().toString().toString();
                if (message.length() > 0) {
                    sendMessage(message);
                    edtMessage.setText("");
                }
            }
        });


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatActivity.this, ProfileDetailedActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("profile", getIntent().getStringExtra("profile"));
                intent.putExtra("status", getIntent().getStringExtra("status"));
                intent.putExtra("fullname", getIntent().getStringExtra("fullname"));
                intent.putExtra("date", getIntent().getStringExtra("date"));
                intent.putExtra("gender", getIntent().getStringExtra("gender"));
                intent.putExtra("userid", getIntent().getStringExtra("userid"));
                intent.putExtra("phone", getIntent().getStringExtra("phone"));
                startActivity(intent);

            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void sendMessage(String message) {
        String messageId = UUID.randomUUID().toString();
        Messages messages = new Messages(messageId, FirebaseAuth.getInstance().getUid(), message);
        messagesList.add(messages);

        myRefSender.child(messageId)
                .setValue(messages);

        myRefReceiver.child(messageId)
                .setValue(messages);

    }


    @Override
    protected void onStart() {
        super.onStart();
        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String newStatus = snapshot.getValue(String.class);
                        Toast.makeText(ChatActivity.this, newStatus, Toast.LENGTH_SHORT).show();
                        txtStatus.setText(newStatus);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}