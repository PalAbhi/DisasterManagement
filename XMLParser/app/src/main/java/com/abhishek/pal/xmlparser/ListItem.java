package com.abhishek.pal.xmlparser;

/**
 * Created by User on 23-05-2017.
 */
public class ListItem {

    private String name;
    private String no_of_img;
    private String date;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo_of_img() {
        return no_of_img;
    }

    public void setNo_of_img(String no_of_img) {
        this.no_of_img = no_of_img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "[ name=" + name + ", reporter Name=" + no_of_img + " , date=" + date + "]";
    }
}

