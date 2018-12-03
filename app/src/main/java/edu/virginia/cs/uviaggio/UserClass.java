package edu.virginia.cs.uviaggio;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

public class UserClass implements Parcelable {
    private String name, instructor, deptID, number, section, meetingTime, location, lat, lon, leaveTime;

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(name);
        dest.writeString(instructor);
        dest.writeString(deptID);
        dest.writeString(number);
        dest.writeString(section);
        dest.writeString(meetingTime);
        dest.writeString(location);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(leaveTime);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<UserClass>(){
        @Override
        public UserClass createFromParcel(Parcel in) {
            return new UserClass(in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString()
            );
        }

        @Override public UserClass[] newArray(int size){
            return new UserClass[size];
        }
    };

    public UserClass(String name, String instructor, String deptID, String number, String section, String meetingTime, String location, String lat, String lon, String leaveTime){
        this.name = name;
        this.instructor = instructor;
        this.deptID = deptID;
        this.number = number;
        this.section = section;
        this.meetingTime = meetingTime;
        this.location = location;
        this.lat = lat;
        this.lon = lon;
        this.leaveTime = leaveTime;
    }

    public static ArrayList<UserClass> createInitialClassList(){
        ArrayList<UserClass> classList = new ArrayList<>();
        classList.add(new UserClass("Mobile - Test", "Mark Sherriff", "CS", "4720", "100", "MWF 1:00PM - 1:50PM", "Olsson Hall 005", "38.031639", "-78.510811", "Recommended leave time: "));
        classList.add(new UserClass("PDR - Test", "Mark Floryan", "CS", "2150", "100", "MWF 2:00PM - 2:50PM", "Rice 130", "38.034276", "-78.513005", "Recommended leave time: "));
        return classList;
    }

    public String getName(){
        return name;
    }

    public String getInstructor(){
        return instructor;
    }

    public String getDeptID(){
        return deptID;
    }

    public String getNumber(){
        return number;
    }

    public String getSection(){
        return section;
    }

    public String getMeetingTime(){
        return meetingTime;
    }

    public String getLocation(){
        return location;
    }

    public String getLat(){
        return lat;
    }

    public String getLon(){
        return lon;
    }

    public String getLeaveTime() { return leaveTime; }
}
