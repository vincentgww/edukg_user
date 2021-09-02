package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.R;
import com.google.android.material.card.MaterialCardView;

public class FunctionFragment extends Fragment {
    public interface FunctionListener {
        void knowledgeCheck();
        void quizSearch();
    }
    private FunctionListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_fragment, container, false);
        ImageView  knowledge_check= view.findViewById(R.id.knowledge_check_img);
        knowledge_check.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        listener.knowledgeCheck();
                    }
                }
        );
        MaterialCardView card=view.findViewById(R.id.test_card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.quizSearch();
            }
        });
        return view;
    }

    public static FunctionFragment newInstance(){
        FunctionFragment indexFragment = new FunctionFragment();
        return indexFragment;
    }

    @Override
    public void onAttach(Context context) {
        listener = (FunctionListener) context;
        super.onAttach(context);
    }

}