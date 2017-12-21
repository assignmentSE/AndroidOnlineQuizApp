package com.mixasian.androidonlinequizapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mixasian.androidonlinequizapp.Common.Common;
import com.squareup.picasso.Picasso;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    final static long INTERVAL= 1000; //1 sec
    final static long TIMEOUT =7000; //7 sec
    int progressValue =0;

    CountDownTimer mCountDown;
    int index=0,score=0,thisQuestion=0,totalQuestion,CorrectAnswer;

    //firebase
    FirebaseDatabase database;
    DatabaseReference questions;

    ProgressBar progressBar;
    ImageView question_image;
    Button btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;
    TextView txtScore, txtQuestionNum,question_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        //firebase
        database=FirebaseDatabase.getInstance();
        questions=database.getReference("Question");

        //Views
        txtScore =(TextView)findViewById(R.id.txtScore);
        txtQuestionNum=(TextView) findViewById(R.id.txtTotalQuestion);
        question_text =(TextView) findViewById(R.id.question_text);
        progressBar =(ProgressBar) findViewById(R.id.progressBar);

        btnAnswerA = (Button) findViewById(R.id.btnAnswerA);
        btnAnswerB =(Button) findViewById(R.id.btnAnswerB);
        btnAnswerC =(Button) findViewById(R.id.btnAnswerC);
        btnAnswerD =(Button) findViewById(R.id.btnAnswerD);

        btnAnswerA.setOnClickListener(this);
        btnAnswerB.setOnClickListener(this);
        btnAnswerC.setOnClickListener(this);
        btnAnswerD.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        mCountDown.cancel();
        if(index < totalQuestion) //atill have question in list
        {
            Button clickedButton = (Button)view;
            if(clickedButton.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
            {
                //choose correct answer
                score+=10;
                CorrectAnswer++;
                showQuestion(++index); //next question
            }
            else
            {
                //choose wrong
                Intent intent = new Intent(this,Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE",score);
                dataSend.putInt("TOTAL",totalQuestion);
                dataSend.putInt("CORRECT",CorrectAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();
            }

            txtScore.setText(String.format("%d",score));
        }

    }

    private void showQuestion(int index) {
        if(index <totalQuestion)
        {
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d",thisQuestion,totalQuestion));
            progressBar.setProgress(0);
            progressValue=0;
            if(Common.questionList.get(index).getIsImageQuestion().equals(true))
            {
                //if this question is image
                Picasso.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
               // question_text.setVisibility(View.INVISIBLE);
            }

            btnAnswerA.setText(Common.questionList.get(index).getAnswerA());
            btnAnswerB.setText(Common.questionList.get(index).getAnswerB());
            btnAnswerC.setText(Common.questionList.get(index).getAnswerC());
            btnAnswerD.setText(Common.questionList.get(index).getAnswerD());


            mCountDown.start(); //start timer
        }
        else
        {
            //if its final question
            Intent intent = new Intent(this,Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT",CorrectAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();

        }
    }
}
