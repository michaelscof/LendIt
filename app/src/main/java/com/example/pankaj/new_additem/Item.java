package com.example.pankaj.new_additem;

/**
 * Created by pankaj on 19/3/18.
 */

public class Item {

    private String image,type,size,price,caution,user_id;



    public Item() {
        this.image = null;
        this.type = null;
        this.size = null;
        this.price = null;
        this.caution = null;
        this.user_id=null;
    }


    public Item(String image, String type, String size, String price, String caution,String user_id) {
        this.image = image;
        this.type = type;
        this.size = size;
        this.price = price;
        this.caution = caution;
        this.user_id=user_id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
