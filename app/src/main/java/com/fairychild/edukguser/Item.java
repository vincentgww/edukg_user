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
    String get_label(){
        return label;
    }
    String get_des(){
        return des;
    }
    boolean get_sub(){
        return subject;
    }

}
