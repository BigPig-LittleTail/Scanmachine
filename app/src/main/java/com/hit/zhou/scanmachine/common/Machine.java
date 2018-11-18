package com.hit.zhou.scanmachine.common;

import java.io.Serializable;

/**
 * Created by zhou on 2018/11/15.
 */

public class Machine implements Serializable{
    private String phone;
    private String machineId;
    private String machineName;
    private String score;
    private String rank;
    private String imageUrl;

    public Machine(){

    }

    public Machine(String phone,String machineId,String machineName,String score,String rank,String imageUrl){
        this.phone = phone;
        this.machineId = machineId;
        this.machineName = machineName;
        this.score = score;
        this.rank = rank;
        this.imageUrl = imageUrl;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getMachineId(){
        return this.machineId;
    }

    public String getMachineName(){
        return this.machineName;
    }

    public String getScore(){
        return this.score;
    }

    public String getRank(){
        return this.rank;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setMachineId(String machineId){
        this.machineId = machineId;
    }

    public void setMachineName(String machineName){
        this.machineName = machineName;
    }

    public void setScore(String score){
        this.score = score;
    }

    public void setRank(String rank){
        this.rank = rank;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
}
