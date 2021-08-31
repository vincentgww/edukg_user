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

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MeFragment extends Fragment {
    public interface FragmentListener {
        void switchToLogin();
        void switchToRegister();
    }
    private Button btnLogin;
    private FragmentListener listener;
    private Button btnRegister;

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
                // 跳转到登录界面
                listener.switchToLogin();
            }
        });
        if(btnLogin==null)
            System.out.println("?");
        btnRegister=view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转至注册界面
                listener.switchToRegister();
            }
        });

        return view;
    }
}
