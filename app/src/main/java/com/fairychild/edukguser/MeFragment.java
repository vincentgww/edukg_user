package com.fairychild.edukguser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MeFragment extends Fragment {
    public interface FragmentListener {
        void switchToLogin();
        void switchToBrowsingHistory();
        void switchToFavourites();
        void switchToReport();
    }
    private Button btnLogin;
    private Button btnBrowsingHistory;
    private Button btnFavourites;
    private Button btnReport;
    private FragmentListener listener;

    private MaterialToolbar topAppBar;

    public static MeFragment newInstance(){
        MeFragment indexFragment = new MeFragment();
        return indexFragment;
    }

    @Override
    public void onAttach(Context context) {
        listener = (FragmentListener) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me,container,false);

        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到登录页面
                listener.switchToLogin();
            }
        });
        if(btnLogin==null)
            System.out.println("?btnLogin");

        btnBrowsingHistory = view.findViewById(R.id.btn_browsing_history);
        btnBrowsingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到浏览历史
                listener.switchToBrowsingHistory();
            }
        });
        if(btnBrowsingHistory==null)
            System.out.println("?btnBrowsingHistory");

        btnFavourites = view.findViewById(R.id.btn_favourites);
        btnFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到我的收藏
                listener.switchToFavourites();
            }
        });
        if(btnFavourites==null)
            System.out.println("?btnFavourites");

        btnReport = view.findViewById(R.id.btn_report);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到学习周报
                listener.switchToReport();
            }
        });
        if(btnReport==null)
            System.out.println("?btnReport");

        topAppBar = (MaterialToolbar) view.findViewById(R.id.top_app_bar);
        topAppBar.setTitle("个人中心");

        return view;
    }
}
