package com.fairychild.edukguser.datastructure;

import android.os.Parcel;
import android.os.Parcelable;

public class LocalCache implements Parcelable {

    private Integer id;
    private String course;
    private String name;
    private String time;

    public LocalCache() {}

    protected LocalCache(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        course = in.readString();
        name = in.readString();
        time = in.readString();
    }

    public static final Creator<LocalCache> CREATOR = new Creator<LocalCache>() {
        @Override
        public LocalCache createFromParcel(Parcel in) {
            return new LocalCache(in);
        }

        @Override
        public LocalCache[] newArray(int size) {
            return new LocalCache[size];
        }
    };

    public static LocalCache newInstance(Integer id, String course, String name, String time) {
        LocalCache localCache = new LocalCache();
        localCache.id = id;
        localCache.course = course;
        localCache.name = name;
        localCache.time = time;
        return localCache;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(course);
        parcel.writeString(name);
        parcel.writeString(time);
    }

    public Integer getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    @Override
    public String toString() {
        return "LocalCache{" +
                "id=" + id +
                ", course='" + course + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
