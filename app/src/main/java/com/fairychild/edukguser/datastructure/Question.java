package com.fairychild.edukguser.datastructure;

public class Question {
    private String title;
    private String choiceA,choiceB,choiceC,choiceD;
    private int correct;
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
    boolean isCorrect(int pos){
        return pos == correct;
    }
}
