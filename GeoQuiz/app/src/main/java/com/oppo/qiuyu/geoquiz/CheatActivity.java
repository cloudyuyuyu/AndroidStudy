package com.oppo.qiuyu.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CheatActivity.class.getSimpleName();
    private static final String EXTRA_ANSWER_IS_TRUE = "com.oppo.qiuyu.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.oppo.qiuyu.geoquiz.answer_shown";
    private static final String EXTRA_CHEAT_COUNT = "com.oppo.qiuyu.geoquiz.cheat_count";
    private static final String KEY_IS_ANSWER_SHOW = "ker_is_answer_show";
    private static final String KEY_CHEAT_COUNT = "ker_cheat_count";

    private TextView mAnswerTextView;
    private TextView mApiLevelTextView;
    private Button mShowAnswerButton;

    private boolean mAnswerIsTrue;
    private boolean mAnswerIsShown = false;
    private int mCheatCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerIsShown = savedInstanceState.getBoolean(KEY_IS_ANSWER_SHOW, false);
            Log.i(TAG, "Restore from savedInstanceState!" + mAnswerIsShown);
        }
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mCheatCount = getIntent().getIntExtra(EXTRA_CHEAT_COUNT, 0);

        mAnswerTextView = findViewById(R.id.answer_text_view);
        mApiLevelTextView = findViewById(R.id.show_api_level);
        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(this);

        if (mCheatCount == 3) {
            mShowAnswerButton.setEnabled(false);
        }

        if (mAnswerIsShown) {
            if (mAnswerIsTrue) {
                mAnswerTextView.setText(R.string.true_button);
            } else {
                mAnswerTextView.setText(R.string.false_button);
            }
            setAnswerShowResult(true, mCheatCount);
        }

        String apiLevel = getResources().getString(R.string.show_api_level);
        mApiLevelTextView.setText(String.format(apiLevel, Build.VERSION.SDK_INT));
    }

    public static Intent newIntent(Context context, boolean answerIsTrue, int cheatCount) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(EXTRA_CHEAT_COUNT, cheatCount);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static int getCheatCount(Intent result) {
        return result.getIntExtra(EXTRA_CHEAT_COUNT, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_answer_button:
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mAnswerIsShown = true;
                mCheatCount = mCheatCount + 1;

                setAnswerShowResult(true, mCheatCount);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int x = mShowAnswerButton.getWidth() / 2;
                    int y = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator animator = ViewAnimationUtils
                            .createCircularReveal(mAnswerTextView, x, y, radius, 0);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    animator.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
                break;


            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState(Bundle outState) called");
        outState.putBoolean(KEY_IS_ANSWER_SHOW, mAnswerIsShown);
        outState.putInt(KEY_CHEAT_COUNT, mCheatCount);
    }

    private void setAnswerShowResult(boolean isAnswerShown, int cheatCount) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(EXTRA_CHEAT_COUNT, cheatCount);
        setResult(RESULT_OK, data);
    }
}
