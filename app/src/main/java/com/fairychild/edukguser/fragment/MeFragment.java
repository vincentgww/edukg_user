package com.fairychild.edukguser.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.MessageEvent;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.LoginNotice;
import com.fairychild.edukguser.datastructure.LogoutNotice;
import com.google.android.material.appbar.MaterialToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MeFragment extends Fragment {
    public interface FragmentListener {
        void switchToLogin();
        void switchToBrowsingHistory();
        void switchToFavourites();
        void switchToReport();
        void switchToRegister();
        String getIdFromSP();
        String getUsernameFromSP();
        void logout();
    }

    private Button btnLogin;
    private Button btnRegister;
    private Button btnLogout;
    private Button btnBrowsingHistory;
    private Button btnFavourites;
    private Button btnReport;

    private FragmentListener listener;

    private MaterialToolbar topAppBar;

    private String id;
    private String username;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        id = listener.getIdFromSP();
        username = listener.getUsernameFromSP();

        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到登录页面
                listener.switchToLogin();
            }
        });
        if (id != null && username != null) {
            btnLogin.setText("你好!" + username);
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

        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listener.logout();
            }
        });

        topAppBar = (MaterialToolbar) view.findViewById(R.id.top_app_bar);
        topAppBar.setTitle("个人中心");
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginNotice notice) {
        Log.d("MeFragment", "onMessageEvent LoginNotice" + notice.getMessage());
        try {
            username = notice.getMessage();
            btnLogin.setEnabled(false);
            btnLogin.setText("你好！" + username);

            btnRegister.setVisibility(View.GONE);

            btnLogout.setVisibility(View.VISIBLE);

            EventBus.getDefault().removeStickyEvent(notice);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutNotice notice) {
        Log.d("MeFragment", "onMessageEvent LogoutNotice");
        try {
            btnLogin.setEnabled(true);
            btnLogin.setText("登录");

            btnRegister.setVisibility(View.VISIBLE);

            btnLogout.setVisibility(View.GONE);

            EventBus.getDefault().removeStickyEvent(notice);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
