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

public class TabFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //setContentView(R.layout.fragment_login);
        View view = inflater.inflate(R.layout.fragment_tab,getActivity().findViewById(R.id.login_layout));
        return view;
    }

    public static TabFragment newInstance(){
        TabFragment indexFragment = new TabFragment();
        return indexFragment;
    }

}
