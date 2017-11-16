package com.example.admin.janjaruka.helper;

import java.util.Date;

/**
 * Created by Admin on 02/08/2017.
 */

public class ChatMessage {
    private String messageText, messageUserEmail, messageUserDisplayName;
    private Long messageTime;

    public ChatMessage(String messageText, String messageUserEmail,String messageUserDisplayName) {
        this.messageText = messageText;
        this.messageUserEmail = messageUserEmail;
        this.messageTime = new Date().getTime();
        this.messageUserDisplayName = messageUserDisplayName;
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUserEmail() {
        return messageUserEmail;
    }

    public void setMessageUser(String messageUser) {
        this.messageUserEmail = messageUserEmail;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }
    public  String getMessageUserDisplayName(){
        return messageUserDisplayName;
    }
    public void setMessageUserDisplayName(String messageUserDisplayName){
        this.messageUserDisplayName = messageUserDisplayName;
    }
}
