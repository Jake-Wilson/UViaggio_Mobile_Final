package edu.virginia.cs.uviaggio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AddClassActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://stardock.cs.virginia.edu/louslist/Courses/view/";

    EditText courseEditText;
    TextView courseNameTextView;
    TextView instructorTextView;
    TextView locationTextView;
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView courseNumberTextView;
    TextView meetingTextView;
    TextView sectionTextView;
    TextView departmentIDTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        courseEditText = (EditText) findViewById(R.id.courseEditText);
        courseNameTextView = (TextView) findViewById(R.id.courseNameTextView);
        instructorTextView = (TextView) findViewById(R.id.instructorTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        latitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
        longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        courseNumberTextView = (TextView) findViewById(R.id.courseNumberTextView);
        meetingTextView = (TextView) findViewById(R.id.meetingTextView);
        sectionTextView = (TextView) findViewById(R.id.sectionTextView);
        departmentIDTextView = (TextView) findViewById(R.id.departmentIDTextView);
    }

    public void downloadData(View view) {
        EditText userInput = (EditText) findViewById(R.id.courseEditText);
        String inputText = userInput.getText().toString();
        String[] textArray = inputText.split(" ");

        String requestUrl = "http://stardock.cs.virginia.edu/louslist/Courses/view/" + textArray[0] + "/" + textArray[1] + "?json";

        Log.d("request url", requestUrl);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, requestUrl, (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject ret = response.getJSONObject(0);
                    courseNameTextView.setText("Course Name: " + ret.get("courseName").toString());
                    instructorTextView.setText("Instructor: " + ret.get("instructor").toString());
                    locationTextView.setText("Location: " + ret.get("location").toString());
                    latitudeTextView.setText("Latitude: " + ret.get("lat").toString());
                    longitudeTextView.setText("Longitude: " + ret.get("lon").toString());
                    departmentIDTextView.setText("Department ID: " + ret.get("deptID").toString());
                    courseNumberTextView.setText("Course Number: " + ret.get("courseNum").toString());
                    sectionTextView.setText("Section Number: " + ret.get("section").toString());
                    meetingTextView.setText("Meeting Times: " + ret.get("meetingTime").toString());
                }catch(JSONException e){
                    courseNameTextView.setText("Course Name: INVALID REQUEST");
                    instructorTextView.setText("Instructor: INVALID REQUEST");
                    locationTextView.setText("Location: INVALID REQUEST");
                    latitudeTextView.setText("Latitude: INVALID REQUEST");
                    longitudeTextView.setText("Longitude: INVALID REQUEST");
                    departmentIDTextView.setText("Department ID: INVALID REQUEST");
                    courseNumberTextView.setText("Course Number: INVALID REQUEST");
                    sectionTextView.setText("Section Number: INVALID REQUEST");
                    meetingTextView.setText("Meeting Times: INVALID REQUEST");
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        });
        queue.add(req);
    }
}
