package com.fairychild.edukguser.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fairychild.edukguser.MessageEvent;
import com.fairychild.edukguser.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

public class KnowledgeCheckFragment extends Fragment {
    public interface KnowledgeCheckListener {
        void search_point(String s,String course);
        void switchToFunction();
    }
    private KnowledgeCheckListener listener;
    private TextView ansTxt;
    private Spinner subjectSpinner;
    private String subject;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knowledgecheck, container, false);
        Button btn = view.findViewById(R.id.btn_knowledge_check);
        EditText txt = view.findViewById(R.id.search_point_input);
        ansTxt = view.findViewById(R.id.search_point_txt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = txt.getText().toString();
                listener.search_point(s,subject);
            }
        });
        Button backBtn = view.findViewById(R.id.back_knowledge_button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.switchToFunction();
            }
        });
        subjectSpinner = view.findViewById(R.id.subject_spinner);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subject = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                subject = null;
            }
        });

        return view;
    }


    public static KnowledgeCheckFragment newInstance(){
        KnowledgeCheckFragment indexFragment = new KnowledgeCheckFragment();
        return indexFragment;
    }

    @Override
    public void onAttach(Context context) {
        listener = (KnowledgeCheckListener) context;
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        System.out.println(event.message);
        String ans = "";
        try {
            JSONObject obj = new JSONObject(event.message);
            //JSONArray arr = new JSONArray(obj.getString("data"));
            JSONObject obj2 = new JSONObject(obj.getString("data"));
            JSONArray arr = obj2.getJSONArray("results");
            for(int i=0;i<arr.length();i++){
                obj =  arr.getJSONObject(i);
                ans += obj.getString("entity") + " ";
            }
            if(ans.equals(""))
                ans = "对不起，没有找到相关的实体";
            ansTxt.setText(ans);
            ansTxt.setTextColor(Color.BLACK);
            ansTxt.setTextSize(18);
            ansTxt.setMovementMethod(ScrollingMovementMethod.getInstance());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
