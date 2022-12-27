package com.project.practical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME=2000;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);

        firebaseAuth=FirebaseAuth.getInstance();

        //Thread for spash Screnn
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (firebaseAuth.getCurrentUser()!=null){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, FirstActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME);//spash timeout
    }

}
