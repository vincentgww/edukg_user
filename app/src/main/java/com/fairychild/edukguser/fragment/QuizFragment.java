package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

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

public class QuizFragment extends Fragment {
    public interface quizListener {
        void delete_quiz(int idx);
        void go_back(int back_id);
        void addQuestion(Question question);
    }
    String name;
    int idx;
    int back_id;
    private Context mActivity;
    private quizListener listener;
    private RecyclerView quizRecyclerView;
    private QuestionAdapter adapter;
    List<Question> question_list = new ArrayList<>();
    @Override
    public void onAttach(Context context) {
        listener = (quizListener) context;
        mActivity = context;
        super.onAttach(context);
    }

    public QuizFragment(String name,int idx, List<Question> questionList, int back_id){
        this.name = name;
        this.idx = idx;
        question_list = questionList;
        this.back_id = back_id;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.fragment_quiz,container,false);
        mActivity = getActivity();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new
                        LinearLayoutManager(getActivity());
                quizRecyclerView = view.findViewById(R.id.quiz_recycler);
                quizRecyclerView.setLayoutManager(layoutManager);
                adapter = new QuestionAdapter(getActivity(), question_list);
                quizRecyclerView.setAdapter(adapter);
                //adapter.notifyItemInserted(question_list.size()-1);
                View footer = LayoutInflater.from(getContext()).inflate(R.layout.submit_ans, quizRecyclerView, false);
                adapter.setFooterView(footer);
            }
        });
        Button back_button = view.findViewById(R.id.back_button_quiz);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.go_back(back_id);
            }
        });
        return view;
    }
    public static QuizFragment newInstance(String name,int idx,List<Question> questionList, int back_id){
        QuizFragment indexFragment=new QuizFragment(name,idx,questionList, back_id);
        return indexFragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden)
            listener.delete_quiz(idx);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        //listener.delete_quiz(idx);
        super.onStop();
    }
}
