package com.fairychild.edukguser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.R;
import com.fairychild.edukguser.datastructure.Question;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

public class QuizFragment extends Fragment {
    String name;
    int idx;
    List<Question> question_list = new ArrayList<>();
    private void handle_quiz (String response){
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr = new JSONArray(obj.getString("data"));
            for(int i=0;i<arr.length();i++){
                obj = arr.getJSONObject(i);
                System.out.println(obj);
                String raw = obj.getString("qBody");
                String ans = obj.getString("qAnswer");
                int correct=-1;
                switch (ans){
                    case "A":
                        correct = 0;
                        break;
                    case "B":
                        correct = 1;
                        break;
                    case "C":
                        correct = 2;
                        break;
                    case "D":
                        correct = 3;
                }
                String[] blocks = raw.split("[A-D].");
                Question q = new Question(blocks[0],blocks[1],blocks[2],blocks[3],blocks[4],correct);
                question_list.add(q);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public QuizFragment(String name,int idx, String response){
        this.name = name;
        this.idx = idx;
        handle_quiz(response);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.fragment_quiz,container,false);
        return view;
    }
    public static QuizFragment newInstance(String name,int idx,String response){
        QuizFragment indexFragment=new QuizFragment(name,idx,response);
        return indexFragment;
    }
}
