package edu.udacity.mou.project.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mou on 27/9/15.
 */
public class Review implements Parcelable {
    private String mAuthor;
    private String mContent;

    private Review() {}

    public Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    public Review(Parcel input) {
        mAuthor = input.readString();
        mContent = input.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        public Review createFromParcel(Parcel input) {
            return new Review(input);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public static class Builder {
        private Review mReview;

        public Builder (Cursor cursor, int[] fields) { }
        public Builder () { mReview = new Review(); }

        public Builder setAuthor(String author) { mReview.setAuthor(author); return this; }
        public Builder setContent(String content) {  mReview.setContent(content); return this; }

        public Review build () {
            return mReview;
        }

    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "mAuthor=" + mAuthor +
                ", mContent ='" + mContent + '\'' +
                '}';
    }
}
