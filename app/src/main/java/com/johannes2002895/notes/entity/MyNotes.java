package com.johannes2002895.notes.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MyNotes implements Parcelable {
    private int id;
    private String title,description,date;



    public static final Creator<MyNotes> CREATOR = new Creator<MyNotes>() {
        @Override
        public MyNotes createFromParcel(Parcel in) {
            return new MyNotes(in);
        }

        @Override
        public MyNotes[] newArray(int size) {
            return new MyNotes[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.date);
    }
    public MyNotes(){

    }
    public MyNotes(int id, String title, String description, String date){
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;

    }
    private MyNotes(Parcel in){
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
    }

}
