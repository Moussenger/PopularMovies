package edu.udacity.mou.project.popularmovies.utils;

import android.os.Parcel;

/**
 * Created by mou on 18/06/16.
 */
public class ParcelableUtils {
    public static void writeBoolean (Parcel parcel, boolean bool) {
        parcel.writeByte((byte) (bool ? 1 : 0));
    }

    public static boolean readBoolean (Parcel input) {
        return input.readByte() != 0;
    }
}
