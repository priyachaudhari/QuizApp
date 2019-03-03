package com.bignerdranch.android.quiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

import static com.bignerdranch.android.quiz.R.string.correct_toast;
import static com.bignerdranch.android.quiz.R.string.question_americas;

public class MainActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private Boolean mIsCheater;
    private TextView mQuestionTextView;
    public static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    public static final int REQUEST_CODE_CHEAT = 0;
    public int mTrueAnswer =0;
    private ArrayList<Integer> mQuestion =new ArrayList<Integer>(6);


    private Question[] mQuestionBank = new Question[]
            {
                    new Question(R.string.question_australia, true),
                    new Question(R.string.question_africa, true),
                    new Question(R.string.question_americas, false),
                    new Question(R.string.question_asia, false),
                    new Question(R.string.question_mideast, true),
                    new Question(R.string.question_oceans, true)
            };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mQuestion = savedInstanceState.getIntegerArrayList("Array");
        }

        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_main);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        //updateQuestion();
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (Button) findViewById(R.id.next_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        updateQuestion();

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue =
                        mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(
                        MainActivity.this,
                        answerIsTrue
                );
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putIntegerArrayList("Array",mQuestion);
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        int resultResId= (mTrueAnswer*100)/6;
        if(mQuestion.size()>5){
            Toast.makeText(this,Integer.toString(resultResId)+"% correct annwers",Toast.LENGTH_LONG).show();
        }
    }

    private void checkAnswer(Boolean userPressedTrue) {

        Log.d(TAG,"checkanswer method start");

        int messageResId = 0;
        mIsCheater = false;
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        mQuestion.add(mCurrentIndex);

        if(userPressedTrue == answerIsTrue){
            Log.d(TAG, "checkanswer block mid");
            if(mIsCheater){
                messageResId= R.string.judgement_toast;
            }
            else {
            messageResId = R.string.correct_toast;
            mTrueAnswer += 1;
            }
        }
        else {
            if (mIsCheater) {
                messageResId = R.string.judgement_toast;
            } else
                messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

        Log.d(TAG,"checkanswer method ends");
    }

    protected void OnActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK)
            return;

        if(resultCode == REQUEST_CODE_CHEAT)
            if(data == null)
                return;

        mIsCheater = CheatActivity.wasAnswerShown(data);
    }
}



