package com.fairychild.edukguser.datastructure;

public class outlineItem {
    String sub,verb,obj,property;
    public outlineItem(String sub,String verb,String obj){
        this.sub = sub;
        this.verb = verb;
        this.obj = obj;
        this.property = null;
    }
    public void setProperty(String property){
        this.property = property;
    }
    public String getProperty() {
        return property;
    }
    public void setSub(String sub){
        this.sub = sub;
    }
    public String getSub(){
        return sub;
    }
    public void setVerb(String verb){
        this.verb = verb;
    }
    public String getVerb(){
        return verb;
    }
    public void setObj(String obj){
        this.obj = obj;
    }
    public String getObj(){
        return obj;
    }
}
