package com.fairychild.edukguser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import android.support.v4.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
//import android.support.v4.view.ViewPager;
import androidx.viewpager.widget.ViewPager;

import com.andy.library.ChannelActivity;
import com.andy.library.ChannelBean;
import com.fairychild.edukguser.Activity.CategoryArrangement;
import com.fairychild.edukguser.ViewPagerAdapterForNav;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener{
    public static HomeFragment newInstance(){
        HomeFragment indexFragment = new HomeFragment();
        return indexFragment;
    }
    MeFragment.FragmentListener listener;
    private ViewPagerAdapterForNav mViewPagerAdapterForNav;
    private ViewPager mViewPager;
    List<Fragment> mFragments;
    List<TabLayout.Tab> mTabs;
    private Button mImgBtn;
    private ArrayList<ChannelBean> channelBeans;
    String jsonStr="";
    private Gson gson;
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
        mViewPagerAdapterForNav = new ViewPagerAdapterForNav(getContext(),getChildFragmentManager(),mFragments);
        mViewPager.setAdapter(mViewPagerAdapterForNav);
        mViewPager.setCurrentItem(0);
        tabLayout=view.findViewById(R.id.tab_layout);
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
        //tabLayout.setupWithViewPager(mViewPager);
        //mViewPager.setOffscreenPageLimit(mFragments.size());
        mImgBtn= view.findViewById(R.id.imgBtn);
        mImgBtn.setOnClickListener(this);
        initData();
        //mViewPager.setOffscreenPageLimit(mFragments.size());

        topAppBar = (MaterialToolbar) view.findViewById(R.id.top_app_bar);
        topAppBar.setTitle("首页");

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mTabs.get(position).select();
                    //mViewPager.setCurrentItem(position);
                    //selectTab(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void selectTab(int i){
        mTabs.get(i).select();
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
    private void initData(){
        channelBeans = new ArrayList<ChannelBean>();
        channelBeans.add(new ChannelBean("BIOLOGY",true));
        channelBeans.add(new ChannelBean("CHEMISTRY",true));
        channelBeans.add(new ChannelBean("CHINESE",true));
        channelBeans.add(new ChannelBean("ENGLISH",false));
        channelBeans.add(new ChannelBean("GEOGRAPHY",false));
        channelBeans.add(new ChannelBean("HISTORY",false));
        channelBeans.add(new ChannelBean("MATHS",false));
        channelBeans.add(new ChannelBean("PHYSICS",false));
        channelBeans.add(new ChannelBean("POLITICS",false));
        for(int i=0;i<channelBeans.size();i++){
            if(channelBeans.get(i).isSelect()){
                mFragments.add(TabFragment.newInstance());
                mViewPagerAdapterForNav.setFragments(mFragments);
                addTab(channelBeans.get(i).getName());
            }
        }
    }

    @Override
    public void onClick(View V){
        switch (V.getId()){
            default:
                break;
            case R.id.imgBtn:
                ArrayList<String> mCategory= new ArrayList<>();
                ArrayList<String> mDelCategory= new ArrayList<>();
                Intent intent=new Intent((AppCompatActivity)getActivity(), CategoryArrangement.class);
                for(int i=0;i<channelBeans.size();i++){
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
                channelBeans=new ArrayList<ChannelBean>();
                for(int i=0;i<mCategory.size();i++){
                    channelBeans.add(new ChannelBean(mCategory.get(i),true));
                }
                for(int i=0;i<mDelCategory.size();i++){
                    channelBeans.add(new ChannelBean(mDelCategory.get(i),false));
                }
                tabLayout.removeAllTabs();
                mFragments.clear();
                int backStackCount= getFragmentManager().getBackStackEntryCount();
                for(int i=0;i<backStackCount;i++){
                    getFragmentManager().popBackStack();
                }
                for(int i=0;i<channelBeans.size();i++){
                    if(channelBeans.get(i).isSelect()){
                        mFragments.add(TabFragment.newInstance());
                        System.out.println(channelBeans.get(i).getName());
                        addTab(channelBeans.get(i).getName());
                    }
                }
                mViewPagerAdapterForNav.notifyDataSetChanged();
                //mViewPagerAdapterForNav=new ViewPagerAdapterForNav(getContext(),getChildFragmentManager(),mFragments);
                break;
            default:
                break;
        }
    }
}
