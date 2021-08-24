package com.fairychild.edukguser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation=(BottomNavigationView) findViewById(R.id.m_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_me:
                    Intent intent = new Intent(MainActivity.this, MeActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_home:
                    item.setChecked(true);
                    return true;
                case R.id.navigation_functions:
                    item.setChecked(true);
                    break;
                case R.id.navigation_query:
                    item.setChecked(true);
                    break;
            }
            return false;
        }
    };
}