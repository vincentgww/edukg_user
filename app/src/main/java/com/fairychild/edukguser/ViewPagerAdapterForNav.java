package com.fairychild.edukguser;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

public class ViewPagerAdapterForNav extends FragmentStatePagerAdapter {
    //碎片集合
    private List<Fragment> mFragments = new ArrayList<>();
    //private List<TabLayout.Tab> pageTitles = new ArrayList<>();

    public ViewPagerAdapterForNav(FragmentManager fm) {
        super(fm);
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
