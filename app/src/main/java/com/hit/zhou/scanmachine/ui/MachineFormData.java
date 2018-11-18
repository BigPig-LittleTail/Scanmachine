package com.hit.zhou.scanmachine.ui;

/**
 * Created by zhou on 2018/11/16.
 */

public class MachineFormData {
    private String date;
    private float score;
    public MachineFormData(String date,float score){
        this.date = date;
        this.score = score;
    }
    public float getScore(){
        return this.score;
    }

    public String getDate(){
        return this.date;
    }
}
