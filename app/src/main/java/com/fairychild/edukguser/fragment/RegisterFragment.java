package com.fairychild.edukguser.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fairychild.edukguser.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class RegisterFragment extends Fragment {
    public interface RegisterListener {
        void register(String username, String password);
    }
    private Button reg_Btn;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private RegisterListener listener;
    private TextView pwd_weak;
    private TextView pwd_in;
    private TextView pwd_strong;
    private boolean user_is_empty=true;

    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.register,getActivity().findViewById(R.id.Register_Layout));
        reg_Btn = view.findViewById(R.id.regBtn);
        etUsername = view.findViewById(R.id.register_username);
        etPassword = view.findViewById(R.id.register_password);
        pwd_weak = view.findViewById(R.id.pwd_weak);
        pwd_in = view.findViewById(R.id.pwd_in);
        pwd_strong = view.findViewById(R.id.pwd_strong);

        reg_Btn.setEnabled(false);

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0){
                    user_is_empty=true;
                    reg_Btn.setEnabled(false);
                }
                else{
                    user_is_empty=false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (Objects.requireNonNull(etUsername.getText()).toString().trim().length() == 0) {
                    reg_Btn.setEnabled(false);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String str = etPassword.getText().toString().trim();
                int length = str.length();
                if (length > 0) {
                    if (!isNumberLetter(str)) {
                        str = str.substring(0, length - 1);
                        etPassword.setText(str);
                        String str1 = etPassword.getText().toString().trim();
                        etPassword.setSelection(str1.length());
                        etPassword.setError("密码只能是字母和数字");
                        reg_Btn.setEnabled(false);
                    }
                }
                //输入框为0
                if (str.length() == 0)
                {
                    pwd_weak.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                if (str.length() > 14)
                {
                    pwd_weak.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的纯数字为弱
                if (str.matches ("^[0-9]+$"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的纯小写字母为弱
                else if (str.matches ("^[a-z]+$"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的纯大写字母为弱
                else if (str.matches ("^[A-Z]+$"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的大写字母和数字，输入的字符小于7个密码为弱
                else if (str.matches ("^[A-Z0-9]{1,5}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的大写字母和数字，输入的字符大于7个密码为中
                else if (str.matches ("^[A-Z0-9]{6,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的小写字母和数字，输入的字符小于7个密码为弱
                else if (str.matches ("^[a-z0-9]{1,5}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的小写字母和数字，输入的字符大于7个密码为中
                else if (str.matches ("^[a-z0-9]{6,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的大写字母和小写字母，输入的字符小于7个密码为弱
                else if (str.matches ("^[A-Za-z]{1,5}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的大写字母和小写字母，输入的字符大于7个密码为中
                else if (str.matches ("^[A-Za-z]{6,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的大写字母和小写字母和数字，输入的字符小于5个个密码为弱
                else if (str.matches ("^[A-Za-z0-9]{1,5}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的大写字母和小写字母和数字，输入的字符大于6个个密码为中
                else if (str.matches ("^[A-Za-z0-9]{6,7}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                    reg_Btn.setEnabled(false);
                }
                //输入的大写字母和小写字母和数字，输入的字符大于等于8个密码为强
                else if (str.matches ("^[A-Za-z0-9]{8,14}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(113,198,14));
                    reg_Btn.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
        });
        reg_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!user_is_empty){
                    listener.register(etUsername.getText().toString(), etPassword.getText().toString());
                }
            }
        });
        return view;
    }
    public static RegisterFragment newInstance(){
        RegisterFragment indexFragment=new RegisterFragment();
        return indexFragment;
    }
    @Override
    public void onAttach(Context context){
        listener = (RegisterListener) context;
        super.onAttach(context);
    }
}
