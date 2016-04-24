package com.example.ym;

import android.os.Parcel;
import android.os.Parcelable;

//класс, для хранения url обложек
public class Cover implements Parcelable {
    public String small;
    public String big;

    public Cover() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(small);
        dest.writeString(big);
    }

    public static final Parcelable.Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel source) {
            return new Cover(source);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };

    private Cover(Parcel parcel) {
        small = parcel.readString();
        big = parcel.readString();
    }
}
