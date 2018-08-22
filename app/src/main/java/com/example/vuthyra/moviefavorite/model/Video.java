package com.example.vuthyra.moviefavorite.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
    
    private String mYoutubeKey;
    private String mNameOfVideo;
    private String mSite;
    private int mVideoQuality;

    // A Plain constructor.
    public Video() {
    }

    public static final Creator CREATOR = new Creator() {
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    /**
     * Parcel parts of each attributes for Video.class
     *
     * @param input to bind all class objects to parcel.
     */

    private Video (Parcel input) {
        this.mYoutubeKey = input.readString();
        this.mNameOfVideo = input.readString();
        this.mSite = input.readString();
        this.mVideoQuality = input.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mYoutubeKey);
        dest.writeString(mNameOfVideo);
        dest.writeString(mSite);
        dest.writeInt(mVideoQuality);
    }



    public String getmYoutubeKey() {
        return mYoutubeKey;
    }

    public void setmYoutubeKey(String mYoutubeKey) {
        this.mYoutubeKey = mYoutubeKey;
    }

    public String getmNameOfVideo() {
        return mNameOfVideo;
    }

    public void setmNameOfVideo(String mNameOfVideo) {
        this.mNameOfVideo = mNameOfVideo;
    }

    public String getmSite() {
        return mSite;
    }

    public void setmSite(String mSite) {
        this.mSite = mSite;
    }

    public int getmVideoQuality() {
        return mVideoQuality;
    }

    public void setmVideoQuality(int mVideoQuality) {
        this.mVideoQuality = mVideoQuality;
    }


    public Video (String key, String nameOfVideo, String site, int videoQuality) {
        this.mYoutubeKey = key;
        this.mNameOfVideo = nameOfVideo;
        this.mSite = site;
        this.mVideoQuality = videoQuality;
    }


}