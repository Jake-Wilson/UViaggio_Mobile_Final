package edu.virginia.cs.uviaggio;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    /**
     Assignment Notes: This code is provided as part of the SQLite feature.
     You do not need to edit this code.  Note that it provides the functionality
     to create a blank database if one does not exist.
     */

    public class DatabaseHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Final.db";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table Classes (name varchar(25), instructor varchar(25), deptID varchar(25), number varchar(25), section varchar(25), meetingTime varchar(25), location varchar(25), lat varchar(25), lon varchar(25), leaveTime varchar(50))");
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
//            db.execSQL("delete table person");
//            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
