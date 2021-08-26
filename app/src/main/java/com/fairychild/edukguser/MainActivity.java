package com.fairychild.edukguser;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.fairychild.edukguser.HomeFragment;
import com.fairychild.edukguser.LoginFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements MeFragment.FragmentListener,LoginFragment.LoginListener{
    List<Fragment> mFragments;
    //组件
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    //适配器
    private ViewPagerAdapterForNav mViewPagerAdapterForNav;
    //Chip Group
    private MenuItem menuItem;
    private FragmentTransaction transaction;
    private FragmentManager mSupportFragmentManager;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElement();
        initFragments();
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //FragmentManager fragmentManager = getSupportFragmentManager();
        mSupportFragmentManager = getSupportFragmentManager();
        switchFragments(0);
        //transaction = mSupportFragmentManager.beginTransaction();
        //transaction.commit();
        /*mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                if(position==4)
                    menuItem = mBottomNavigationView.getMenu().getItem(3);
                else
                    menuItem = mBottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        //为ViewPager设置适配器
        //mViewPagerAdapterForNav = new ViewPagerAdapterForNav(getSupportFragmentManager());
        //mViewPager.setAdapter(mViewPagerAdapterForNav);
        //mViewPagerAdapterForNav.setFragments(mFragments);
        //mViewPager.setOffscreenPageLimit(mFragments.size());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragments(0);
                    return true;
                case R.id.navigation_query:
                    switchFragments(1);
                    return true;
                case R.id.navigation_functions:
                    switchFragments(2);
                    return true;
                case R.id.navigation_me:
                    switchFragments(3);
                    return true;
            }
            return false;
        }
    };

    private void initElement(){
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.activity_main_bottom_navigation_view);
        //mViewPager = (ViewPager)findViewById(R.id.viewpager);
    }

    private void initFragments(){
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(MeFragment.newInstance());
        mFragments.add(LoginFragment.newInstance());
    }

    private void switchFragments(int FragmentId) {
        transaction = mSupportFragmentManager.beginTransaction();
        Fragment targetFragment = mFragments.get(FragmentId);
        if (!targetFragment.isAdded()) {
           transaction.add(R.id.frameLayout,targetFragment);
        }
        for (Fragment frag : mFragments) {

            if (frag.equals(targetFragment)) {
                /*先隐藏其他fragment*/
                transaction.show(frag);
            }
            else
                transaction.hide(frag);
        }
        transaction.commit();
        currentFragment = targetFragment;
    }

    public void switchToLogin(){
        switchFragments(4);
    }

    public void check(Editable phone, Editable password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "47.93.101.225:8080/login";
                    String json = "{\n" +
                            " \"account\":\"" + phone + "\",\n" +
                            " \"password\":\"" + password + "\"\n" +
                            "}";
                    String response = OkHttp.post(url, json);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}