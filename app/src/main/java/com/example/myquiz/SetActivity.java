package com.example.myquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class SetActivity extends AppCompatActivity {

    private GridView setGView;
    private FirebaseFirestore firestore;
    public static int category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        setGView = findViewById(R.id.setGridView);

        //get catGridAdpater
        category_id = getIntent().getIntExtra("CATEGORY_ID", 1);

        firestore = FirebaseFirestore.getInstance();
        loadSet();


    }

    /*Load all of the document from the firebsae
    * According to the database map
    * */
    public void loadSet() {
        firestore.collection("QUIZ").document("CAT" + String.valueOf(category_id))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                /*We need to check whether the query are on click*/
                if (task.isSuccessful()) {
                    /*Fetch the document value*/
                    DocumentSnapshot doc = task.getResult();

                    /*security check*/
                    if (doc.exists()) {
                        /*key value in our firebase*/
                        long set = (long) doc.get("SET");

                        /*set the question from the firebase*/
                        SetGridAdapter adapter = new SetGridAdapter((int) set);

                        setGView.setAdapter(adapter);

                    } else {
                        /*When there is not category in the quiz*/
                        Toast.makeText(SetActivity.this, "No CAT Document Exists!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    /* if it false, notify user*/
                    Toast.makeText(SetActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

        });
    }
}