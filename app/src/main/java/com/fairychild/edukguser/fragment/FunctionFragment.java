package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.R;
import com.google.android.material.card.MaterialCardView;

public class FunctionFragment extends Fragment {
    public interface FunctionListener {
        void switchToKnowledgeCheck();
        void switchToSearch();
        void show_quiz(String label,String course);
        void show_exam() throws InterruptedException;
    }
    private TextView title,text;
    private LinearLayout linear;
    private Spinner quizSpinner;
    private EditText quizInput;
    private Button quizButton;
    private FunctionListener listener;
    private String course;
    private ImageView quiz_test;
    private ImageView rec_exam;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_fragment, container, false);
        ImageView  knowledge_check= view.findViewById(R.id.knowledge_check_img);
        title = view.findViewById(R.id.quiz_title);
        text = view.findViewById(R.id.quiz_text);
        linear = view.findViewById(R.id.quiz_linear);
        linear.setVisibility(View.GONE);
        quizButton = view.findViewById(R.id.quiz_button);
        quizInput = view.findViewById(R.id.quiz_input);
        quizSpinner = view.findViewById(R.id.quiz_spinner);
        quizSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                course = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                course = null;
            }
        });
        quizButton.setVisibility(View.GONE);
        quizButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        listener.show_quiz(quizInput.getText().toString(),course);
                    }
                }
        );
        knowledge_check.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        listener.switchToKnowledgeCheck();
                    }
                }
        );

        quiz_test = view.findViewById(R.id.quiz_test_img);
        quiz_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quizButton.setVisibility(View.VISIBLE);
                linear.setVisibility(View.VISIBLE);
                title.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }
        });

        rec_exam = view.findViewById(R.id.quiz_exam_img);
        rec_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    listener.show_exam();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onHiddenChanged(boolean hidd) {
        if (hidd) {
            quizButton.setVisibility(View.GONE);
            linear.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
        } else {
            //显示时所作的事情

        }
    }

}
