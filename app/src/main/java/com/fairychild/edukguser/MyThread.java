package com.fairychild.edukguser;

public class MyThread extends Thread{
    private String str;

    public MyThread(Runnable runnable) {
        super(runnable);
    }

    public void setStr(String s){
        str = s;
    }
    public String getStr(){
        return str;
    }
}
