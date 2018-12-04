package edu.virginia.cs.uviaggio;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        classList = UserClass.createInitialClassList();
        loadFromDatabase();
        setContentView(R.layout.activity_class_card_view);
        setTitle("Class List");
        Log.d("List:", classList.toString());
        rvClassList = findViewById(R.id.rvClassList);
        ClassCardViewAdapter adapter = new ClassCardViewAdapter(this, classList, this);
        rvClassList.setAdapter(adapter);
        rvClassList.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }
    @Override
    protected void onPause(){
        super.onPause();
        saveToDatabase();
    }

    @Override
    protected void onStop(){
        super.onStop();
        saveToDatabase();
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
        gpsIntent.putExtra("name", Class.getName());
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
                        data.getStringExtra("lon"),
                        data.getLongExtra("leaveTime", 999),
                        data.getLongExtra("tripsTaken", 999));
                Log.d("leaveTime for new", String.valueOf(data.getLongExtra("leaveTime", 999)));
                Log.d("tripsTaken for new", String.valueOf(data.getLongExtra("tripsTaken", 999)));
                classList.add(c);
                //TODO: Sort here or sort once when starting main activity??
                rvClassList.getAdapter().notifyDataSetChanged();
            }
        }

        //editClass code
        //TODO: Instead of removing/adding new, can we find a way to just edit existing?
        // send rvclassLIst index in intent since sorting won't change
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
                        data.getStringExtra("lon"),
                        data.getLongExtra("leaveTime", 999),
                        data.getLongExtra("tripsTaken", 999));
                classList.add(c);
                classList.remove(data.getIntExtra("Position", 1000000000));
                //TODO:Sort here or somewhere when main activity loads?
                rvClassList.getAdapter().notifyItemChanged(data.getIntExtra("Position", 1000000000));
            }
        }

        if(requestCode == GPS_RESULT){
            if(resultCode == RESULT_OK){
                //TODO: Store associated (classStartTime(ms) - recordedTime) in class's startAt variable
                for(UserClass user : classList){
                    if(user.getName().equals(data.getStringExtra("name"))) {
                        user.setLeaveTime(data.getLongExtra("leaveTime", 999));
                        DatabaseHelper mDBHelper = new DatabaseHelper(this);
                        SQLiteDatabase db = mDBHelper.getWritableDatabase();
                        String insertString = "UPDATE classes SET leaveTime = " + data.getLongExtra("leaveTime", 999)+ " WHERE name = \"" + data.getStringExtra("name") +"\"";
                        db.execSQL(insertString);
                        Log.d("GPS class update", insertString);
                        Log.d("item leaveTime",String.valueOf( user.getLeaveTime()));
                    }
                }

                //TODO:Sort here or somewhere when main activity loads?
                rvClassList.getAdapter().notifyDataSetChanged();
            }
        }
    }

    public void onClick(View v){
        int from = v.getId();
        if(from == R.id.fab){
            toAddClass();
        }
    }
    public void saveToDatabase(){
    // Add code here to save to the database
    DatabaseHelper mDBHelper = new DatabaseHelper(this);
    SQLiteDatabase db = mDBHelper.getWritableDatabase();
    UserClass saveClass;
    for (int i=0; i < classList.size(); i++) {
        ContentValues values = new ContentValues();
        saveClass = classList.get(i);
        String selectString = "Select * From Classes WHERE name = " + "\"" + saveClass.getName() + "\"";
        if(db.rawQuery(selectString, null).getCount() == 0) {
            values.put("name", saveClass.getName());
            values.put("instructor", saveClass.getInstructor());
            values.put("deptID", saveClass.getDeptID());
            values.put("number", saveClass.getNumber());
            values.put("section", saveClass.getSection());
            values.put("meetingTime", saveClass.getMeetingTime());
            values.put("location", saveClass.getLocation());
            values.put("lat", saveClass.getLat());
            values.put("lon", saveClass.getLon());
            values.put("leaveTime", saveClass.getLeaveTime());
            values.put("tripsTaken", saveClass.getTripsTaken());
            Log.d("works", saveClass.getName());
            Log.d("tag", values.toString());
            long newRowID;
            newRowID = db.insert("Classes", null, values);
        }
    }
    db.close();
    }
    public void loadFromDatabase() {

        // Add code here to load from the database
        DatabaseHelper mDBHelper = new DatabaseHelper(this);
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String name = "";
        String instructor = "";
        String deptID = "";
        String number = "";
        String section = "";
        String meetingTime = "";
        String location = "";
        String lat = "";
        String lon = "";
        long leaveTime = 0;
        long tripsTaken = 0;
        String[] projection = {"name", "instructor", "deptID", "number", "section", "meetingTime", "location", "lat", "lon", "leaveTime", "tripsTaken"};

        Cursor cursor = db.query(
                "Classes",
                projection,
                null,
                null,
                null,
                null,
                null
        );
        if(cursor.getCount() != 0) {
            classList.clear();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                instructor = cursor.getString(cursor.getColumnIndexOrThrow("instructor"));
                deptID = cursor.getString(cursor.getColumnIndexOrThrow("deptID"));
                number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
                section = cursor.getString(cursor.getColumnIndexOrThrow("section"));
                meetingTime = cursor.getString(cursor.getColumnIndexOrThrow("meetingTime"));
                location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                lat = cursor.getString(cursor.getColumnIndexOrThrow("lat"));
                lon = cursor.getString(cursor.getColumnIndexOrThrow("lon"));
                leaveTime = cursor.getLong(cursor.getColumnIndexOrThrow("leaveTime"));
                tripsTaken = cursor.getLong(cursor.getColumnIndexOrThrow("tripsTaken"));
                Log.d("works", name);
                Log.d("works", instructor);
                Log.d("works", deptID);
                Log.d("works", number);
                Log.d("works", section);
                Log.d("works", meetingTime);
                Log.d("works", location);
                Log.d("works", lat);
                Log.d("works", lon);
                //Log.d("works", leaveTime);
                UserClass newClass = new UserClass(name, instructor, deptID, number, section, meetingTime, location, lat, lon, leaveTime, tripsTaken);
                Log.d("Class", newClass.toString());
                classList.add(newClass);
                cursor.moveToNext();
            }
        } else{ classList = UserClass.createInitialClassList();}
        cursor.close();
    }
}
