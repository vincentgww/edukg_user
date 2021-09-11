package com.fairychild.edukguser.datastructure;

public class Question {

    private String course;
    private String label;
    private Integer number;
    private String title;
    private String choiceA,choiceB,choiceC,choiceD;
    private int correct;
    private int usr_ans = -1;

    public Question(String course, String label, Integer number, String title, String choiceA,
                    String choiceB, String choiceC, String choiceD, int correct){
        this.course = course;
        this.label = label;
        this.number = number;
        this.title = title;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.correct = correct;
    }

    public String getCourse() {
        return course;
    }
    public String getLabel() {
        return label;
    }
    public Integer getNumber() {
        return number;
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
    public boolean ansIsRight() {return usr_ans == correct;}
}
