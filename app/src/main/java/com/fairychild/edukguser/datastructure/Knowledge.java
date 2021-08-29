package com.fairychild.edukguser.datastructure;

import android.os.Parcel;
import android.os.Parcelable;

public class Knowledge implements Parcelable{
    private String label;
    private String category;
    private String uri;

    public Knowledge() {
    }

    protected Knowledge(Parcel in) {
        label = in.readString();
        category = in.readString();
        uri = in.readString();
    }

    public static final Creator<Knowledge> CREATOR = new Creator<Knowledge>() {
        @Override
        public Knowledge createFromParcel(Parcel in) {
            return new Knowledge(in);
        }

        @Override
        public Knowledge[] newArray(int size) {
            return new Knowledge[size];
        }
    };

    public Knowledge newInstance(String label, String category, String uri) {
        Knowledge knowledge = new Knowledge();
        knowledge.label = label;
        knowledge.category = category;
        knowledge.uri = uri;
        return knowledge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(label);
        parcel.writeString(category);
        parcel.writeString(uri);
    }

    public String getLabel() {
        return label;
    }

    public String getCategory() {
        return category;
    }

    public String getUri() {
        return uri;
    }


}
