package com.gov.phcrevitalization.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubItems implements Parcelable {
    String subMenuItem;

    protected SubItems(Parcel in) {
        subMenuItem = in.readString();
    }

    public String getSubMenuItem() {
        return subMenuItem;
    }

    public SubItems(String subMenuItem) {
        this.subMenuItem = subMenuItem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Creator<SubItems> CREATOR = new Creator<SubItems>() {
        @Override
        public SubItems createFromParcel(Parcel parcel) {
            return new SubItems(parcel);
        }

        @Override
        public SubItems[] newArray(int i) {
            return new SubItems[i];
        }
    };
}