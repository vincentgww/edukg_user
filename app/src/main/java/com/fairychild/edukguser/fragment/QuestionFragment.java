package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fairychild.edukguser.ItemAdapter;
import com.fairychild.edukguser.QuestionAdapter;
import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Question;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

public class QuestionFragment extends Fragment {
    String name;
    int idx;
    int back_id;
    int id = 0;
    int usr;
    private Context mActivity;
    private QuizFragment.quizListener listener;
    private RecyclerView quizRecyclerView;
    private QuestionAdapter adapter;
    private TextView title;
    private RadioButton choiceA,choiceB,choiceC,choiceD;
    private Button last_question,next_question,submit;
    private RadioGroup rgroup;
    private detailFragment.detailListener mlistener;
    List<Question> question_list = new ArrayList<>();
    private Button question_shareBtn;
    @Override
    public void onAttach(Context context) {
        listener = (QuizFragment.quizListener) context;
        mlistener = (detailFragment.detailListener) context;
        mActivity = context;
        super.onAttach(context);
    }


    public QuestionFragment(String name,int idx, List<Question> questionList, int back_id){
        this.name = name;
        this.idx = idx;
        question_list = questionList;
        this.back_id = back_id;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.fragment_question,container,false);
        mActivity = getActivity();
        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new
                        LinearLayoutManager(getActivity());
                quizRecyclerView = view.findViewById(R.id.quiz_recycler);
                quizRecyclerView.setLayoutManager(layoutManager);
                adapter = new QuestionAdapter(question_list);
                quizRecyclerView.setAdapter(adapter);
                //adapter.notifyItemInserted(question_list.size()-1);
                View footer = LayoutInflater.from(getContext()).inflate(R.layout.submit_ans, quizRecyclerView, false);
                adapter.setFooterView(footer);
            }
        });*/
        title = view.findViewById(R.id.question);
        choiceA = view.findViewById(R.id.q_choice1);
        choiceB = view.findViewById(R.id.q_choice2);
        choiceC = view.findViewById(R.id.q_choice3);
        choiceD = view.findViewById(R.id.q_choice4);
        last_question = view.findViewById(R.id.last_question);
        next_question = view.findViewById(R.id.next_question);
        Button back_button = view.findViewById(R.id.back_button_quiz);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.go_back(back_id);
                listener.delete_quiz(idx);
            }
        });
        last_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --id;
                show_question();
            }
        });
        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++id;
                show_question();
            }
        });
        submit = view.findViewById(R.id.submit_question);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usr!=0)
                    choiceA.setEnabled(false);
                if(usr!=1)
                    choiceB.setEnabled(false);
                if(usr!=2)
                    choiceC.setEnabled(false);
                if(usr!=3)
                    choiceD.setEnabled(false);
                int correct = question_list.get(id).getAns();
                System.out.println(usr);
                switch(correct){
                    case 0:
                        choiceA.setButtonDrawable(R.drawable.cb_right);
                        break;
                    case 1:
                        choiceB.setButtonDrawable(R.drawable.cb_right);
                        break;
                    case 2:
                        choiceC.setButtonDrawable(R.drawable.cb_right);
                        break;
                    case 3:
                        choiceD.setButtonDrawable(R.drawable.cb_right);
                        break;
                }
                submit.setVisibility(View.INVISIBLE);
            }
        });
        question_shareBtn=view.findViewById(R.id.question_share_btn);
        question_shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question_name=name;
                String question_content="知识点："+name+"\n";
                question_content+="题目："+"\n";
                question_content+=title.getText().toString()+"\n";
                question_content+=choiceA.getText().toString()+"\n"+choiceB.getText().toString()+"\n"+choiceC.getText().toString()+"\n"+choiceD.getText().toString()+"\n";
                int correct=question_list.get(id).getAns();
                switch (correct){
                    case 0:
                        question_content+="答案："+"A";
                        break;
                    case 1:
                        question_content+="答案："+"B";
                        break;
                    case 2:
                        question_content+="答案："+"C";
                        break;
                    case 3:
                        question_content+="答案："+"D";
                        break;
                }
                mlistener.weibo_share(question_name,question_content);
            }
        });
        rgroup = view.findViewById(R.id.rg_question);
        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println(checkedId);
                switch (checkedId){
                    case 2131296784:
                        usr = 0;
                        //choiceA.setChecked(true);
                        break;
                    case 2131296785:
                        usr = 1;
                        //choiceB.setChecked(true);
                        break;
                    case 2131296786:
                        usr = 2;
                        //choiceC.setChecked(true);
                        break;
                    case 2131296787:
                        usr = 3;
                        //choiceD.setChecked(true);
                        break;
                }
            }
        });
        show_question();
        return view;
    }
        public static QuestionFragment newInstance(String name,int idx,List<Question> questionList, int back_id){
            QuestionFragment indexFragment=new QuestionFragment(name,idx,questionList, back_id);
        return indexFragment;
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void show_question(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                usr = -1;
                choiceA.setEnabled(true);
                choiceB.setEnabled(true);
                choiceC.setEnabled(true);
                choiceD.setEnabled(true);
                choiceA.setButtonDrawable(R.drawable.cb);
                choiceB.setButtonDrawable(R.drawable.cb);
                choiceC.setButtonDrawable(R.drawable.cb);
                choiceD.setButtonDrawable(R.drawable.cb);
                rgroup.setEnabled(true);
                rgroup.clearCheck();
                choiceA.setChecked(false);
                choiceB.setChecked(false);
                choiceC.setChecked(false);
                choiceD.setChecked(false);
                submit.setVisibility(View.VISIBLE);
                Question que = question_list.get(id);
                if(id == 0)
                    last_question.setVisibility(View.INVISIBLE);
                else
                    last_question.setVisibility(View.VISIBLE);
                if(id == question_list.size()-1)
                    next_question.setVisibility(View.INVISIBLE);
                else
                    next_question.setVisibility(View.VISIBLE);
                String t = "" + (id + 1) + "、" + que.getTitle();
                title.setText(t);
                choiceA.setText("A."+que.getChoiceA());
                choiceB.setText("B."+que.getChoiceB());
                choiceC.setText("C."+que.getChoiceC());
                choiceD.setText("D."+que.getChoiceD());
            }
        });
    }
}
