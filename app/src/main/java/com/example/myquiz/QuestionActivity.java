package com.example.myquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.example.myquiz.SetActivity.category_id;

/* implement on click listener to the project */
public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    /*All TextView Variable */
    private TextView questionNum,questionShowQ,questionTimer;

    /*All Button Variable*/
    private Button btnOp1,btnOp2,btnOp3,btnOp4;

    /*List*/
    private List<Question> questionList ;

    private int qNum;

    private CountDownTimer countDown;
    private int score;
    private FirebaseFirestore firestore;
    private int setNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        /*set ID*/
        // Text View
        questionNum = findViewById(R.id.tVQuestionNumber);
        questionShowQ = findViewById(R.id.tVQuestionShowQ);
        questionTimer = findViewById(R.id.tVQuestionTimer);

        // Button
        btnOp1 = findViewById(R.id.btnQuestionOpt1);
        btnOp2 = findViewById(R.id.btnQuestionOpt2);
        btnOp3 = findViewById(R.id.btnQuestionOpt3);
        btnOp4 = findViewById(R.id.btnQuestionOpt4);

        btnOp1.setOnClickListener(this);
        btnOp2.setOnClickListener(this);
        btnOp3.setOnClickListener(this);
        btnOp4.setOnClickListener(this);

        firestore = FirebaseFirestore.getInstance();

        // Get the set number from the intent
        setNo = getIntent().getIntExtra("SETNO",1);

        /*create a list that feed all of the question*/
        getQuestionsList();

        score = 0;

    }

    /*innitialize question list*/
    private void getQuestionsList(){
        /*question list will be our new variable */
        questionList = new ArrayList<>();

        /*Add quesiton , constructor will do the job
        * All of the correct answer will be depends on the number of correctAns variable
        * For example, this question opt2 is the correct answer since we set correctAns as 2*/

        /*Now fetch question and answer from database */

        firestore.collection("QUIZ").document("CAT"+ String.valueOf(category_id))
                .collection("SET" +String.valueOf(setNo))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    /*Fetch the document value*/
                    // this is to get the document , not the whole collection
                    QuerySnapshot question = task.getResult();

                    /*run a for loop in each of documentation
                    * Each documentation get all of the variable : question, answer , opt1, opt2 opt3 etc*/
                    for (QueryDocumentSnapshot doc : question){
                        questionList.add(new Question(doc.getString("QUESTION"),
                                doc.getString("A"),
                                doc.getString("B"),
                                doc.getString("C"),
                                doc.getString("D"),
                                Integer.valueOf(doc.getString("ANSWER"))
                        ));
                    }

                    setQuestion();

                } else {
                    /* if it false, notify user*/
                    Toast.makeText(QuestionActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        //setQuestion();
    }

    private void setQuestion(){
        questionTimer.setText(String.valueOf(10));

        /*set textview question show out first question , array start from 0, then get question*/
        questionShowQ.setText(questionList.get(0).getQuestion());

        /*set answer selection*/
        btnOp1.setText(questionList.get(0).getOpt1());
        btnOp2.setText(questionList.get(0).getOpt2());
        btnOp3.setText(questionList.get(0).getOpt3());
        btnOp4.setText(questionList.get(0).getOpt4());

        /*Question number show */
        questionNum.setText(String.valueOf(1) + "/" +String.valueOf(questionList.size()));

        /*Start timmer */
        startTime();

        /*Save the number of the question here*/
        qNum = 0;

    }

    private void startTime(){

        // Our total time 10 milisecond, each second is count down by 1 milisecond
         countDown = new CountDownTimer(12000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished < 10000)
                // set timer count until finish by second (milisecond / 1000 = second )
                questionTimer.setText(String.valueOf(millisUntilFinished / 1000 ));
            }

            @Override
            public void onFinish() {
                // When timer up , change the question
                changeQuestion();

            }
        };
        countDown.start();
    }

    /*save the value once user have select the answer and check the answer immediately*/
    @Override
    public void onClick(View v) {

        int selectedOtp = 0;
        switch (v.getId()){

            case R.id.btnQuestionOpt1:
                selectedOtp = 1;
                break;

            case R.id.btnQuestionOpt2:
                selectedOtp = 2;
                break;

            case R.id.btnQuestionOpt3:
                selectedOtp = 3;
                break;

            case R.id.btnQuestionOpt4:
                selectedOtp = 4;
                break;
            default:
        }

        countDown.cancel();
        /*Check whether the answer is right or wrong */
        checkAnswer(selectedOtp ,v);
    }

    /*Set the background color */
    private void checkAnswer(int selectAnswer , View view){
        // check if user the selected answer is correct from the list
        if(selectAnswer == questionList.get(qNum).getCorrectAns()){
            //if correct Answer

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                score++;
            }

        }else{
            // if incorrect Answer
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }

            switch (questionList.get(qNum).getCorrectAns()){
                case 1:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnOp1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    }
                    break;
                case 2:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnOp2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    }
                    break;
                case 3:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnOp3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    }
                    break;
                case 4:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnOp4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    }
                    break;
            }
        }

        Handler handler = new Handler();
        // Delay the question after user have select the answer
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        },2000);
        // when user click the answer, change the question


    }

    private void changeQuestion(){


        // check if the question is the last question
        if(qNum < questionList.size() - 1){
            qNum++ ;
            playAnim(questionShowQ,0,0);
            playAnim(btnOp1,0,1);
            playAnim(btnOp2,0,2);
            playAnim(btnOp3,0,3);
            playAnim(btnOp4,0,4);

           questionNum.setText(String.valueOf(qNum+1) + "/" + String.valueOf(questionList.size()));
           questionTimer.setText(String.valueOf(10));
           startTime();

        }else{

            // Go to score activity
            Intent i = new Intent(QuestionActivity.this,ScoreActivity.class);

            // Bring the score to the Score Activity
            i.putExtra("SCORE",String.valueOf(score)+"/"+String.valueOf(questionList.size()));
            //The flag will clear the previous activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }
    }

    private void playAnim(View view, final int value, int viewNum){

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {

            // Listener function
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // change the question once the question have end.

                if(value == 0){
                    switch(viewNum){
                        case 0:
                            ((TextView)view).setText(questionList.get(qNum).getQuestion());
                            break;
                        case 1:
                            ((Button)view).setText(questionList.get(qNum).getOpt1());
                            break;
                        case 2:
                            ((Button)view).setText(questionList.get(qNum).getOpt2());
                            break;
                        case 3:
                            ((Button)view).setText(questionList.get(qNum).getOpt3());
                            break;
                        case 4:
                            ((Button)view).setText(questionList.get(qNum).getOpt4());
                            break;
                    }

                    if(viewNum != 0){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f4af1b")));
                        }
                    }
                    playAnim(view,1,viewNum);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    // If user quit from the test, the timer will cancel automatically.
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDown.cancel();
    }
}