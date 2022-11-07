package com.digital.moviesbuddypro.model;

import java.util.ArrayList;

public class Movie {

    private String mId, mTitle, mPoster, mDescription, mDuration, mIMDBRating, mCertificate, mCategory, mYouTubeVideoID;

    private ArrayList<String> mActor, mDirector, mWriter, mGenre, mLanguage, mRelease;

    public Movie(){}

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getIMDBRating() {
        return mIMDBRating;
    }

    public void setIMDBRating(String mIMDBRating) {
        this.mIMDBRating = mIMDBRating;
    }

    public String getCertificate() {
        return mCertificate;
    }

    public void setCertificate(String mCertificate) {
        this.mCertificate = mCertificate;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getYouTubeVideoID() {
        return mYouTubeVideoID;
    }

    public void setYouTubeVideoID(String mYouTubeVideoID) {
        this.mYouTubeVideoID = mYouTubeVideoID;
    }

    public ArrayList<String> getActor() {
        return mActor;
    }

    public void setActor(ArrayList<String> mActor) {
        this.mActor = mActor;
    }

    public ArrayList<String> getDirector() {
        return mDirector;
    }

    public void setDirector(ArrayList<String> mDirector) {
        this.mDirector = mDirector;
    }

    public ArrayList<String> getWriter() {
        return mWriter;
    }

    public void setWriter(ArrayList<String> mWriter) {
        this.mWriter = mWriter;
    }

    public ArrayList<String> getGenre() {
        return mGenre;
    }

    public void setGenre(ArrayList<String> mGenre) {
        this.mGenre = mGenre;
    }

    public ArrayList<String> getLanguage() {
        return mLanguage;
    }

    public void setLanguage(ArrayList<String> mLanguage) {
        this.mLanguage = mLanguage;
    }

    public ArrayList<String> getRelease() {
        return mRelease;
    }

    public void setRelease(ArrayList<String> mRelease) {
        this.mRelease = mRelease;
    }
}
