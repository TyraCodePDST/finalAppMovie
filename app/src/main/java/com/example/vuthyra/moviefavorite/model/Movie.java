package com.example.vuthyra.moviefavorite.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity (tableName = "favorite_table")
public class Movie implements Parcelable {
    // Annotate the id as PrimaryKey. Set autoGenerate to true.
    @PrimaryKey(autoGenerate = true)
    public int mId;
//    @ColumnInfo(name = "mTitle")
    public String mTitle;
    public String mPosterImage;
    public String mPlotInfo;
    public String mReleaseDate;
    public float mRating;


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPosterImage() {
        return mPosterImage;
    }

    public void setmPosterImage(String mPosterImage) {
        this.mPosterImage = mPosterImage;
    }

    public String getmPlotInfo() {
        return mPlotInfo;
    }

    public void setmPlotInfo(String mPlotInfo) {
        this.mPlotInfo = mPlotInfo;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public float getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }

    public Movie(int numId, String mTitle, String mPosterImage, String mPlotInfo, String mReleaseDate, float mRating) {

        this.mId = numId;
        this.mTitle = mTitle;
        this.mPosterImage = mPosterImage;
        this.mPlotInfo = mPlotInfo;
        this.mReleaseDate = mReleaseDate;
        this.mRating = mRating;
    }

    public Movie() {
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * Parcel parts of each attributes for Movie.class
     *
     * @param input to bind all class objects to parcel.
     */

    public Movie(Parcel input) {

        this.mId = input.readInt();
        this.mTitle = input.readString();
        this.mPosterImage = input.readString();
        this.mPlotInfo = input.readString();
        this.mReleaseDate = input.readString();
        this.mRating = input.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mPosterImage);
        dest.writeString(mPlotInfo);
        dest.writeString(mReleaseDate);
        dest.writeFloat(mRating);
    }
}