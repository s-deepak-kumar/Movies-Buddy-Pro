package com.digital.moviesbuddypro.model;

public class Category {

    private int mCategoryBannerInt;
    private String mCategoryTitle;

    public Category(){}

    public Category(String mCategoryTitle, int mCategoryBannerInt){
        this.mCategoryTitle = mCategoryTitle;
        this.mCategoryBannerInt = mCategoryBannerInt;
    }

    public int getCategoryBannerInt() {
        return mCategoryBannerInt;
    }

    public void setCategoryBannerInt(int mCategoryBannerInt) {
        this.mCategoryBannerInt = mCategoryBannerInt;
    }

    public String getCategoryTitle() {
        return mCategoryTitle;
    }

    public void setCategoryTitle(String mCategoryTitle) {
        this.mCategoryTitle = mCategoryTitle;
    }
}
