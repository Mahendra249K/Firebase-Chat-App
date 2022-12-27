package com.project.practical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.Adpter.UserAdpter;
import com.project.Fragment.ChatFragment;
import com.project.Fragment.ProfileFragment;
import com.project.Fragment.WelcomeFragment;
import com.project.Model.ChatModel;
import com.project.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle hamburgerToggle;
    private NavigationView navigationView;
    private DrawerLayout navigationDrawerLayout;
    private Toolbar toolbar;
    private FrameLayout framLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView=findViewById(R.id.navigationView);
        navigationDrawerLayout=findViewById(R.id.navigationDrawerLayout);

        framLayout=findViewById(R.id.framLayout);
        toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tupple");
        hamburgerToggle=new ActionBarDrawerToggle(MainActivity.this,navigationDrawerLayout,R.string.Opan,R.string.Close);
        navigationDrawerLayout.addDrawerListener(hamburgerToggle);
        hamburgerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WelcomeFragment welcomeFragment=new WelcomeFragment();
        loadFragment(welcomeFragment);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.profile){
                    navigationDrawerLayout.closeDrawers();
                    ProfileFragment profileFragment=new ProfileFragment();
                    loadFragment(profileFragment);
                }if (item.getItemId()==R.id.logout){
                    navigationDrawerLayout.closeDrawers();
                    logout();
                }
                if (item.getItemId()==R.id.chatHistory){
                    navigationDrawerLayout.closeDrawers();
                    ChatFragment chatFragment=new ChatFragment();
                    loadFragment(chatFragment);

                }

                if (item.getItemId()==R.id.aboutUs){
                    navigationDrawerLayout.closeDrawers();

                }
                return true;
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // here set toolbarItem select True
        if (hamburgerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        new AlertDialog.Builder(MainActivity.this,R.style.ThemeOverlay_AppCompat_Dialog_Alert)
                .setTitle("Logout")
                .setMessage("Are you sure, You want to Logout ?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(MainActivity.this,FirstActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }).setNegativeButton("Cancel",null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



}