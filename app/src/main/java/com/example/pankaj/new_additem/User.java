package com.example.pankaj.new_additem;

/**
 * Created by pankaj on 24/3/18.
 */

public class User {

    String image;
    String name;
    String user_id;
    String status;

    public User() {
        this.image =null;
        this.name = null;
        this.status=null;
        this.image=null;
    }
    public User(String image, String name,String user_id,String status) {
        this.image = image;
        this.name = name;
        this.user_id=user_id;
        this.status=status;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
