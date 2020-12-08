package com.example.myquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private Button btnDone;
    private TextView scoreMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        btnDone = findViewById(R.id.btnScoreEndButton);
        scoreMark = findViewById(R.id.tVScoreMark);

        /*Update Score*/
        String score = getIntent().getStringExtra("SCORE");
        scoreMark.setText(score);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScoreActivity.this, MainActivity.class);
                ScoreActivity.this.startActivity(i);
                ScoreActivity.this.finish();
            }
        });
    }
}