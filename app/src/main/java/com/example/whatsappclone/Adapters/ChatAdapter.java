package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Activities.ChatActivity;
import com.example.whatsappclone.Models.Chat;
import com.example.whatsappclone.R;

import org.w3c.dom.Text;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    List<Chat> chatList;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.username.setText(chat.getUsername());
        holder.status.setText(chat.getStatus());

        Glide.with(context)
                .load(chat.getProfile()).into(holder.profile);

        holder.Container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("username", chat.getUsername());
                intent.putExtra("status", chat.getStatus());
                intent.putExtra("profile", chat.getProfile());
                intent.putExtra("fullname", chat.getFullname());
                intent.putExtra("date", chat.getDate());
                intent.putExtra("gender", chat.getGender());
                intent.putExtra("userid", chat.getUserid());
                intent.putExtra("phone", chat.getPhone());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView username, status;
        CardView Container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.chat_profile);
            username = itemView.findViewById(R.id.chat_username);
            status = itemView.findViewById(R.id.chat_status);
            Container = itemView.findViewById(R.id.container_users);
        }
    }
}
