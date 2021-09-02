package com.fairychild.edukguser.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fairychild.edukguser.R;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {
    public interface RegisterListener {
        void register(String username, String password);
    }
    private Button reg_Btn;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private RegisterListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.register,getActivity().findViewById(R.id.Register_Layout));
        reg_Btn=view.findViewById(R.id.regBtn);
        etUsername=view.findViewById(R.id.register_username);
        etPassword=view.findViewById(R.id.register_password);
        reg_Btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listener.register(etUsername.getText().toString(),etPassword.getText().toString());
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
