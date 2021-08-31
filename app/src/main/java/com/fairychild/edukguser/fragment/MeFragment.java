package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
        void switchToRegister();
        String getIdFromSP();
        String getPhoneFromSP();
    }

    private Button btnLogin;
    private Button btnBrowsingHistory;
    private Button btnFavourites;
    private Button btnReport;
    private Button btnRegister;

    private FragmentListener listener;

    private MaterialToolbar topAppBar;

    private String id;
    private String phone;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    public static MeFragment newInstance(){
        return new MeFragment();
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

        id = listener.getIdFromSP();
        phone = listener.getPhoneFromSP();

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

        btnRegister=view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转至注册界面
                listener.switchToRegister();
            }
        });

        topAppBar = (MaterialToolbar) view.findViewById(R.id.top_app_bar);
        topAppBar.setTitle("个人中心");
    }
}
