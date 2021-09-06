package com.fairychild.edukguser.datastructure;

import android.os.Parcel;
import android.os.Parcelable;

public class Favourite implements Parcelable {

    private Integer id;
    private String course;
    private String name;

    public Favourite(){}

    public static Favourite newInstance(Integer id, String course, String name) {
        Favourite favourite = new Favourite();
        favourite.id = id;
        favourite.course = course;
        favourite.name = name;
        return favourite;
    }

    protected Favourite(Parcel in) {
        id = in.readInt();
        course = in.readString();
        name = in.readString();
    }

    public static final Creator<Favourite> CREATOR = new Creator<Favourite>() {
        @Override
        public Favourite createFromParcel(Parcel in) {
            return new Favourite(in);
        }

        @Override
        public Favourite[] newArray(int size) {
            return new Favourite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(course);
        parcel.writeString(name);
    }

    public String getCourse() {
        return course;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}
