package edu.udacity.mou.project.popularmovies.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import edu.udacity.mou.project.popularmovies.utils.ParcelableUtils;

/**
 * Created by Mou on 27/9/15.
 */
public class Movie implements Parcelable {
    private long   mId;
    private String mOriginalTitle;
    private String mMoviePoster;
    private String mSynopsis;
    private float  mUserRating;
    private String mReleaseDate;
    private boolean mFavorite;

    private Movie () {}

    public Movie (long id, String originalTitle, String moviePoster,
                   String synopsis, float userRating, String releaseDate, boolean favorite) {
        mId            = id;
        mOriginalTitle = originalTitle;
        mMoviePoster   = moviePoster;
        mSynopsis      = synopsis;
        mUserRating    = userRating;
        mReleaseDate   = releaseDate;
        mFavorite      = favorite;
    }

    public Movie (Parcel input) {
        mId            = input.readLong();
        mOriginalTitle = input.readString();
        mMoviePoster   = input.readString();
        mSynopsis      = input.readString();
        mUserRating    = input.readFloat();
        mReleaseDate   = input.readString();
        mFavorite      = ParcelableUtils.readBoolean(input);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mMoviePoster);
        dest.writeString(mSynopsis);
        dest.writeFloat(mUserRating);
        dest.writeString(mReleaseDate);
        ParcelableUtils.writeBoolean(dest, mFavorite);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel input) {
            return new Movie(input);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static class Builder {
        private Movie mMovie;

        public Builder (Cursor cursor, int[] fields) { }
        public Builder () { mMovie = new Movie(); }

        public Builder setId(long id) { mMovie.setId(id); return this; }
        public Builder setOriginalTitle(String originalTitle) {  mMovie.setOriginalTitle(originalTitle); return this; }
        public Builder setMoviePoster(String moviePoster) { mMovie.setMoviePoster(moviePoster); return this; }
        public Builder setSynopsis(String synopsis) { mMovie.setSynopsis(synopsis); return this; }
        public Builder setUserRating(float userRating) { mMovie.setUserRating(userRating); return this; }
        public Builder setReleaseDate(String releaseDate) { mMovie.setReleaseDate(releaseDate); return this; }
        public Builder setFavorite (Boolean favorite) { mMovie.setFavorite(favorite); return this; }

        public Movie build () {
            return mMovie;
        }

    }

    public long getId() { return mId; }
    public void setId(long id) { mId = id; }

    public String getOriginalTitle() { return mOriginalTitle; }
    public void setOriginalTitle(String originalTitle) {  mOriginalTitle = originalTitle; }

    public String getMoviePoster() { return mMoviePoster; }
    public void setMoviePoster(String moviePoster) { mMoviePoster = moviePoster; }

    public String getSynopsis() { return mSynopsis; }
    public void setSynopsis(String synopsis) { mSynopsis = synopsis; }

    public float getUserRating() { return mUserRating; }
    public void setUserRating(float userRating) { mUserRating = userRating; }

    public String getReleaseDate() { return mReleaseDate; }
    public void setReleaseDate(String releaseDate) { mReleaseDate = releaseDate; }

    public boolean isFavorite() { return mFavorite; }
    public void setFavorite(boolean mFavorite) { this.mFavorite = mFavorite; }

    @Override
    public String toString() {
        return "Movie{" +
                "mId=" + mId +
                ", mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mMoviePoster='" + mMoviePoster + '\'' +
                ", mSynopsis='" + mSynopsis + '\'' +
                ", mUserRating=" + mUserRating +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mFavorite ='" + mFavorite + '\'' +
                '}';
    }
}
