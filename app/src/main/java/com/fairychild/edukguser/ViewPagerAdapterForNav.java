package com.fairychild.edukguser;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
public class ViewPagerAdapterForNav extends SmartFragmentStatePagerAdapter {
    //碎片集合
    private List<Fragment> mFragments = new ArrayList<>();

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

}
