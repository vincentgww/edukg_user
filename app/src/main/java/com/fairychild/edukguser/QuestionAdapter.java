package com.fairychild.edukguser;
import com.fairychild.edukguser.Msg;
import com.fairychild.edukguser.datastructure.Question;
import com.google.android.material.internal.CheckableImageButton;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    public interface myListener {
        void addQuestion(Question question);
    }

    private QuestionAdapter.myListener listener;

    private List<Question> mQuestionList;
    private List<Integer> usr_ans = new ArrayList<>();
    private View mFooterView;
    private int score=0;
    private boolean submit;
    private final int score_per_q = 5;
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }
    private Handler handler = new Handler(Looper.myLooper()){
        //获取当前进程的Looper对象传给handler
        @Override
        public void handleMessage(Message message){
            int idx = message.getData().getInt("idx");
            int value = message.getData().getInt("value");
            usr_ans.set(idx,value);
        }
    };
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        //ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
        View view;
        if(viewType == -1)
            view = mFooterView;
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_quiz,parent,false);
        //LayoutInflat.from()从一个Context中，获得一个布局填充器，这样你就可以使用这个填充器来把xml布局文件转为View对象了。
        //LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);这样的方法来加载布局msg_item.xml
        return new ViewHolder(view);
    }
    public void cal_score(){
        for(int i=0;i<usr_ans.size();i++){
            if(usr_ans.get(i) == mQuestionList.get(i).getAns())
                score += score_per_q;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){
        if(getItemViewType(position)!=-1) {

            Question que = mQuestionList.get(holder.getAbsoluteAdapterPosition());
            holder.radioGroup.clearCheck();
            holder.radioGroup.setEnabled(true);
            String title = "" + (position + 1) + "、" + que.getTitle();
            holder.quiz_title.setText(title);
            holder.choiceA.setText("A."+que.getChoiceA());
            holder.choiceB.setText("B."+que.getChoiceB());
            holder.choiceC.setText("C."+que.getChoiceC());
            holder.choiceD.setText("D."+que.getChoiceD());
            int usr = usr_ans.get(position);
            switch(usr){
                case 0:
                    holder.choiceA.setChecked(true);
                    break;
                case 1:
                    holder.choiceB.setChecked(true);
                    break;
                case 2:
                    holder.choiceC.setChecked(true);
                    break;
                case 3:
                    holder.choiceD.setChecked(true);
                    break;
                case -1:
                    break;
            }
            if(submit){
                //System.out.println("这是第多少个问题？"+position);
                //System.out.println("正确答案是？"+que.getAns());
                if(usr!=0)
                    holder.choiceA.setEnabled(false);
                if(usr!=1)
                    holder.choiceB.setEnabled(false);
                if(usr!=2)
                    holder.choiceC.setEnabled(false);
                if(usr!=3)
                    holder.choiceD.setEnabled(false);
                int correct = que.getAns();
                switch(correct){
                    case 0:
                        holder.choiceA.setButtonDrawable(R.drawable.cb_right);
                        break;
                    case 1:
                        holder.choiceB.setButtonDrawable(R.drawable.cb_right);
                        break;
                    case 2:
                        holder.choiceC.setButtonDrawable(R.drawable.cb_right);
                        break;
                    case 3:
                        holder.choiceD.setButtonDrawable(R.drawable.cb_right);
                        break;
                }
                holder.radioGroup.setEnabled(false);
            }
            else{
                holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int id = -1;
                        int count = holder.radioGroup.getChildCount();
                        for(int i = 0 ;i < count;i++){
                            RadioButton rb = (RadioButton)holder.radioGroup.getChildAt(i);
                            if(rb.isChecked()){
                                id = i;
                                break;
                            }
                        }
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("idx", holder.getAbsoluteAdapterPosition());
                        bundle.putInt("value", id);  //往Bundle中存放数据
                        message.setData(bundle);//mes利用Bundle传递数据
                        handler.sendMessage(message);
                    }
                });
            }

        }
        else{
            if(submit){
                holder.btn.setText("您的得分为"+score+"/"+mQuestionList.size()*score_per_q);
            }
            else {
                holder.btn.setText("提交答案");
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit = true;
                        score = 0;
                        cal_score();
                        for (int i = 0; i < mQuestionList.size(); i++) {
                            mQuestionList.get(i).set_usr_ans(usr_ans.get(i));
                            listener.addQuestion(mQuestionList.get(i));
                        }
                        //notifyItemRangeChanged(0, mQuestionList.size());
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
    @Override
    public int getItemCount(){
        if(mFooterView==null)
            return mQuestionList.size();
        return mQuestionList.size()+1;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView quiz_title;
        RadioButton choiceA;
        RadioButton choiceB;
        RadioButton choiceC;
        RadioButton choiceD;
        RadioGroup radioGroup;
        //TextView ans;
        Button btn;
        public ViewHolder(@NonNull View view){
            super(view);
            quiz_title = view.findViewById(R.id.tv_que);
            radioGroup = view.findViewById(R.id.rg);
            choiceA = view.findViewById(R.id.cb_choice1);
            choiceB = view.findViewById(R.id.cb_choice2);
            choiceC = view.findViewById(R.id.cb_choice3);
            choiceD = view.findViewById(R.id.cb_choice4);
            btn = view.findViewById(R.id.loadBtn);
            //ans = view.findViewById(R.id.tv_answer);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(mFooterView == null)
            return position;
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return -1;
        }
        return position;
    }


    public QuestionAdapter (Context context, List<Question> questionList){
        listener = (myListener) context;
        mQuestionList = questionList;
        for(int i=0;i<mQuestionList.size();i++){
            usr_ans.add(-1);
        }
    }
}

