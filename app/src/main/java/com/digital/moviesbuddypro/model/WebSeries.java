package com.digital.moviesbuddypro.model;

import java.util.ArrayList;

public class WebSeries {

    private String mId, mTitle, mMainPoster, mDescription, mDuration, mIMDBRating,
            mCertificate, mTotalSeason, mSeasonTag;

    private ArrayList<String> mActor, mCreator, mGenre, mLanguage, mRelease,
            mOriginalNetwork, mSeasonPoster, mTotalEpisode;

    public WebSeries(){}

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

    public String getMainPoster() {
        return mMainPoster;
    }

    public void setMainPoster(String mMainPoster) {
        this.mMainPoster = mMainPoster;
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

    public String getTotalSeason() {
        return mTotalSeason;
    }

    public void setTotalSeason(String mTotalSeason) {
        this.mTotalSeason = mTotalSeason;
    }

    public ArrayList<String> getTotalEpisode() {
        return mTotalEpisode;
    }

    public void setTotalEpisode(ArrayList<String> mTotalEpisode) {
        this.mTotalEpisode = mTotalEpisode;
    }

    public String getSeasonTag() {
        return mSeasonTag;
    }

    public void setSeasonTag(String mSeasonTag) {
        this.mSeasonTag = mSeasonTag;
    }

    public ArrayList<String> getActor() {
        return mActor;
    }

    public void setActor(ArrayList<String> mActor) {
        this.mActor = mActor;
    }

    public ArrayList<String> getCreator() {
        return mCreator;
    }

    public void setCreator(ArrayList<String> mCreator) {
        this.mCreator = mCreator;
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

    public ArrayList<String> getOriginalNetwork() {
        return mOriginalNetwork;
    }

    public void setOriginalNetwork(ArrayList<String> mOriginalNetwork) {
        this.mOriginalNetwork = mOriginalNetwork;
    }

    public ArrayList<String> getSeasonPoster() {
        return mSeasonPoster;
    }

    public void setSeasonPoster(ArrayList<String> mSeasonPoster) {
        this.mSeasonPoster = mSeasonPoster;
    }
}
