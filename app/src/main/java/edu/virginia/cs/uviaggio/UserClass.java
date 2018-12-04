package edu.virginia.cs.uviaggio;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;

public class UserClass {
    private String name, instructor, deptID, number, section, meetingTime, location, lat, lon;
    private long leaveTime, tripsTaken;

    public UserClass(String name, String instructor, String deptID, String number, String section, String meetingTime, String location, String lat, String lon, long leaveTime, long tripsTaken){
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

    public static ArrayList<UserClass> createInitialClassList(){
        ArrayList<UserClass> classList = new ArrayList<>();
        classList.add(new UserClass("Mobile - Test", "Mark Sherriff", "CS", "4720", "100", "MWF 1:00PM - 1:50PM", "Olsson Hall 005", "38.031639", "-78.510811", 45900000, 1));
        classList.add(new UserClass("PDR - Test", "Mark Floryan", "CS", "2150", "100", "MWF 2:00PM - 2:50PM", "Rice 130", "38.034276", "-78.513005", 49500000, 1));
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

    public long getLeaveTime() { return leaveTime; }

    public long getTripsTaken() { return tripsTaken;}

    public void setLeaveTime(long leaveTime) {
        long totalTime = (this.leaveTime * this.tripsTaken) + leaveTime;
        this.tripsTaken++;
        long totalTrips = this.tripsTaken;
        this.leaveTime = totalTime / totalTrips;
    }
}
