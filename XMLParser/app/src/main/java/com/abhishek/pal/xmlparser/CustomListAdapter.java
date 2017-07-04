package com.abhishek.pal.xmlparser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by User on 23-05-2017.
 */
public class CustomListAdapter extends BaseAdapter {
    private ArrayList<ListItem> listData;
    //public ArrayList<Bitmap> bitmap;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public CustomListAdapter(Context context, ArrayList<ListItem> listData) {
        this.listData = listData;
        //this.bitmap=bitmaps;
        layoutInflater = LayoutInflater.from(context);
        mContext=context;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item2, null);
            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.tweet);
            //holder.noofImages = (TextView) convertView.findViewById(R.id.imagedata);
            //holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListItem newsItem = listData.get(position);

        holder.headlineView.setText(newsItem.getName());
        //holder.reportedDateView.setText(newsItem.getDate());
        //holder.noofImages.setText(newsItem.getNo_of_img());

        if (holder.imageView != null) {
            //new ImageDownloaderTask(holder.imageView).execute(newsItem.getUrl());
            Glide
                    .with(mContext)
                    .load(newsItem.getUrl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        //TextView noofImages;
        //TextView reportedDateView;
        ImageView imageView;
    }
}
