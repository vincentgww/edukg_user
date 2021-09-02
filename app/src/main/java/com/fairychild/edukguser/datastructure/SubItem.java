package com.fairychild.edukguser.datastructure;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class SubItem implements Serializable {
    @NonNull
    @PrimaryKey
    private String label;
    private String course;
    private String description;

    public SubItem(final String label,final String course,final String description){
        this.label=label;
        this.course=course;
        this.description=description;
    }
    public String getLabel(){
        return label;
    }
    public String getCourse(){
        return course;
    }
    public String getDescription(){
        return description;
    }
    public void setLabel(String label){
        this.label=label;
    }
    public void setCourse(String course){
        this.course=course;
    }
    public void setDescription(String description){
        this.description=description;
    }

}
