package com.fairychild.edukguser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.fairychild.edukguser.ViewPagerAdapterForNav;
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
    private View lastView;
    private Context parent;
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
        TabLayout tabLayout=view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    private void initFragments(){
        mFragments = new ArrayList<>();
        mFragments.add(TabFragment.newInstance());
        mFragments.add(TabFragment.newInstance());
        mFragments.add(TabFragment.newInstance());
    }
}
