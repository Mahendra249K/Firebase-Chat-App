package com.project.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.Model.ChatModel;
import com.project.practical.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdpter extends RecyclerView.Adapter<MessageAdpter.myHolder> {

    public  static final int MESSAGERIGHT=1;
    public  static final int MESSAGELEFT=2;
    Context context;
    List<ChatModel> chatModels;
    ChatModel chatModel;
    String imageURI;
    FirebaseUser firebaseUser;

    public MessageAdpter(Context context, List<ChatModel> chatModels, String imageURI) {
        this.context = context;
        this.chatModels = chatModels;
        this.imageURI = imageURI;
    }

    @NonNull
    @Override
    public MessageAdpter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==MESSAGERIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chatitem_right, parent, false);
            return new MessageAdpter.myHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chatitem_left, parent, false);
            return new MessageAdpter.myHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {

        chatModel=chatModels.get(position);

        holder.showMessage.setText(chatModel.getMessage());

        if (imageURI.equals("deafault")){
            holder.userImage.setImageResource(R.drawable.user);
        }
        else {
            Glide.with(context).load(imageURI).into(holder.userImage);
        }

    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (chatModels.get(position).getSender().equals(firebaseUser.getUid())){
            return MESSAGERIGHT;
        }
        else {
            return MESSAGELEFT;
        }
    }

    public class myHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView showMessage;
        public myHolder(@NonNull View itemView) {
            super(itemView);

            userImage=itemView.findViewById(R.id.userImage);
            showMessage=itemView.findViewById(R.id.showMessage);


        }
    }
}
