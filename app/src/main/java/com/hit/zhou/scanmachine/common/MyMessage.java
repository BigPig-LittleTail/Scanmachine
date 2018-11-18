package com.hit.zhou.scanmachine.common;

import java.io.Serializable;

/**
 * Created by zhou on 2018/11/18.
 */

public class MyMessage implements Serializable {
    private String messageName;
    private String messageImageUrl;
    private String messageContent;

    public MyMessage(String messageName,String messageImageUrl,String messageContent){
        this.messageName = messageName;
        this.messageImageUrl = messageImageUrl;
        this.messageContent = messageContent;
    }

    public void setMessageName(String messageName){
        this.messageName = messageName;
    }

    public void setMessageImageUrl(String messageImageUrl){
        this.messageImageUrl = messageImageUrl;
    }

    public void setMessageContent(String messageContent){
        this.messageContent = messageContent;
    }

    public String getMessageName(){
        return this.messageName;
    }

    public String getMessageImageUrl(){
        return this.messageImageUrl;
    }

    public String getMessageContent(){
        return this.messageContent;
    }

}
