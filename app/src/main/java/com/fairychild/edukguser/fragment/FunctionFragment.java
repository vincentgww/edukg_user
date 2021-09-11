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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.R;
import com.google.android.material.card.MaterialCardView;

public class FunctionFragment extends Fragment {
    public interface FunctionListener {
        void switchToKnowledgeCheck();
        void switchToSearch();
        void switchToOutline(String course,String label);
        void show_quiz(String label,String course);
        void show_exam() throws InterruptedException;
    }
    private TextView title,text,outlineTitle,outlineText;
    private LinearLayout linear,outlineLinear;
    private Spinner quizSpinner,outlineSpinner;
    private EditText quizInput,outlineInput;
    private Button quizButton,outlineButton;
    private FunctionListener listener;
    private String course;
    private ImageView quiz_test;
    private ImageView rec_exam;

    public static class Utils {
        private static long lastClickTime;
        public static boolean isFastDoubleClick() {
            long time = System.currentTimeMillis();
            if ( time - lastClickTime < 5000) {
                return true;
            }
            lastClickTime = time;
            return false;
        }
    }

    private String outlineCourse;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.function_fragment, container, false);
        ImageView  knowledge_check= view.findViewById(R.id.knowledge_check_img);
        ImageView  outline= view.findViewById(R.id.outline_img);
        title = view.findViewById(R.id.quiz_title);
        text = view.findViewById(R.id.quiz_text);
        linear = view.findViewById(R.id.quiz_linear);
        linear.setVisibility(View.GONE);
        quizButton = view.findViewById(R.id.quiz_button);
        quizInput = view.findViewById(R.id.quiz_input);
        quizSpinner = view.findViewById(R.id.quiz_spinner);
        outlineTitle = view.findViewById(R.id.outline_title);
        outlineText = view.findViewById(R.id.outline_text);
        outlineLinear = view.findViewById(R.id.outline_linear);
        outlineLinear.setVisibility(View.GONE);
        outlineButton = view.findViewById(R.id.outline_button);
        outlineInput = view.findViewById(R.id.outline_input);
        outlineSpinner = view.findViewById(R.id.outline_spinner);
        outlineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                outlineCourse = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                course = null;
            }
        });
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
        outlineButton.setVisibility(View.GONE);
        outlineButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        listener.switchToOutline(outlineCourse,outlineInput.getText().toString());
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
                if (Utils.isFastDoubleClick()){
                    return;
                }
                try {
                    listener.show_exam();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView outline_recommendation=view.findViewById(R.id.outline_img);
        outline_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outlineButton.setVisibility(View.VISIBLE);
                outlineLinear.setVisibility(View.VISIBLE);
                outlineTitle.setVisibility(View.GONE);
                outlineText.setVisibility(View.GONE);
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
            outlineButton.setVisibility(View.GONE);
            outlineLinear.setVisibility(View.GONE);
            outlineTitle.setVisibility(View.VISIBLE);
            outlineText.setVisibility(View.VISIBLE);
        } else {
            //显示时所作的事情

        }
    }

}
