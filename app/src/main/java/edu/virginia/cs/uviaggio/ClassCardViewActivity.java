package edu.virginia.cs.uviaggio;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ClassCardViewActivity extends FragmentActivity implements View.OnClickListener{
    public static ArrayList<UserClass> classList;
    public RecyclerView rvClassList;
    static final int ADD_CLASS = 0;
    static final int EDIT_CLASS = 1;
    static final int GPS_RESULT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_card_view);
        setTitle("Class List");
        classList = UserClass.createInitialClassList();
        Log.d("List:", classList.toString());
        rvClassList = findViewById(R.id.rvClassList);
        ClassCardViewAdapter adapter = new ClassCardViewAdapter(this, classList, this);
        rvClassList.setAdapter(adapter);
        rvClassList.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    public void toAddClass(){
        Intent addIntent = new Intent(ClassCardViewActivity.this, AddClassActivity.class);
        startActivityForResult(addIntent,ADD_CLASS);
    }
    public void launchGPS(View view){
        Intent gpsIntent = new Intent(ClassCardViewActivity.this, GpsActivity.class);
        UserClass Class = classList.get((int)view.getTag());
        SimpleDateFormat format = new SimpleDateFormat("hh:mma");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Log.d("Meet time string: ", Class.getMeetingTime().split(" ")[1]);
            Date startTime = format.parse(Class.getMeetingTime().split(" ")[1]);
            Log.d("Meet time parsed:", String.valueOf(startTime));
            Log.d("Meet time millis:", String.valueOf(startTime.getTime()));
            gpsIntent.putExtra("start", startTime.getTime());
        }catch (Exception e){
            Log.e("Error!", e.getStackTrace().toString());
        }
        gpsIntent.putExtra("lat", Class.getLat());
        gpsIntent.putExtra("lon",Class.getLon());
        startActivityForResult(gpsIntent, GPS_RESULT);
    }



//    public void editClass(View view){
//        Intent editIntent = new Intent(this, EditItemActivity.class);
//
//        UserClass c = classList.get((int)view.getTag());
//
//        //TODO: Use putExtra to pass the data to the editing form
//        startActivityForResult(editIntent, 1);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //addClass code
        if(requestCode == ADD_CLASS){
            if(resultCode == RESULT_OK){
                UserClass c = new UserClass(
                        data.getStringExtra("name"),
                        data.getStringExtra("instructor"),
                        data.getStringExtra("deptID"),
                        data.getStringExtra("number"),
                        data.getStringExtra("section"),
                        data.getStringExtra("meetingTime"),
                        data.getStringExtra("location"),
                        data.getStringExtra("lat"),
                        data.getStringExtra("lon"));
                classList.add(c);
                //TODO: Sort here or sort once when starting main activity??
                rvClassList.getAdapter().notifyDataSetChanged();
            }
        }

        //editClass code
        //TODO: Instead of removing/adding new, can we find a way to just edit existing?
        if(requestCode == EDIT_CLASS){
            if(resultCode == RESULT_OK){
                //Swap Items
                UserClass c = new UserClass(
                        data.getStringExtra("name"),
                        data.getStringExtra("instructor"),
                        data.getStringExtra("deptID"),
                        data.getStringExtra("number"),
                        data.getStringExtra("section"),
                        data.getStringExtra("meetingTime"),
                        data.getStringExtra("location"),
                        data.getStringExtra("lat"),
                        data.getStringExtra("lon"));
                classList.add(c);
                classList.remove(data.getIntExtra("Position", 1000000000));
                //TODO:Sort here or somewhere when main activity loads?
                rvClassList.getAdapter().notifyItemChanged(data.getIntExtra("Position", 1000000000));
            }
        }

        if(requestCode == GPS_RESULT){
            if(resultCode == RESULT_OK){
                //TODO: Store associated (classStartTime(ms) - recordedTime) in class's startAt variable

                //TODO:Sort here or somewhere when main activity loads?
                rvClassList.getAdapter().notifyItemChanged(data.getIntExtra("Position", 1000000000));
            }
        }
    }

    public void onClick(View v){
        int from = v.getId();
        if(from == R.id.fab){
            toAddClass();
        }
    }
}
