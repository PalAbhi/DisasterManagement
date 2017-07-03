package com.abhishek.pal.xmlparser;

/**
 * Created by User on 24-05-2017.
 */

public class GridItem {
    private String image;
    private String title;
    private String retweet;
    private int sortindex;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle()
     {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.sortindex=Integer.parseInt(title);
    }

    public void  setId(String retweets) { this.retweet = retweets; }

    public String getId() { return retweet; }

    public int getsorter() {
        return this.sortindex;
    }
}

