package com.project.practical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    EditText userName,userEmail,userPassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton=findViewById(R.id.registerButton);
        userPassword=findViewById(R.id.userPassword);
        userEmail=findViewById(R.id.userEmail);
        userName=findViewById(R.id.userName);
        firebaseAuth=FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (userName.getText().toString().trim().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "userName", Toast.LENGTH_SHORT).show();
                }
                else if (userPassword.getText().toString().trim().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "password", Toast.LENGTH_SHORT).show();
                }
                else if (userEmail.getText().toString().trim().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "email", Toast.LENGTH_SHORT).show();
                }
                else {
                    String sUserName=userName.getText().toString();
                    String sEmail=userEmail.getText().toString();
                    String sPassword=userPassword.getText().toString();

                    Register(sEmail,sPassword,sUserName);
                }
            }
        });
    }

    public void Register(String email,String password,String userName){

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                    String userId=firebaseUser.getUid();

                    reference= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",userName);
                    hashMap.put("imageUrl","deafault");

                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (task.isSuccessful()){
                                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "You Can't Register with this email", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}