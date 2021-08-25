package com.fairychild.edukguser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {
    public static HomeFragment newInstance(){
        HomeFragment indexFragment = new HomeFragment();
        return indexFragment;
    }
    MeFragment.FragmentListener listener;

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
        Activity mActivity=getActivity();
//        TabLayout layout=mActivity.findViewById(R.id.tab_layout);
//        layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        return view;
    }
}
