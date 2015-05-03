package com.rorantes.muvruler;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Roger on 4/25/2015.
 */
public class LogEntry implements Parcelable {

    private float measurement;
    private String tagname;

    public LogEntry(String tagname, float measurement){
        this.measurement = measurement;
        this.tagname = tagname;
    }

    public String getTagname(){
        return tagname;
    }
    public float getMeasurement(){
        return measurement;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tagname);
        dest.writeFloat(measurement);
    }
    public static final Parcelable.Creator<LogEntry> CREATOR = new Parcelable.Creator<LogEntry>() {
        public LogEntry createFromParcel(Parcel in) {
            return new LogEntry(in);
        }

        public LogEntry[] newArray(int size) {
            return new LogEntry[size];
        }
    };

    public LogEntry(Parcel in){
        //This needs to be same order as above this will save you HOURS of debugging....
        tagname = in.readString();
        measurement = in.readFloat();
    }


}
