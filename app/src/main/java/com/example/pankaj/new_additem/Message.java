package com.example.pankaj.new_additem;

import java.util.Date;

/**
 * Created by pankaj on 28/3/18.
 */

public class Message {


    public String message;
    public String messageUser;
    public String messageTime;

    public Message() {

    }
    public Message(String message, String messageUser,String messageTime) {
        this.message = message;
        this.messageUser = messageUser;
        this.messageTime=messageTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }



}
