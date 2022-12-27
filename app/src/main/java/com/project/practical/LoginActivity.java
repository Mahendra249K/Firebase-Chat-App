package com.project.practical;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail,userPassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private ImageView userImage;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail=findViewById(R.id.userEmail);
        userPassword=findViewById(R.id.userPassword);
        loginButton=findViewById(R.id.loginButton);
        progressBar=findViewById(R.id.progressBar);
        firebaseAuth=FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sEmail=userEmail.getText().toString();
                String sPassword=userPassword.getText().toString();

                if (userPassword.getText().toString().trim().isEmpty()){
                    Toast.makeText(LoginActivity.this, "password", Toast.LENGTH_SHORT).show();
                }
                else if (userEmail.getText().toString().trim().isEmpty()){
                    Toast.makeText(LoginActivity.this, "email", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


    }


}