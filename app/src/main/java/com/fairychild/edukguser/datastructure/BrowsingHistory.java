package com.fairychild.edukguser.datastructure;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BrowsingHistory implements Parcelable {

    private Integer id;
    private String course;
    private String name;
    private String time;

    public BrowsingHistory() {}

    protected BrowsingHistory(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        course = in.readString();
        name = in.readString();
    }

    public static final Creator<BrowsingHistory> CREATOR = new Creator<BrowsingHistory>() {
        @Override
        public BrowsingHistory createFromParcel(Parcel in) {
            return new BrowsingHistory(in);
        }

        @Override
        public BrowsingHistory[] newArray(int size) {
            return new BrowsingHistory[size];
        }
    };

    public static BrowsingHistory newInstance(Integer id, String course, String name, String time) {
        BrowsingHistory browsingHistory = new BrowsingHistory();
        browsingHistory.id = id;
        browsingHistory.course = course;
        browsingHistory.name = name;
        browsingHistory.time = time;
        return browsingHistory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(course);
        parcel.writeString(name);
        parcel.writeString(time);
    }

    public Integer getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
}
