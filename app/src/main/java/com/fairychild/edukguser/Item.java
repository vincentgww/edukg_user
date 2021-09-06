package com.fairychild.edukguser;

public class Item{
    private boolean subject;
    private String label;
    private String des;
    public Item(String l,String d,boolean s){
        label = l;
        des = d;
        subject = s;
    }
    public String get_label(){
        return label;
    }
    public String get_des(){
        return des;
    }
    public boolean get_sub(){
        return subject;
    }

}
