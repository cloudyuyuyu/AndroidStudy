package com.oppo.qiuyu.geoquiz;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = QuizActivity.class.getSimpleName();
    private static final String KEY_INDEX = "index";
    private static final String KEY_IS_CHEATER = "is_cheater";
    private static final String KEY_CHEAT_COUNT = "cheat_count";
    private static final int REQUEST_CODE_CHEAT = 0;


    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private TextView mShowCheatCountTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;
    private int mCheatCount = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            Log.i(TAG, "Restore from savedInstanceState!");
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCheatCount = savedInstanceState.getInt(KEY_CHEAT_COUNT, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
        }

        mTrueButton = findViewById(R.id.true_button);
//        mTrueButton = findViewById(R.id.question_text_view);
        mFalseButton = findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.prev_button);
        mCheatButton = findViewById(R.id.cheat_button);

        mTrueButton.setOnClickListener(this);
        mFalseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);
        mCheatButton.setOnClickListener(this);

        mQuestionTextView = findViewById(R.id.question_text_view);
        mShowCheatCountTextView = findViewById(R.id.show_cheat_count);
        mQuestionTextView.setOnClickListener(this);

        String cheatCount = getResources().getString(R.string.show_cheat_count);
        mShowCheatCountTextView.setText(String.format(cheatCount, mCheatCount));
        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState(Bundle outState) called");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(KEY_CHEAT_COUNT, mCheatCount);
        outState.putBoolean(KEY_IS_CHEATER, mIsCheater);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.true_button:
                checkAnswer(true);
                break;
            case R.id.false_button:
                checkAnswer(false);
                break;
            case R.id.next_button:
            case R.id.question_text_view:
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = mQuestionBank[mCurrentIndex].isCheater();
                updateQuestion();
                break;
            case R.id.prev_button:
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = mCurrentIndex - 1;
                }
                updateQuestion();
                break;
            case R.id.cheat_button:
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity
                        .newIntent(QuizActivity.this, answerIsTrue, mCheatCount);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            if (mIsCheater) {
                mQuestionBank[mCurrentIndex].setCheater(true);
            }
            mCheatCount = CheatActivity.getCheatCount(data);
            mShowCheatCountTextView.setText("Cheat Count : " + mCheatCount);
        }
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast falseToast = Toast.makeText(QuizActivity.this,
                messageResId,
                Toast.LENGTH_SHORT);
        falseToast.setGravity(Gravity.BOTTOM, 0, 0);
        falseToast.show();
    }
}
