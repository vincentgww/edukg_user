package com.fairychild.edukguser.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.CircleImageViewDrawable;
import com.fairychild.edukguser.MessageEvent;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.LoginNotice;
import com.fairychild.edukguser.datastructure.LogoutNotice;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MeFragment extends Fragment {
    public interface FragmentListener {
        void switchToLogin();
        void switchToBrowsingHistory() throws InterruptedException;
        void switchToFavourites() throws InterruptedException;
        void switchToLocalCache();
        void switchToReport();
        void switchToRegister();
        String getIdFromSP();
        String getUsernameFromSP();
        void logout();
    }

    private ImageView mImageview;

    private Button btnLogin;
    private Button btnRegister;
    private Button btnLogout;
    private MaterialCardView cardBrowsingHistory;
    private MaterialCardView cardFavourites;
    private MaterialCardView cardReport;
    private MaterialCardView cardLocalCache;

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

        mImageview=view.findViewById(R.id.profile_img);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head_img);
        Drawable drawable = new CircleImageViewDrawable(bitmap);
        mImageview.setImageDrawable(drawable);

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
        if (btnLogin==null)
            System.out.println("?btnLogin");

        cardBrowsingHistory = view.findViewById(R.id.his_card);
        cardBrowsingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到浏览历史
                try {
                    listener.switchToBrowsingHistory();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        if (cardBrowsingHistory==null)
            System.out.println("?cardBrowsingHistory");

        cardFavourites = view.findViewById(R.id.fav_card);
        cardFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到我的收藏
                try {
                    listener.switchToFavourites();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        if (cardFavourites==null)
            System.out.println("?cardFavourites");

        cardReport = view.findViewById(R.id.report_card);
        cardReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到学习周报
                listener.switchToReport();
            }
        });
        if (cardReport==null)
            System.out.println("?cardReport");

        cardLocalCache = view.findViewById(R.id.local_cache_card);
        cardLocalCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到本地缓存
                listener.switchToLocalCache();
            }
        });
        if (cardLocalCache == null)
            System.out.println("?cardLocalCache");

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
