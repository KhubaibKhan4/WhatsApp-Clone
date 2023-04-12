package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Models.Messages;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdpater extends RecyclerView.Adapter<MessageAdpater.ViewHolder> {
    Context context;
    List<Messages> messagesList;

    public MessageAdpater(Context context, List<Messages> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Messages messages = messagesList.get(position);
        if (messages.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.messageReceived.setVisibility(View.GONE);
            holder.messageSent.setText(messages.getMessage());
        } else {
            holder.messageReceived.setText(messages.getMessage());
            holder.messageSent.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageSent, messageReceived;
        RelativeLayout main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSent = itemView.findViewById(R.id.message_sent);
            messageReceived = itemView.findViewById(R.id.message_received);
            main = itemView.findViewById(R.id.main_message);
        }
    }
}
