package com.example.pankaj.new_additem;

/**
 * Created by pankaj on 19/3/18.
 */

public class Item {

    private String image,type,desc,price,caution,user_id,user_name,subType;
    private int popularity;

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Item() {

        this.popularity=0;
        this.image = null;
        this.type = null;
        this.desc = null;
        this.price = null;

        this.caution = null;
        this.user_id=null;
        this.user_name=null;
        this.subType=null;
    }

    public Item(String image, String type, String desc, String price, String caution, String user_id, String user_name,String subType, int popularity) {
        this.image = image;
        this.type = type;
        this.desc =desc;
        this.price = price;
        this.caution = caution;
        this.user_id=user_id;
        this.popularity=popularity;
        this.user_name=user_name;
        this.subType=subType;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String size) {
        this.desc = size;
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
