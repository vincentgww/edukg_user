package com.fairychild.edukguser.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
//import android.support.v4.view.ViewPager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.andy.library.ChannelBean;
import com.fairychild.edukguser.Activity.CategoryArrangement;
import com.fairychild.edukguser.MessageEvent;
import com.fairychild.edukguser.MyViewPager;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.ViewPagerAdapterForNav;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener{

    public interface FragmentListener {
        void switchToSearch();
    }

    public static HomeFragment newInstance(){
        HomeFragment indexFragment = new HomeFragment();
        return indexFragment;
    }

    HomeFragment.FragmentListener listener;

    private ViewPagerAdapterForNav mViewPagerAdapterForNav;
    private MyViewPager mViewPager;
    private FloatingActionButton mImgBtn;
    private TabLayout tabLayout;
    private MaterialToolbar topAppBar;
    private ActionMenuItemView mBtnSearch;



    List<Fragment> mFragments;
    //List<TabLayout.Tab> mTabs;
    ArrayList<String> mTitles;
    private ArrayList<ChannelBean> channelBeans;
    private int mCurrentSelect=0;

    @Override
    public void onAttach(Context context) {
        listener = (HomeFragment.FragmentListener) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        mViewPager = view.findViewById(R.id.pager);
        tabLayout=view.findViewById(R.id.tab_layout);
        mImgBtn = view.findViewById(R.id.imgBtn);
        topAppBar = (MaterialToolbar) view.findViewById(R.id.top_app_bar);
        mBtnSearch = view.findViewById(R.id.search);
        initFragments();
        initData();
        mViewPagerAdapterForNav = new ViewPagerAdapterForNav(getContext(),getChildFragmentManager(),mFragments,mTitles);
        //mViewPagerAdapterForNav.setFragments(mFragments);
        mViewPager.setAdapter(mViewPagerAdapterForNav);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentSelect=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(mViewPager);
        //tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                mViewPager.setCurrentItem(tab.getPosition(),false);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mImgBtn.setOnClickListener(this);


        topAppBar.setTitle("首页");


        mBtnSearch.setOnClickListener(this);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        //initData();
        return view;
    }

    //@Override
    //public void setUserVisibleHint(boolean isVisibleToUser){
    //    super.setUserVisibleHint(isVisibleToUser);
    //    if(getUserVisibleHint()){
    //        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    //            @Override
    //            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    //            }

    //            @Override
//                public void onPageSelected(int position) {
                    //Tabs.get(position).select();
//                    mViewPager.setCurrentItem(position);
                    //selectTab(position);
//                }

//                @Override
//                public void onPageScrollStateChanged(int state) {

//                }
//            });
//        }
//    }

    //private void selectTab(int i){
       // mTabs.get(i).select();
    //}

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    private void addTab(String title){
        TabLayout.Tab t = tabLayout.newTab();
        t.setText(title);
        tabLayout.addTab(t);
        //mTabs.add(t);
    }

    private void initFragments(){
        mFragments = new ArrayList<>();
        mTitles=new ArrayList<>();
        //mTabs = new ArrayList<>();
        //mTitles=new ArrayList<>();
    }

    private void initData(){
        channelBeans = new ArrayList<ChannelBean>();
        channelBeans.add(new ChannelBean("BIOLOGY",false));
        channelBeans.add(new ChannelBean("CHEMISTRY",false));
        channelBeans.add(new ChannelBean("CHINESE",false));
        channelBeans.add(new ChannelBean("ENGLISH",false));
        channelBeans.add(new ChannelBean("GEO",false));
        channelBeans.add(new ChannelBean("HISTORY",false));
        channelBeans.add(new ChannelBean("MATH",false));
        channelBeans.add(new ChannelBean("PHYSICS",false));
        channelBeans.add(new ChannelBean("POLITICS",false));
        for(int i=0;i<channelBeans.size();i++){
            if(channelBeans.get(i).isSelect()){
                addTab(channelBeans.get(i).getName());
                mFragments.add(TabFragment.newInstance(channelBeans.get(i).getName().toLowerCase(Locale.ROOT)));
                mTitles.add(channelBeans.get(i).getName());
                //mViewPagerAdapterForNav.setFragments(mFragments);
            }
        }
    }

    @Override
    public void onClick(View V){
        Intent intent = null;
        switch (V.getId()){
            default:
                break;
            case R.id.imgBtn:
                ArrayList<String> mCategory = new ArrayList<>();
                ArrayList<String> mDelCategory = new ArrayList<>();
                intent = new Intent((AppCompatActivity)getActivity(), CategoryArrangement.class);
                for(int i = 0; i < channelBeans.size(); i++){
                    if(channelBeans.get(i).isSelect()){
                        mCategory.add(channelBeans.get(i).getName());
                    }
                    else{
                        mDelCategory.add(channelBeans.get(i).getName());
                    }
                }
                intent.putExtra("cat",mCategory);
                intent.putExtra("delCat",mDelCategory);
                startActivityForResult(intent,0);
                break;
            case R.id.search:
                listener.switchToSearch();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (resultCode){
            case Activity.RESULT_OK:
                ArrayList<String> mCategory=new ArrayList<>();
                ArrayList<String> mDelCategory=new ArrayList<>();
                mCategory=(ArrayList<String>) (data.getSerializableExtra("cat"));
                mDelCategory=(ArrayList<String>) (data.getSerializableExtra("delCat"));
                System.out.println(mCategory);
                channelBeans.clear();
                for(int i=0;i<mCategory.size();i++){
                    channelBeans.add(new ChannelBean(mCategory.get(i),true));
                }
                for(int i=0;i<mDelCategory.size();i++){
                    channelBeans.add(new ChannelBean(mDelCategory.get(i),false));
                }
                mTitles.clear();
                mFragments.clear();
                tabLayout.removeAllTabs();
                mViewPager.setSaveFromParentEnabled(false);
                //mViewPagerAdapterForNav.removeAllFragments();
                //mViewPagerAdapterForNav.notifyDataSetChanged();
                for(int i=0;i<mCategory.size();i++){
                        addFragment(mCategory.get(i));
                        mViewPager.setCurrentItem(mFragments.size()-1);
                }
                mViewPagerAdapterForNav = new ViewPagerAdapterForNav(getContext(),getChildFragmentManager(),mFragments,mTitles);
                mViewPager.setAdapter(mViewPagerAdapterForNav);
                //mViewPager.setOffscreenPageLimit(mFragments.size());
                //mViewPager.setSaveFromParentEnabled(false);
                break;
            default:
                break;
        }
    }
    private void addFragment(String title){
        mTitles.add(title);
        TabLayout.Tab tab=tabLayout.newTab();
        tab.setText(title);
        tabLayout.addTab(tab);
        mFragments.add(TabFragment.newInstance(title.toLowerCase(Locale.ROOT)));
        mViewPagerAdapterForNav.setFragments(mFragments,mTitles);
        mViewPager.setOffscreenPageLimit(mFragments.size());
    }
    private void delFragment(){
        mTitles.clear();
        tabLayout.removeAllTabs();
        mFragments.clear();
        mViewPagerAdapterForNav.setFragments(mFragments,mTitles);
        mViewPager.setOffscreenPageLimit(mFragments.size());
    }
}

