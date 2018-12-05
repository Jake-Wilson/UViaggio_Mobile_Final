package edu.virginia.cs.uviaggio;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;

public class UserClass {
    private String name, instructor, deptID, number, section, meetingTime, location, lat, lon, user;
    private long leaveTime, tripsTaken;

    public UserClass(String user, String name, String instructor, String deptID, String number, String section, String meetingTime, String location, String lat, String lon, long leaveTime, long tripsTaken){
        this.user = user;
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
        this.tripsTaken = tripsTaken;
    }

    protected String getUser(){return user;}

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

    public long getLeaveTime() { return leaveTime; }

    public long getTripsTaken() { return tripsTaken;}

    public void setLeaveTime(long leaveTime) {
        long totalTime = (this.leaveTime * this.tripsTaken) + leaveTime;
        this.tripsTaken++;
        long totalTrips = this.tripsTaken;
        this.leaveTime = totalTime / totalTrips;
    }
}
