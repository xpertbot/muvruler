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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tagname);
        dest.writeFloat(this.measurement);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public LogEntry createFromParcel(Parcel in) {
            return new LogEntry(in);
        }

        public LogEntry[] newArray(int size) {
            return new LogEntry[size];
        }
    };

    public LogEntry(Parcel in){
        this.measurement = in.readFloat();
        this.tagname = in.readString();
    }

    public int describeContents(){
        return 0;
    }
}
