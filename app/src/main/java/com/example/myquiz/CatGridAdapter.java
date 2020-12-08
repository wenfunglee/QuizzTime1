package com.example.myquiz;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class CatGridAdapter extends BaseAdapter {

    /*create item here*/
    private List<String> catList;

    /*Constructor for the list we pass in */
    public CatGridAdapter(List<String> catList) {
        this.catList = catList;
    }

    /*get the position of get count*/
    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        /*
        * if the view is empty, straight read it into the cat_layout which in layout directory
        * If user click on any of category , show the set of the question
        * */
        if(convertView == null ){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_layout,parent,false);
        }else{
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent activity
                // also put all of the catlist inside to let other class to use
                Intent i = new Intent(parent.getContext(),SetActivity.class);
                i.putExtra("CATEGORY",catList.get(position));
                i.putExtra("CATEGORY_ID",position + 1);
                parent.getContext().startActivity(i);
            }
        });

        /* Set the text view by getting position inside the array */
        ((TextView) view.findViewById(R.id.catGridViewName)).setText(catList.get(position));

        // Generating random color
        // set the argb color
        Random rnd = new Random();
        int color = Color.argb(255,rnd.nextInt(255),rnd.nextInt(),rnd.nextInt(255));

        view.setBackgroundColor(color);

        return view;
    }
}
