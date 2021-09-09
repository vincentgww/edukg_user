package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.R;

public class LoginFragment extends Fragment {
    public interface LoginListener {
        void login(String username, String password);
    }
    private Button btnSignIn;
    private EditText etUsername;
    private EditText etPassword;
    private LoginListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_login,getActivity().findViewById(R.id.login_layout));
            btnSignIn = view.findViewById(R.id.btn_sign_in);
            etUsername = view.findViewById(R.id.login_username);
            etPassword = view.findViewById(R.id.login_password);
            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.login(etUsername.getText().toString(), etPassword.getText().toString());
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
