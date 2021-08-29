package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.R;
import com.google.android.material.appbar.MaterialToolbar;

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
    private String id;
    private String phone;

    private MaterialToolbar topAppBar;

    public static MeFragment newInstance(Bundle args){
        MeFragment fragment = new MeFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        listener = (FragmentListener) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        id = null;
        phone = null;
        if (getArguments() != null) {
            id = getArguments().getString("id", null);
            phone = getArguments().getString("phone", null);
        }

        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到登录页面
                listener.switchToLogin();
            }
        });
        if (id != null && phone != null) {
            btnLogin.setText(phone);
            btnLogin.setEnabled(false);
        } else {
            btnLogin.setText(R.string.login);
            btnLogin.setEnabled(true);
        }
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
    }
}
