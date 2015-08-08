package com.example.dshrout.popularmovies.helper;

public class MovieCard {
    private String mId;
    private String mTitle;
    private String mReleaseDate;
    private String mRuntime;
    private String mBudget;
    private String mVoterAverage;
    private String mPosterPath;
    private String mBackdropPath;
    private String mSummary;

    public MovieCard(){
        mId = "";
        mTitle = "";
        mReleaseDate = "";
        mRuntime = "";
        mBudget = "";
        mVoterAverage = "";
        mPosterPath = "";
        mBackdropPath = "";
        mSummary = "";
    }

    public String getId() {return mId;}
    public void setId(String id) {
        mId = (id != null ? id : "");
    }

    public String getTitle() {return mTitle;}
    public void setTitle(String title) {
        mTitle = (title != null ? title : "");
    }

    public String getReleaseDate() {return mReleaseDate;}
    public void setReleaseDate(String date) {
        mReleaseDate = (date != null ? date : "");
    }

    public String getRuntime() {return mRuntime;}
    public void setRuntime(String runtime) {
        mRuntime = (runtime != null ? runtime : "");
    }

    public String getBudget() {return mBudget;}
    public void setBudget(String budget) {
        mBudget = (budget != null ? budget : "");
    }

    public String getVoterAverage() {return mVoterAverage;}
    public void setVoterAverage(String voterAverage) {
        mVoterAverage = (voterAverage != null ? voterAverage : "");
    }

    public String getPosterPath() {return mPosterPath;}
    public void setPosterPath(String path) {
        mPosterPath = (path != null ? path : "");
    }

    public String getBackdropPath() {return mBackdropPath;}
    public void setBackdropPath(String path) {
        mBackdropPath = (path != null ? path : "");
    }

    public String getSummary() {return mSummary;}
    public void setSummary(String summary) {
        mSummary = (summary != null ? summary : "");
    }
}
