package com.fairychild.edukguser;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;
//import android.support.v4.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.Nullable;

public class ViewPagerAdapterForNav extends FragmentPagerAdapter {
    //碎片集合
    private List<Fragment> mFragments = new ArrayList<>();
    private Context context;
    private FragmentManager fm;
    private List<String> tags;
    private ArrayList<String> tabNames;
    //private List<TabLayout.Tab> pageTitles = new ArrayList<>();

    public ViewPagerAdapterForNav(Context context,FragmentManager fm,List<Fragment> fragments,ArrayList<String> tabNames) {
        super(fm);
        this.fm=fm;
        this.tags=new ArrayList<>();
        //this.tabNames=tabNames;
        this.context=context;
        this.mFragments=fragments;
        this.tabNames=tabNames;
        notifyDataSetChanged();
    }
    public ViewPagerAdapterForNav(Context context,FragmentManager fm){
        super(fm);
        this.fm=fm;
        this.tags=new ArrayList<>();
        this.context=context;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object){
        if(!((Fragment)object).isAdded()||mFragments.contains(object)){
            return PagerAdapter.POSITION_NONE;
        }
        return mFragments.indexOf(object);
    }

    @Override
    public int getCount() {
        if (mFragments == null){
            return 0;
        }else{
            return mFragments.size();
        }
    }
    @NonNull
    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    public void setFragments(List<Fragment> fragments,ArrayList<String> titles) {
        //this.mFragments.clear();
        //this.tabNames.clear();
        this.mFragments=fragments;
        this.tabNames=titles;
        notifyDataSetChanged();
    }



     public void removeAllFragments(){
        for(int i=mFragments.size()-1;i>=0;i--){
            Fragment fragment=mFragments.get(i);
            mFragments.remove(fragment);
            removeFragmentInternal(fragment);
        }
        notifyDataSetChanged();
    }
    private void removeFragmentInternal(Fragment fragment){
        FragmentTransaction transaction=fm.beginTransaction();
        transaction.remove(fragment);
        transaction.commitNow();
    }

    @Override
    public long getItemId(int position){
        return mFragments.get(position).hashCode();
    }

    //@Override
    //public Object instantiateItem(@NonNull ViewGroup container, int position) {
    //    Fragment instantiateItemFragment=(Fragment) super.instantiateItem(container,position);
    //    Fragment itemFragment=mFragments.get(position);
    //    if(instantiateItemFragment==itemFragment){
    //        return instantiateItemFragment;
    //    }
    //    else{
    //        fm.beginTransaction().add(container.getId(),itemFragment).commit();
    //        return itemFragment;
    //    }
    //}
//
    //@Override
    //public void destroyItem(@NonNull ViewGroup container, int position,@NonNull Object object) {
    //    Fragment fragment=(Fragment) object;
    //    if(mFragments.contains(fragment)){
    //        super.destroyItem(container,position,object);
    //        return;
    //    }
    //    if(!fm.isStateSaved()){
    //        fm.beginTransaction().remove(fragment).commit();
    //    }
    //}
//
    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
//
    @Nullable
    @Override
    public String getPageTitle(int i){
        return tabNames.get(i);
    }
}
