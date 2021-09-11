package com.fairychild.edukguser.datastructure;

import java.util.Objects;

public class Question {

    private String label;
    private Integer id;
    private String title;
    private String choiceA,choiceB,choiceC,choiceD;
    private int correct;
    private int usr_ans = -1;

    public Question(String label, Integer id, String title, String choiceA,
                    String choiceB, String choiceC, String choiceD, int correct){
        this.label = label;
        this.id = id;
        this.title = title;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.correct = correct;
    }

    public String getLabel() {
        return label;
    }
    public Integer getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Question{" +
                "label='" + label + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", choiceA='" + choiceA + '\'' +
                ", choiceB='" + choiceB + '\'' +
                ", choiceC='" + choiceC + '\'' +
                ", choiceD='" + choiceD + '\'' +
                ", correct=" + correct +
                ", usr_ans=" + usr_ans +
                '}';
    }
}
