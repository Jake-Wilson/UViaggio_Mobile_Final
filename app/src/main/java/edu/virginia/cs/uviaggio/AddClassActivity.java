package edu.virginia.cs.uviaggio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddClassActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://stardock.cs.virginia.edu/louslist/Courses/view/";
    private boolean classText = false;
    private boolean secText = false;
    private String currentUserId;
    EditText courseEditText, sectionEditText;
    Button dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        getSupportActionBar().setTitle("UViaggio");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        courseEditText = (EditText) findViewById(R.id.courseEditText);
        sectionEditText = (EditText) findViewById(R.id.sectionEditText);
        dl = findViewById(R.id.callWebServiceButton);
        final String expression = "[A-Z]{2,4} [0-9]{4}";
        final String expression2 = "[0-9]{3}";
        courseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern pattern = Pattern.compile(expression);
                Matcher matcher = pattern.matcher(s);
                classText = matcher.matches();
                if(secText && classText){
                    Button dl = findViewById(R.id.callWebServiceButton);
                    dl.setEnabled(true);
                } else { dl.setEnabled(false);}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sectionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern pattern = Pattern.compile(expression2);
                Matcher matcher = pattern.matcher(s);
                secText = matcher.matches();
                if(secText && classText){
                    dl.setEnabled(true);
                }else{ dl.setEnabled(false);}

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void downloadData(View view) {
        EditText userInput = (EditText) findViewById(R.id.courseEditText);
        EditText sectionInput = (EditText) findViewById(R.id.sectionEditText);
        String inputText = userInput.getText().toString();
        final String sectionText = sectionInput.getText().toString();
        String[] textArray = inputText.split(" ");

        String requestUrl = "http://stardock.cs.virginia.edu/louslist/Courses/view/" + textArray[0] + "/" + textArray[1] + "?json";

        Log.d("request url", requestUrl);

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, requestUrl, (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject ret = response.getJSONObject(i);
                        if (ret.get("section").toString().equals(sectionText)) {
                            Intent newClass = new Intent();
                            newClass.putExtra("user", currentUserId);
                            newClass.putExtra("name", ret.get("courseName").toString());
                            newClass.putExtra("instructor", ret.get("instructor").toString());
                            newClass.putExtra("deptID", ret.get("deptID").toString());
                            newClass.putExtra("number", ret.get("courseNum").toString());
                            newClass.putExtra("section", ret.get("section").toString());
                            newClass.putExtra("meetingTime", ret.get("meetingTime").toString());
                            newClass.putExtra("location", ret.get("location").toString());
                            newClass.putExtra("lat", ret.get("lat").toString());
                            newClass.putExtra("lon", ret.get("lon").toString());
                            newClass.putExtra("leaveTime", (long)0);
                            newClass.putExtra("tripsTaken", (long)0);
                            setResult(RESULT_OK, newClass);
                            finish();
                            Log.d("packing up", "OK");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
