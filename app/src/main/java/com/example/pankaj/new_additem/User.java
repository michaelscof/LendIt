package com.example.pankaj.new_additem;

/**
 * Created by pankaj on 24/3/18.
 */

public class User {

    String image;
    String name;
    String user_id;

    public User() {
        this.image = image;
        this.name = name;
    }

    public User(String image, String name,String user_id) {
        this.image = image;
        this.name = name;
        this.user_id=user_id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
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
