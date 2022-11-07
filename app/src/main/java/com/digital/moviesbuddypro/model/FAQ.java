package com.digital.moviesbuddypro.model;

public class FAQ {
    private String mId, mQuestion, mAnswer, mYoutubeLink, mLanguage;

    public FAQ(){}

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }

    public String getYoutubeLink() {
        return mYoutubeLink;
    }

    public void setYoutubeLink(String mYoutubeLink) {
        this.mYoutubeLink = mYoutubeLink;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }
}
