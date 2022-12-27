package com.project.practical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.Adpter.MessageAdpter;
import com.project.Model.ChatModel;
import com.project.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView userImage;
    TextView userName;
    Toolbar toolbar;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    RecyclerView chatRecycleview;
    EditText input;
    Button sendMessageButton;
    MessageAdpter messageAdpter;
    List<ChatModel> chatModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        input = findViewById(R.id.input);
        chatRecycleview = findViewById(R.id.chatRecycleview);
        chatRecycleview.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String userID = getIntent().getStringExtra("userId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                userName.setText(user.getUserName());

                if (user.getImageUrl().equals("deafault")) {
                    userImage.setImageResource(R.drawable.user);
                } else {
                    Glide.with(MessageActivity.this).load(user.getImageUrl()).into(userImage);
                }

                readMessge(firebaseUser.getUid(), userID, user.getImageUrl());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input.getText().toString().trim().isEmpty()) {
                    sendMessage(firebaseUser.getUid(), userID, input.getText().toString());
                } else {

                }
                input.setText(null);
            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }



    public void sendMessage(String sender, String reciver, String message) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciver", reciver);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);
    }

    public void readMessge(String myID, String userID, String imageUri) {
        chatModels = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatModels.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                    if (chatModel.getReciver().equals(myID) && chatModel.getSender().equals(userID) ||
                            chatModel.getReciver().equals(userID) && chatModel.getSender().equals(myID)) {
                        chatModels.add(chatModel);
                    }
                    messageAdpter = new MessageAdpter(MessageActivity.this, chatModels, imageUri);
                    chatRecycleview.setAdapter(messageAdpter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}