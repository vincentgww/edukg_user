package com.fairychild.edukguser.datastructure;

public class Question {
    private String title;
    private String choiceA,choiceB,choiceC,choiceD;
    private int correct;
    private int usr_ans = -1;
    public Question(String title,String choiceA,String choiceB,String choiceC,String choiceD,int correct){
        this.title = title;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.correct = correct;
    }
    public String getTitle(){
        return title;
    }
    public String getChoiceA(){
        return choiceA;
    }
    public String getChoiceB(){
        return choiceB;
    }
    public String getChoiceC(){
        return choiceC;
    }
    public String getChoiceD(){
        return choiceD;
    }
    public int getAns(){
        return correct;
    }
    public void set_usr_ans (int u){
        usr_ans = u;
    }
    public int get_usr_ans(){
        return usr_ans;
    }
}
