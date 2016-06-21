package edu.udacity.mou.project.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import edu.udacity.mou.project.popularmovies.utils.ParcelableUtils;

/**
 * Created by Mou on 27/9/15.
 */
public class Trailer implements Parcelable {
    private String mKey;
    private String mName;

    private Trailer() {}

    public Trailer(String key, String name) {
        mKey = key;
        mName = name;
    }

    public Trailer(Parcel input) {
        mKey = input.readString();
        mName = input.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeString(mName);
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        public Trailer createFromParcel(Parcel input) {
            return new Trailer(input);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public static class Builder {
        private Trailer mTrailer;

        public Builder (Cursor cursor, int[] fields) { }
        public Builder () { mTrailer = new Trailer(); }

        public Builder setKey(String key) { mTrailer.setKey(key); return this; }
        public Builder setName(String name) {  mTrailer.setName(name); return this; }

        public Trailer build () {
            return mTrailer;
        }

    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "mKey=" + mKey +
                ", mName ='" + mName + '\'' +
                '}';
    }
}
