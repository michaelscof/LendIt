package com.example.pankaj.new_additem;

/**
 * Created by pankaj on 16/4/18.
 */

public class NotificationMessage {


    String fromName;
    String message;
    public NotificationMessage() {
        this.fromName =null;
        this.message = null;
    }
    public NotificationMessage(String fromName,String message) {
        this.fromName =fromName;
        this.message = message;
    }

    public String getFromName() {
        return fromName;
    }

    public String getMessage() {

        return message;
    }

    public void setFromName() {

        this.fromName=fromName;
    }

    public void setMessage() {
        this.message=message;
    }

}
