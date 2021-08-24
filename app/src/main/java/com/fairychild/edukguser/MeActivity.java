package com.fairychild.edukguser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MeActivity extends AppCompatActivity {
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人界面
                Intent intent = new Intent(MeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        BottomNavigationView navigation=(BottomNavigationView) findViewById(R.id.m_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    Intent intent = new Intent(MeActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_me:
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