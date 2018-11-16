package edu.virginia.cs.uviaggio;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserClass implements Parcelable {
    private String name, instructor, deptID, number, section, meetingTime, location, lat, lon;

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
                    in.readString()
            );
        }

        @Override public UserClass[] newArray(int size){
            return new UserClass[size];
        }
    };

    public UserClass(String name, String instructor, String deptID, String number, String section, String meetingTime, String location, String lat, String lon){
        this.name = name;
        this.instructor = instructor;
        this.deptID = deptID;
        this.number = number;
        this.section = section;
        this.meetingTime = meetingTime;
        this.location = location;
        this.lat = lat;
        this.lat = lon;
    }

    public static ArrayList<UserClass> createInitialClassList(){
        ArrayList<UserClass> classList = new ArrayList<>();
        classList.add(new UserClass("Mobile - Test", "Mark Sherriff", "CS", "4720", "100", "MWF 1:00PM - 1:50PM", "Olsson Hall 005", "30", "122"));
        classList.add(new UserClass("PDR - Test", "Mark Floryan", "CS", "2150", "100", "MWF 2:00PM - 2:50PM", "Rice 130", "60", "200"));
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
}
