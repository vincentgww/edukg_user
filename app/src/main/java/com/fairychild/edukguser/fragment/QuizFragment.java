package com.fairychild.edukguser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.R;

import org.jetbrains.annotations.Nullable;

public class QuizFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_quizsearch,getActivity().findViewById(R.id.quizsearchLayout));
        return view;
    }
    public static QuizFragment newInstance(){
        QuizFragment indexFragment=new QuizFragment();
        return indexFragment;
    }
}
