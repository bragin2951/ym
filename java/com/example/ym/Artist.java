package com.example.ym;

import android.os.Parcel;
import android.os.Parcelable;

//класс, для хранения данных исполнителя
public class Artist implements Parcelable {
    public int id;
    public String name;
    public String[] genres;
    public int tracks;
    public int albums;
    public String link;
    public String description;
    public Cover cover;

    public Artist() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeStringArray(genres);
        dest.writeInt(tracks);
        dest.writeInt(albums);
        dest.writeString(link);
        dest.writeString(description);
        dest.writeParcelable(cover, 0);
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>(){

        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    private Artist(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        genres = parcel.createStringArray();
        tracks = parcel.readInt();
        albums = parcel.readInt();
        link = parcel.readString();
        description = parcel.readString();
        cover = parcel.readParcelable(Cover.class.getClassLoader());
    }
}
