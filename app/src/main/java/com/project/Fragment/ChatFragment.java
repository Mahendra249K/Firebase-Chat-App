package com.project.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.Adpter.UserAdpter;
import com.project.Model.ChatModel;
import com.project.Model.User;
import com.project.practical.R;
import com.project.practical.UsersActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment implements UserAdpter.OnItemClickLister  {

    private FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    UserAdpter userAdpter;
    List<User> userList;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    List<String> list;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);

        floatingActionButton=view.findViewById(R.id.floatingActionButton);
        progressBar=view.findViewById(R.id.progressBar);
        recyclerView=view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        list=new ArrayList<>();
        userList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference().child("Chats");


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UsersActivity.class));
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatModel chatModel=dataSnapshot.getValue(ChatModel.class);

                    if (chatModel.getSender().equals(firebaseUser.getUid())){
                        list.add(chatModel.getReciver());
                    }
                    if (chatModel.getReciver().equals(firebaseUser.getUid())){
                        list.add(chatModel.getSender());
                    }

                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void readChats() {
        reference=FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    user.setKey(snapshot.getKey());

                    for (String id:list){
                        if (user.getId().equals(id)){
                            if (userList.size()!=0){
                                for (User user1: userList){
                                    if (!user.getId().equals(user1.getId())){
                                        userList.add(user);
                                    }
                                }
                            }
                            else {
                                userList.add(user);
                            }
                        }
                    }
                }

                Intent intent=new Intent();
                intent.putExtra("longClickKey","main");
                userAdpter=new UserAdpter(getContext(),userList,intent);
                recyclerView.setAdapter(userAdpter);
//                userAdpter.setOnItemClickListner(getContext());
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDeleteClick(int position, String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String myID=firebaseUser.getUid();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                    if (chatModel.getReciver().equals(myID) && chatModel.getSender().equals(userId) ||
                            chatModel.getReciver().equals(userId) && chatModel.getSender().equals(myID)) {


                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                userAdpter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}