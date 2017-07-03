package com.abhishek.pal.xmlparser;

import java.util.Comparator;

/**
 * Created by User on 16-06-2017.
 */

public class TweetSorter implements Comparator<GridItem> {

    @Override
    public int compare(GridItem gridItem, GridItem t1) {
        int returnval=0;

        if(gridItem.getsorter()==t1.getsorter()){
            returnval=0;
        }
        if(gridItem.getsorter()>t1.getsorter()){
            returnval=1;
        }
        if(gridItem.getsorter()<t1.getsorter()){
            returnval=-1;
        }

        return returnval;
    }
}
