package com.example.vuthyra.moviefavorite.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private String mAuthor;
    private String mContent;
    private String mUrl;

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public static Creator getCREATOR() {
        return CREATOR;
    }

    //Create one default constructor.
    public Review(){
    }

    public Review(String author, String content, String url) {

        this.mAuthor = author;
        this.mContent = content;
        this.mUrl = url;
    }

    public static final Creator CREATOR = new Creator() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    /**
     * Parcel parts of each attributes for Review.class
     *
     * @param input to bind all class objects to parcel.
     */

    public Review (Parcel input) {
        this.mAuthor = input.readString();
        this.mContent = input.readString();
        this.mUrl = input.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
        dest.writeString(mUrl);
    }

}
