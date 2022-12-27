package com.project.practical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.Adpter.UserAdpter;
import com.project.Model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recycleView;
    List<User> users;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycleView=findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(UsersActivity.this));
        users=new ArrayList<>();

        getData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
    private void getData() {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    user.setKey(snapshot.getKey());

                    assert user!=null;
                    assert firebaseUser!=null;
                    if (!user.getId().equals(firebaseUser.getUid())){
                        users.add(user);
                    }
                }

                Intent intent=new Intent();
                intent.putExtra("longClickKey","users");
                UserAdpter userAdpter=new UserAdpter(UsersActivity.this,users,intent);
                recycleView.setAdapter(userAdpter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}