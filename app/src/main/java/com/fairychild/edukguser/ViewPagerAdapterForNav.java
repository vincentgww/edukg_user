package com.fairychild.edukguser;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
public class ViewPagerAdapterForNav extends FragmentPagerAdapter {
    //碎片集合
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> pageTitles = new ArrayList<>();

    public ViewPagerAdapterForNav(FragmentManager fm) {
        super(fm);
        pageTitles.add("tab1");
        pageTitles.add("tab2");
        pageTitles.add("tab3");
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

    @Override
    public String getPageTitle(int i){
        return pageTitles.get(i);
    }
}
