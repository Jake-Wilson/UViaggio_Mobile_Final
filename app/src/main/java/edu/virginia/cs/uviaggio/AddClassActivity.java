package edu.virginia.cs.uviaggio;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        courseEditText = (EditText) findViewById(R.id.courseEditText);

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
                    Intent newClass = new Intent();
                    newClass.putExtra("name", ret.get("courseName").toString());
                    newClass.putExtra("instructor", ret.get("instructor").toString());
                    newClass.putExtra("deptID", ret.get("deptID").toString());
                    newClass.putExtra("number", ret.get("courseNum").toString());
                    newClass.putExtra("section", ret.get("section").toString());
                    newClass.putExtra("meetingTime", ret.get("meetingTime").toString());
                    newClass.putExtra("location", ret.get("location").toString());
                    newClass.putExtra("lat", ret.get("lat").toString());
                    newClass.putExtra("lon", ret.get("lon").toString());
                    setResult(RESULT_OK, newClass);
                    finish();
                    Log.d("packing up", "OK");
                }catch(JSONException e){
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
