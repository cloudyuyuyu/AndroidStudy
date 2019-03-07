package com.oppo.qiuyu.geoquiz;

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mIsCheater;

    public Question(int textRestId, boolean answerTrue){
        mTextResId = textRestId;
        mAnswerTrue = answerTrue;
        mIsCheater = false;

    }

    public boolean isCheater() {
        return mIsCheater;
    }

    public void setCheater(boolean cheater) {
        mIsCheater = cheater;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
