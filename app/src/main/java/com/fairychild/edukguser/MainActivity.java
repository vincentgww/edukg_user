package com.fairychild.edukguser;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatSideChannelService;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fairychild.edukguser.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Fragment> mFragments;
    //组件
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    //适配器
    private ViewPagerAdapterForNav mViewPagerAdapterForNav;

    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElement();
        initFragments();
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                menuItem = mBottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //为ViewPager设置适配器
        mViewPagerAdapterForNav = new ViewPagerAdapterForNav(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapterForNav);
        mViewPagerAdapterForNav.setFragments(mFragments);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_query:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_functions:
                    mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_me:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    private void initElement(){
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.activity_main_bottom_navigation_view);
        mViewPager = (ViewPager)findViewById(R.id.activity_main_view_pager);
    }

    private void initFragments(){
        mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.newInstance());
        //mFragments.add(Pt_jobFragment.newInstance());
        //.add(MessageFragment.newInstance());
        //mFragments.add(PersonalFragment.newInstance());
    }
}