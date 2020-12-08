package com.example.myquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.myquiz.SplashActivity.catList;


public class CreateCategory extends AppCompatActivity {

    private GridView catGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        catGrid = findViewById(R.id.catGridView);

        /*
        * Create an new variable from class adapater and pass in catList list inside the adapter.
        * set it into Grid View
        */
        CatGridAdapter catAdpter = new CatGridAdapter(catList);
        catGrid.setAdapter(catAdpter);


    }
}