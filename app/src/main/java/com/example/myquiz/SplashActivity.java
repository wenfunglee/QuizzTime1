package com.example.myquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    /* Vaeiable that listed here have
    * TextView
    * Firebase
    * An List that hold the question to load data
    * */

    private TextView view1;
    public  static List<String> catList = new ArrayList<>();
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        view1 = findViewById(R.id.SplashWord);

       /*
       * Get animation from anim directory from resource
       * Set animation on TextView
       */
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.anim2_pulse);
        view1.setAnimation(anim);

        // get resource from the firebase database
        firestore = FirebaseFirestore.getInstance();

        /*
        * Create thread to speed up the performance
        * Sleep for 3 second then start this thread
        */

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                loadData();

            }
            // start activity
        }).start();

    }

     /* function which going to load the question from firebase*/
    private void loadData(){
        catList.clear();

        firestore.collection("QUIZ").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                /*We need to check whether the query are on click*/
                if(task.isSuccessful()){
                    /*Fetch the document value*/
                    DocumentSnapshot doc = task.getResult();

                    /*security check*/
                    if(doc.exists()){
                        /*key value in our firebase*/
                        long count = (long)doc.get("COUNT");

                        for(int i=1; i <= count; i++){

                            String catName = doc.getString("CAT" + String.valueOf(i));
                            /*create an category list*/
                            catList.add(catName);
                        }

                        Intent I = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(I);
                        SplashActivity.this.finish();

                    }else{
                        /*When there is not category in the quiz*/
                        Toast.makeText(SplashActivity.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }else{
                /* if it empty, notify user with error message */
                    Toast.makeText(SplashActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}