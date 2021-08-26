package com.fairychild.edukguser;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    public interface LoginListener {
        void check(EditText phone, EditText password);
    }
    private Button btnSignIn;
    private EditText etPhone;
    private EditText etPassword;
    private LoginListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //setContentView(R.layout.fragment_login);
            View view = inflater.inflate(R.layout.fragment_login,getActivity().findViewById(R.id.login_layout));
            btnSignIn = view.findViewById(R.id.btn_sign_in);
            etPhone = view.findViewById(R.id.login_phone);
            etPassword = view.findViewById(R.id.login_password);
            btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.check(etPhone,etPassword);
            }
        });
            return view;
    }

    public static LoginFragment newInstance(){
        LoginFragment indexFragment = new LoginFragment();
        return indexFragment;
    }

    @Override
    public void onAttach(Context context) {
        listener = (LoginListener) context;
        super.onAttach(context);
    }
}
