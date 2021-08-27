package com.fairychild.edukguser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.fairychild.edukguser.ViewPagerAdapterForNav;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public static HomeFragment newInstance(){
        HomeFragment indexFragment = new HomeFragment();
        return indexFragment;
    }
    MeFragment.FragmentListener listener;
    private ViewPagerAdapterForNav mViewPagerAdapterForNav;
    private ViewPager mViewPager;
    List<Fragment> mFragments;
    List<TabLayout.Tab> mTabs;
    private View lastView;
    private Context parent;
    private TabLayout tabLayout;

    private MaterialToolbar topAppBar;

    @Override
    public void onAttach(Context context) {
        listener = (MeFragment.FragmentListener) context;
        super.onAttach(context);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        lastView = view;
        mViewPager = view.findViewById(R.id.pager);
        initFragments();
        mViewPagerAdapterForNav = new ViewPagerAdapterForNav(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapterForNav);
        mViewPagerAdapterForNav.setFragments(mFragments);
        mViewPager.setCurrentItem(0);
        tabLayout=view.findViewById(R.id.tab_layout);
        //tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
            });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabs.get(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Button btnSignIn = view.findViewById(R.id.btn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragments.add(TabFragment.newInstance());
                mViewPagerAdapterForNav.setFragments(mFragments);
                addTab("tab"+mFragments.size());
                //mViewPager.setOffscreenPageLimit(mFragments.size());
            }
        });

        topAppBar = (MaterialToolbar) view.findViewById(R.id.top_app_bar);
        topAppBar.setTitle("首页");

        //mViewPager.setOffscreenPageLimit(mFragments.size());
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    private void addTab(String title){
        TabLayout.Tab t = tabLayout.newTab();
        t.setText(title);
        tabLayout.addTab(t);
        mTabs.add(t);

    }

    private void initFragments(){
        mFragments = new ArrayList<>();
        mTabs = new ArrayList<>();
    }
}
