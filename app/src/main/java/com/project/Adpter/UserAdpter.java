package com.project.Adpter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.project.Model.User;
import com.project.practical.FirstActivity;
import com.project.practical.MainActivity;
import com.project.practical.MessageActivity;
import com.project.practical.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdpter extends RecyclerView.Adapter<UserAdpter.myHolder> {

    Context context;
    List<User> users;
    User user;
    Intent intent;
    OnItemClickLister itemClickListener;

    public UserAdpter(Context context, List<User> users,Intent intent) {
        this.context = context;
        this.users = users;
        this.intent = intent;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleview, parent, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {

        user=users.get(position);

        holder.userName.setText(user.getUserName());
        if (user.getImageUrl().equals("deafault")){
            holder.userImage.setImageResource(R.drawable.user);
        }
        else {
            Glide.with(context).load(user.getImageUrl()).into(holder.userImage);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class myHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView userName;
        public myHolder(@NonNull View itemView) {
            super(itemView);

            userImage=itemView.findViewById(R.id.userImage);
            userName=itemView.findViewById(R.id.userName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MessageActivity.class);
                    intent.putExtra("userId",users.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String s=intent.getStringExtra("longClickKey");
                    if (s.equals("main")){

                        new AlertDialog.Builder(context,R.style.ThemeOverlay_AppCompat_Dialog_Alert)
                                .setTitle("Delete")
                                .setMessage("Are you sure, You want to Delete ?")
                                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (itemClickListener != null){
                                            int position=getAdapterPosition();
                                            if (position != RecyclerView.NO_POSITION){
                                                itemClickListener.onDeleteClick(position,users.get(getAdapterPosition()).getId());
                                            }
                                        }
                                    }
                                }).setNegativeButton("Cancel",null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    return false;
                }
            });

        }
    }

    public interface OnItemClickLister{
        void onDeleteClick(int position,String userId);
    }

    public void setOnItemClickListner(OnItemClickLister onItemClickListener){

        itemClickListener=onItemClickListener;

    }

}
