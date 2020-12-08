package com.example.myquiz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SetGridAdapter extends BaseAdapter {

    /*create variable */
    private int numOfSet;

    /*Constructor*/
    public SetGridAdapter(int numOfSet) {
        this.numOfSet = numOfSet;
    }

    @Override
    public int getCount() {
        return numOfSet;
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

        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_layout,parent,false);
        }else{
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(parent.getContext(),QuestionActivity.class);
                I.putExtra("SETNO",position+1);
                parent.getContext().startActivity(I);
            }
        });
        /*
        * Need to add 1 because we want array start from 1 not 0
        * */
        ((TextView) view.findViewById(R.id.setSelectionGridView)).setText(String.valueOf(position+1));

        return view;

    }
}
