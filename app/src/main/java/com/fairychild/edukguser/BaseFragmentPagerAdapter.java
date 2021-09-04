package com.fairychild.edukguser;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class BaseFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    private ArrayList<String> mTitles;
    private FragmentManager fm;
    private Context context;
    public BaseFragmentPagerAdapter(Context context,FragmentManager fm,List<Fragment> mFragments,ArrayList<String> mTitles){
        super(fm);
        this.context=context;
        this.fm=fm;
        this.mFragments=mFragments;
        this.mTitles=mTitles;
        notifyDataSetChanged();
    }
    public BaseFragmentPagerAdapter(Context context,FragmentManager fm){
        super(fm);
        this.context=context;
        this.fm=fm;
        notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int position){
        return mFragments.get(position);
    }
    @Override
    public int getCount(){
        return mFragments!=null?mFragments.size():0;
    }
    @Override
    public CharSequence getPageTitle(int position){
        return mTitles!=null || mTitles.size()>position?mTitles.get(position):"";
    }
    public void removeFragmentInternal(Fragment fragment){
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.remove(fragment);
        transaction.commitNow();
    }
}
