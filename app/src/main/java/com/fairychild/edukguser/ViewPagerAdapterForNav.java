package com.fairychild.edukguser;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;
//import android.support.v4.app.Fragment;
import androidx.fragment.app.Fragment;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.tabs.TabLayout;

public class ViewPagerAdapterForNav extends FragmentPagerAdapter {
    //碎片集合
    private List<Fragment> mFragments = new ArrayList<>();
    private Context context;
    //private List<TabLayout.Tab> pageTitles = new ArrayList<>();

    public ViewPagerAdapterForNav(Context context,FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.context=context;
        this.mFragments=fragments;
        notifyDataSetChanged();
    }
    public ViewPagerAdapterForNav(Context context,FragmentManager fm){
        super(fm);
        this.context=context;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (mFragments == null){
            return 0;
        }else{
            return mFragments.size();
        }
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    public void setFragments(List<Fragment> fragments) {
        this.mFragments = fragments;
        notifyDataSetChanged();
    }

    /*@Override
    public String getPageTitle(int i){
        return pageTitles.get(i);
    }*/
}
