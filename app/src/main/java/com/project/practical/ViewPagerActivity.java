package com.project.practical;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.project.Fragment.DemoFragment;

public class ViewPagerActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pratical");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set Adpter in viewpager and tablayout
        viewPager.setAdapter(new tabPagerLayout(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        //set tablauout selectListner
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public class tabPagerLayout extends FragmentPagerAdapter {

        String[] viewPagerNames ={"Tutorial 1","Tutorial 2","Tutorial 3"};

        public tabPagerLayout(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new DemoFragment();
            }
            if (position == 1){
                return new DemoFragment();

            }if (position == 2){
                return new DemoFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            return viewPagerNames.length;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return viewPagerNames[position];
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}