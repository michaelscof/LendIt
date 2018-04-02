package com.example.pankaj.new_additem;
/**
 * Created by pankaj on 30/3/18.
 */

public class Product {

    private String title;
    private int image;

    public Product() {


    }
    public Product(String title,int image) {

        this.image=image;
        this.title=title;
    }
    void setTitle(String title)
    {
        this.title=title;
    }
    String getTitle()
    {
        return title;
    }
    void setImage(int image)
    {
        this.image=image;
    }
    int getImage()
    {
        return image;
    }
}
